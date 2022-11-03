package com.G2T7.OurGardenStory.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.G2T7.OurGardenStory.service.UserService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@CrossOrigin("*")
@RestController
public class UserPlantController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "/my-plant")
    public ResponseEntity<?> findMyPlant(@RequestParam(name = "name") Optional<String> plantName,
                    @RequestHeader Map<String, String> headers) {
        try {
            if (plantName.isPresent()) {
                return ResponseEntity.ok(userService.findUserPlant(headers.get("username"), plantName.get()));
            }
            return ResponseEntity.ok(userService.findAllUserPlants(headers.get("username")));
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/my-plant")
    public ResponseEntity<?> saveMyPlant(@RequestBody JsonNode payload, @RequestHeader Map<String, String> headers) {
        try {
            return ResponseEntity.ok(userService.addUserPlantName(headers.get("username"), null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "/my-plant")
    public ResponseEntity<?> deleteMyPlant(@RequestBody JsonNode payload,
            @RequestHeader Map<String, String> headers) {
        try {
            return ResponseEntity.ok(userService.removeUserPlantName(payload.get("plantName").asText(), payload));
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
