package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.repository.WindowRepo;
import com.G2T7.OurGardenStory.service.WindowService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class WindowController {
    @Autowired
    private WindowService windowService;

    @GetMapping(path = "/window")
    public List<Window> findWindowByID(@RequestParam(name = "id") Optional<String> id) {
        return new ArrayList<Window>();
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
        } catch (DynamoDBMappingException e) {
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
        } catch (DynamoDBMappingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
