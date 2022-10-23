package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.repository.WindowRepo;
import com.G2T7.OurGardenStory.service.WindowService;
import com.amazonaws.Response;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class WindowController {
    @Autowired
    private WindowService windowService;

    @GetMapping(path = "/window")
    public ResponseEntity<List<Window>> findWindow(@RequestParam(name = "id") Optional<String> id) {
        List<Window> foundWindows = new ArrayList<Window>();
        try {
            // id defined. Get window by ID
            if (id.isPresent()) {
                foundWindows.add(windowService.findWindowById(id.get()));
                return foundWindows.size() == 0 ? ResponseEntity.notFound().build()
                        : ResponseEntity.ok(foundWindows);
            }

            // id null. Get all windows.
            foundWindows = windowService.findAllWindows();
            return foundWindows.size() == 0 ? ResponseEntity.notFound().build()
                    : ResponseEntity.ok(foundWindows);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/window")
    public ResponseEntity<String> saveWindow(@RequestBody Window postedWindow) {
        try {
            return ResponseEntity.ok(windowService.createWindow(postedWindow).toString());
        } catch (DynamoDBMappingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping(path = "/window")
    public ResponseEntity<String> updateWindow(@RequestBody Window putWindow) {
        try {
            return ResponseEntity.ok(windowService.putWindow(putWindow).toString());
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/window")
    public ResponseEntity<String> deleteWindow(@RequestBody Window deleteWindow) {
        System.out.println("Mapped");
        try {
            windowService.deleteWindowByPkSk(deleteWindow.getPK(), deleteWindow.getSK());
            return ResponseEntity.noContent().build();
        } catch (DynamoDBMappingException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
