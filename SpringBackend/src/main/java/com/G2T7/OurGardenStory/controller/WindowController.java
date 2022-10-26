package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.G2T7.OurGardenStory.service.WindowService;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;

@CrossOrigin("*")
@RestController
public class WindowController {
    @Autowired
    private WindowService windowService;

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

    @GetMapping(path = "/window/{winId}/gardens")
    public ResponseEntity<?> findGardensInWindow(@PathVariable String winId) {
        try {
            List<Relationship> relationList = windowService.findGardensInWindow(winId);
            System.out.println(relationList);
            return ResponseEntity.ok(relationList);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/window/{winId}/gardens")
    public ResponseEntity<?> addGardensInWindow(@PathVariable String winId, @RequestBody JsonNode payload) {
        try {
            List<Relationship> gardenRelations = windowService.addGardensInWindow(winId, payload);
            return ResponseEntity.ok(gardenRelations);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
