package com.G2T7.OurGardenStory.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.G2T7.OurGardenStory.service.UserService;

import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;

import com.fasterxml.jackson.databind.JsonNode;

@CrossOrigin("*")
@RestController
public class UserPlantController {
    private final UserService userService;

    @Autowired
    public UserPlantController(UserService userService) {
        this.userService = userService;
    }

    /**
    * Get the user's plant that corresponds to the given plantName. If plant is not found, get all of user's plants
    *
    * @param plantName an optional String
    * @param headers containing the username as a key
    * @return a Plant object if user's Plant corresponding to plantName exists,
    *         or a list of all user's plants if no plantName is provided
    */
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

    /**
    * Post a new plant into the list of user's plants
    * Throws exception if plantName given does not exist
    *
    * @param payload includes an array of plantNames
    * @param headers containing the username as a key
    * @return the updated list of the user's plants
    */
    @PostMapping(path = "/my-plant")
    public ResponseEntity<?> saveMyPlant(@RequestBody JsonNode payload, @RequestHeader Map<String, String> headers) {
        try {
            System.out.println("username is " + headers.get("username"));
            return ResponseEntity.ok(userService.addUserPlantName(headers.get("username"), payload));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Delete a plant belonging in the list of user's plants
    * Throws exception if plantName does not correspond to a plant that already exists in the list of user's plants
    *
    * @param payload includes an array of plantNames
    * @param headers containing the username as a key
    * @return the updated list of the user's plants
    */
    @DeleteMapping(path = "/my-plant")
    public ResponseEntity<?> deleteMyPlant(@RequestBody JsonNode payload,
            @RequestHeader Map<String, String> headers) {
        try {
            return ResponseEntity.ok(userService.removeUserPlantName(headers.get("username"), payload));
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
