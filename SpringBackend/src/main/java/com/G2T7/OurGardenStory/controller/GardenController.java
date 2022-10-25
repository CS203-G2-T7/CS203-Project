package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.service.GardenService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class GardenController {
    @Autowired
    private GardenService gardenService;
    
    @PostMapping(path = "/garden")
    public Garden saveGarden(@RequestBody Garden garden) {
        return gardenService.createGarden(garden);
    }

    @GetMapping(path = "/garden")
    public ResponseEntity<List<Garden>> findGarden(@RequestParam(name = "gardenAddress") Optional<String> gardenAddress) {
        try {
            if (gardenAddress.isPresent()) {
                return ResponseEntity.ok(gardenService.findGardenByGardenAddress(gardenAddress.get()));
            }
            return ResponseEntity.ok(gardenService.findAllGardens());
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "/garden")
    public ResponseEntity<?> updateGarden(@RequestBody JsonNode payload) {
        try {
            String gardenAddress = payload.get("gardenAddress").asText();
            int numPlots = Integer.parseInt(payload.get("numPlots").asText());
            
            return ResponseEntity.ok(gardenService.putGarden(gardenAddress, numPlots));
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/garden")
    public ResponseEntity<String> deleteGarden(@RequestBody JsonNode payload) {
        try {
            String gardenAddress = payload.get("gardenAddress").asText();
            gardenService.deleteGarden(gardenAddress);
            return ResponseEntity.noContent().build();
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}