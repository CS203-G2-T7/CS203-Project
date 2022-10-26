package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.service.GardenService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@CrossOrigin("*")
@RestController
public class GardenController {
    @Autowired
    private GardenService gardenService;

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

    @GetMapping(path = "/garden")
    public ResponseEntity<?> findGarden(@RequestParam(name = "name") Optional<String> gardenName) {
        try {
            if (gardenName.isPresent()) {
                return ResponseEntity.ok(gardenService.findGardenByGardenName(gardenName.get()));
            }
            return ResponseEntity.ok(gardenService.findAllGardens());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

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
}