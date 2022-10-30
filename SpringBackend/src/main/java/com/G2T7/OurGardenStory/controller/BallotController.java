package com.G2T7.OurGardenStory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.G2T7.OurGardenStory.model.RelationshipModel.Ballot;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.G2T7.OurGardenStory.service.BallotService;
import com.G2T7.OurGardenStory.service.RelationshipService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;

@CrossOrigin("*")
@RestController
public class BallotController {
    
    @Autowired
    private BallotService ballotService;

    @GetMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<?> findBallotInWindow(@PathVariable String winId) {
        try {
            String username = ballotService.getUsername();

            Relationship ballot = ballotService.findUsernameInBallot(winId, username);
            
            return ResponseEntity.ok(ballot);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<?> addBallotInWindow(@PathVariable String winId, @RequestBody JsonNode payload) {
        try {
            String username = ballotService.getUsername();
            System.out.println(username);

            List<Relationship> ballotRelations = ballotService.addBallotInWindow(winId, username, payload);
            return ResponseEntity.ok(ballotRelations);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<?> updateBallotInWindow(@PathVariable String winId, @RequestBody JsonNode payload) {
        try {
            String username = ballotService.getUsername();

            Relationship updatedRelation = ballotService.updateGardenInBallot(winId, username, payload);
            return ResponseEntity.ok(updatedRelation);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<String> deleteBallotInWindow(@PathVariable String winId) {
        try {
            String username = ballotService.getUsername();            
            ballotService.deleteBallotInWindow(winId, username);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
