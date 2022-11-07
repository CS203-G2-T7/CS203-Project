package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.G2T7.OurGardenStory.service.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.*;

@Api(value = "Window Controller", description = "Operations pertaining to Window and GardenWin model")
@CrossOrigin(origins = "https://ourgardenstory.me")
@RestController
public class WindowController {

    private final WindowService windowService;
    private final WinGardenService winGardenService;
    private final AlgorithmServiceImpl algorithmServiceImpl;

    @Autowired
    public WindowController(WindowService windowService, WinGardenService winGardenService, AlgorithmServiceImpl algorithmServiceImpl) {
        this.windowService = windowService;
        this.winGardenService = winGardenService;
        this.algorithmServiceImpl = algorithmServiceImpl;
    }

    /**
    * Gets all windows that exist
    * If there are no windows, throw a ResourceNotFoundException
    *
    * @return a list of all Windows
    */
    @ApiOperation(value = "Get all Windows")
    @GetMapping(path = "/window")
    public ResponseEntity<List<Window>> findAllWindows() {
        try {
            return ResponseEntity.ok(windowService.findAllWindows());
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Find the latest Window based on the start date
    * If there are no Windows in the database, throw ResourceNotFoundException
    *
    * @return an existing Window object that is the latest based on start date
    */
    @ApiOperation(value = "Get the latest Window based on date")
    @GetMapping(path = "/window/latest")
    public ResponseEntity<?> findLatestWindow() {
        try {
            return ResponseEntity.ok(windowService.findLatestWindow());
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Post a new Window, and start a schedule for when to do algorithm on ballots posted in this window
    * If Window with the same start date already exists, throw an Exception
    *
    * @param postedWindow a Window object
    * @return the Window object if added successfully
    */
    @ApiOperation(value = "Save a Window into the database")
    @PostMapping(path = "/window")
    public ResponseEntity<?> saveWindow(@RequestBody Window postedWindow) {
        try {
            Window window = windowService.createWindow(postedWindow);
            String winId = window.getWindowId();
            algorithmServiceImpl.scheduleAlgo(winId);
            return ResponseEntity.ok(window);
        } catch (DynamoDBMappingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
    * Update the windowDuration of an already existing Window
    * If Window does not exist, throw a ResourceNotFoundException
    *
    * @param payload includes a String windowDuration
    * @param id a String
    * @return the updated Window object
    */
    @ApiOperation(value = "Update the windowDuration of a Window given the windowId")
    @PutMapping(path = "/window") // with request param {:id}
    public ResponseEntity<?> updateWindow(@RequestBody JsonNode payload, @RequestParam String id) {
        try {
            String windowDuration = payload.get("windowDuration").asText();
            return ResponseEntity.ok(windowService.putWindow(windowDuration, id));
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
    * Delete an existing Window object
    * If Window does not exist, throw a ResourceNotFoundException
    *
    * @param id a String
    * @return no content
    */
    @ApiOperation(value = "Delete an existing Window")
    @DeleteMapping(path = "/window")
    public ResponseEntity<String> deleteWindow(@RequestParam String id) {
        try {
            windowService.deleteWindow(id);
            return ResponseEntity.noContent().build();
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Relationships

    /**
    * Get a list of all GardenWin Relationship objects that correspond to a particular windowId.
    * If gardenName is provided, return the GardenWin Relationship object corresponding to the winId and gardenName
    * If Window is not found, or there are no Gardens posted in that Window, throw ResourceNotFoundException
    *
    * @param winId a String
    * @param gardenName an optional String
    * @return a list of Relationship objects corresponding to all gardens in a Window, or a Relationship object 
    *         corresponding to the winId and gardenName, if gardenName is given
    */
    @ApiOperation(value = "Get all Gardens for a Window given the windowId")
    @GetMapping(path = "/window/{winId}/garden")
    public ResponseEntity<?> findAllGardensInWindow(@PathVariable String winId,
            @RequestParam(name = "name") Optional<String> gardenName) {
        try {
            if (gardenName.isEmpty()) {
                List<Relationship> relationList = winGardenService.findAllGardensInWindow(winId);
                System.out.println(relationList);
                return ResponseEntity.ok(relationList);
            }
            Relationship gardenRelation = winGardenService.findGardenInWindow(winId, gardenName.get());
            System.out.println(gardenRelation);
            return ResponseEntity.ok(gardenRelation);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Post a new Garden in a Window
    * If Window does not exist, throw ResourceNotFoundException
    *
    * @param winId a String
    * @param payload an array where every object includes a String gardenName, a String leaseDuration, an int numPlotsForBalloting
    * @return a list of all newly added GardenWin Relationship objects, if all added successfully
    */
    @ApiOperation(value = "Add a Garden to a Window given the windowId and gardenName")
    @PostMapping(path = "/window/{winId}/garden")
    public ResponseEntity<?> addGardensInWindow(@PathVariable String winId, @RequestBody JsonNode payload) {
        try {
            List<Relationship> gardenRelations = winGardenService.addGardensInWindow(winId, payload);
            return ResponseEntity.ok(gardenRelations);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Update the leaseDuration and numPlotsForBalloting for a GardenWin Relationship object
    * If Garden is not found inside this Window, throw an error
    *
    * @param winId a String
    * @param gardenName a String
    * @param payload includes a String leaseDuration and an int numPlotsForBalloting
    * @return the updated GardenWin Relationship object, if update was successful
    */
    @ApiOperation(value = "Update the leaseDuration and numPlotsForBalloting for a GardenWin, given the gardenName and windowId")
    @PutMapping(path = "/window/{winId}/garden")
    public ResponseEntity<?> updateGardenInWindow(@PathVariable String winId,
            @RequestParam(name = "name") String gardenName, @RequestBody JsonNode payload) {
        try {
            Relationship updatedRelation = winGardenService.updateGardenInWindow(winId, gardenName, payload);
            return ResponseEntity.ok(updatedRelation);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Delete an existing GardenWin Relationship object
    * If the GardenWin Relationship object does not exist, throw ResourceNotFoundException
    *
    * @param winId a String
    * @param gardenName a String
    * @return no content
    */
    @ApiOperation(value = "Delete a Garden from a Window")
    @DeleteMapping(path = "/window/{winId}/garden")
    public ResponseEntity<String> deleteGardenInWindow(@PathVariable String winId,
            @RequestParam(name = "name") Optional<String> gardenName) {
        try {
            if (gardenName.isPresent()) {
                winGardenService.deleteGardenInWindow(winId, gardenName.get());
                return ResponseEntity.noContent().build();
            }
            winGardenService.deleteAllGardensInWindow(winId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
