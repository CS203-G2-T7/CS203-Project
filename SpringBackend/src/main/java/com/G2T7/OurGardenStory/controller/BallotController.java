package com.G2T7.OurGardenStory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.G2T7.OurGardenStory.service.BallotService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;

@CrossOrigin("*")
@RestController
public class BallotController {

    @Autowired
    private BallotService ballotService;

    @GetMapping(path = "/window/{winId}/allBallot")
    public ResponseEntity<List<Relationship>> findAllBallots(@PathVariable String winId, @RequestBody JsonNode payload,
        @RequestHeader Map<String, String> headers) {
        try {
            return ResponseEntity.ok(ballotService.findAllBallotsInWindowGarden(winId, payload.get("gardenName").asText()));
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<?> findUserBallotInWindow(@PathVariable String winId, 
            @RequestHeader Map<String, String> headers) {
        try {
            System.out.println(headers.get("username"));
            Relationship ballot = ballotService.findUserBallotInWindow(winId, headers.get("username"));
            return ResponseEntity.ok(ballot);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<?> addBallotInWindow(@PathVariable String winId, @RequestBody JsonNode payload,
            @RequestHeader Map<String, String> headers) {
        try {
            Relationship ballot = ballotService.addBallotInWindow(winId, headers.get("username"), payload);
            return ResponseEntity.ok(ballot);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<?> updateBallotInWindow(@PathVariable String winId, @RequestBody JsonNode payload,
            @RequestHeader Map<String, String> headers) {
        try {
            Relationship updatedRelation = ballotService.updateGardenInBallot(winId, headers.get("username"), payload);
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
    public ResponseEntity<String> deleteBallotInWindow(@PathVariable String winId,
            @RequestHeader Map<String, String> headers) {
        try {
            ballotService.deleteBallotInWindow(winId, headers.get("username"));
            // return ResponseEntity.noContent().build();
            return ResponseEntity.ok().body("Ballot deleted");
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
