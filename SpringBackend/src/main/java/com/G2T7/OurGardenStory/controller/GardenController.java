package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.service.GardenService;

import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@Api(value = "Community Controller", description = "Operations pertaining to Community model")
public class GardenController {

    private final GardenService gardenService;

    @Autowired
    public GardenController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    /**
    * Add a garden 
    * If the garden already exists or an invalid garden is parsed, throw an exception
    *
    * @param garden a Garden object
    * @return a Garden object that is newly created
    */
    @ApiOperation(value = "Add a Garden")
    @PostMapping(path = "/garden")
    public ResponseEntity<?> saveGarden(@RequestBody Garden garden) {
        try {
            return ResponseEntity.ok(gardenService.createGarden(garden));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Get the Garden object based on the gardenName. If garden is not found, get all gardens
    *
    * @param gardenName an optional String 
    * @return a Garden object if Garden corresponding to gardenName exists,
    *         or a list of all gardens if no gardenName is provided
    */
    @ApiOperation(value = "Find a Garden based on Garden Name")
    @GetMapping(path = "/garden")
    public ResponseEntity<?> findGarden(@RequestParam(name = "name") Optional<String> gardenName) {
        try {
            if (gardenName.isPresent()) {
                return ResponseEntity.ok(gardenService.findGardenByGardenName(gardenName.get()));
            }
            return ResponseEntity.ok(gardenService.findAllGardens());
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Updates the gardenAddress and numPlots of a Garden object
    * If the garden does not exist, throw a ResourceNotFoundException
    *
    * @param payload includes String gardenAddress and int numPlots
    * @param gardenName an optional String
    * @return the updated Garden object
    */
    @ApiOperation(value = "Updates the Garden Address and Number of Plots in a garden")
    @PutMapping(path = "/garden")
    public ResponseEntity<?> updateGarden(@RequestBody JsonNode payload,
            @RequestParam(name = "name") String gardenName) {
        try {
            String gardenAddress = payload.get("gardenAddress").asText();
            int numPlots = Integer.parseInt(payload.get("numPlots").asText().trim());

            return ResponseEntity.ok(gardenService.putGarden(gardenName, gardenAddress, numPlots));
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Delete a Garden object
    * If the gardenName parsed does not correspond to an existing Garden
      object, throw a ResourceNotFoundException 
    *
    * @param gardenName an optional String
    * @return no content
    */
    @ApiOperation(value = "Delete a Garden")
    @DeleteMapping(path = "/garden")
    public ResponseEntity<String> deleteGarden(@RequestParam(name = "name") String gardenName) {
        try {
            gardenService.deleteGarden(gardenName);
            return ResponseEntity.noContent().build();
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiOperation(value = "Get all successful Ballots in a Garden")
    @GetMapping(path = "/friends")
    public ResponseEntity<?> findCommunity(@RequestParam(name = "name") Optional<String> gardenName, @RequestHeader Map<String, String> headers) {
        try {
            if (gardenName.isPresent()) {
                return ResponseEntity.ok(gardenService.findGardenByGardenName(gardenName.get()));
            }
            return ResponseEntity.ok(gardenService.findAllGardens());
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}