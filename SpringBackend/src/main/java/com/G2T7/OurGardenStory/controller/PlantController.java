package com.G2T7.OurGardenStory.controller;

import java.util.*;

import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.G2T7.OurGardenStory.model.Plant;
import com.G2T7.OurGardenStory.service.PlantService;

import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Plant Controller", description = "Operations pertaining to Plant model")
@CrossOrigin({"https://ourgardenstory.me/", "*"})
@RestController
public class PlantController {

    private final PlantService plantService;

    @Autowired
    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    /**
    * Adds a new Plant object
    *
    * @param plant a Plant object
    * @return a new Plant object that is successfully 
    */
    @ApiOperation(value = "Save a new plant into the database")
    @PostMapping(path = "/plant")
    public ResponseEntity<?> savePlant(@RequestBody Plant plant) {
        try {
            return ResponseEntity.ok(plantService.createPlant(plant));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Get the Plant object that corresponds with gardenName. If No plantName is provided, gets all plants
    * If the Plant is not found, throw a ResourceFoundException
    *
    * @param plantName an optional String
    * @return a Plant object if corresponding plantName matches, or all plants if no plantName given
    */
    @ApiOperation(value = "Get a Plant given its plantName, or get all Plants if no plantName given")
    @GetMapping(path = "/plant")
    public ResponseEntity<?> findPlant(@RequestParam(name = "name") Optional<String> plantName) {
        try {
            if (plantName.isPresent()) {
                return ResponseEntity.ok(plantService.findPlantByName(plantName.get()));
            }
            return ResponseEntity.ok(plantService.findAllPlants());
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Update the description of a Plant object that corresponds to the given plantName
    * If the plantName does not correspond to an existing Plant object, throw ResourceNotFoundException
    *
    * @param payload includes a String description
    * @param plantName a String
    * @return the updated Plant object
    */
    @ApiOperation(value = "Update the description of a Plant")
    @PutMapping(path = "/plant")
    public ResponseEntity<?> updatePlant(@RequestBody JsonNode payload,
            @RequestParam(name = "name") String plantName) {
        try {
            String description = payload.get("description").asText();

            return ResponseEntity.ok(plantService.putPlant(plantName, description));
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Delete the Plant object that was previously posted
    * If the Plant object to be deleted is not found, throw an exception
    *
    * @param plantName a String
    * @return no content
    */
    @ApiOperation(value = "Delete an exisiting Plant")
    @DeleteMapping(path = "/plant")
    public ResponseEntity<String> deletePlant(@RequestParam(name = "name") String plantName) {
        try {
            plantService.deletePlant(plantName);
            return ResponseEntity.noContent().build();
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
