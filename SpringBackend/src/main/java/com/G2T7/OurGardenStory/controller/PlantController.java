package com.G2T7.OurGardenStory.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.G2T7.OurGardenStory.model.Plant;
import com.G2T7.OurGardenStory.service.PlantService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@CrossOrigin("*")
@RestController
public class PlantController {
    @Autowired
    private PlantService plantService;

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
