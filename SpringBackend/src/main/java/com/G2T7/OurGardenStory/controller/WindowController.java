package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.service.WindowService;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class WindowController {
    @Autowired
    private WindowService windowService;

    @GetMapping(path = "/window")
    public ResponseEntity<List<Window>> findWindow(@RequestParam(name = "id") Optional<String> id) {
        try {
            if (id.isPresent()) {
                return ResponseEntity.ok(windowService.findWindowById(id.get()));
            }
            return ResponseEntity.ok(windowService.findAllWindows());
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/window")
    public ResponseEntity<?> saveWindow(@RequestBody Window postedWindow) {
        try {
            return ResponseEntity.ok(windowService.createWindow(postedWindow));
        } catch (DynamoDBMappingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping(path = "/window")
    public ResponseEntity<?> updateWindow(@RequestBody JsonNode payload) {
        try {
            String windowDuration = payload.get("windowDuration").asText();
            String windowId = payload.get("windowId").asText();
            return ResponseEntity.ok(windowService.putWindow(windowDuration, windowId));
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/window")
    public ResponseEntity<String> deleteWindow(@RequestBody JsonNode payload) {
        try {
            String windowId = payload.get("windowId").asText();
            windowService.deleteWindow(windowId);
            return ResponseEntity.noContent().build();
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
