package com.G2T7.OurGardenStory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.repository.query.Param;
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
import com.G2T7.OurGardenStory.service.AlgorithmServiceImpl;
import com.G2T7.OurGardenStory.service.BallotService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class BallotController {

    @Autowired
    private BallotService ballotService;

    @Autowired
    private AlgorithmServiceImpl algorithmServiceImpl;

    /**
    * Gets a list of all ballots for a particular window and garden
    * If there are no ballots in the window garden, throw a ResourceNotFoundException
    *
    * @param winId a String
    * @param payload which includes a String gardenName
    * @return the list of current ballots for window garden
    */
    @GetMapping(path = "/window/{winId}/{gardenName}/allBallot")
    public ResponseEntity<List<Relationship>> findAllBallotsInWindowGarden(@PathVariable String winId,
            @PathVariable String gardenName, @RequestHeader Map<String, String> headers) {
        try {
            return ResponseEntity
                    .ok(ballotService.findAllBallotsInWindowGarden(winId, gardenName));
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Gets a ballot corresponding to a username in a window
    * If there is no ballot corresponding to the username in the window, throw a ResourceNotFoundException.
    *
    * @param winId a String
    * @param headers containing the username as a key
    * @return the ballot corresponding to the username, in the window
    */
    @GetMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<?> findUserBallotInWindow(@PathVariable String winId,
            @RequestHeader Map<String, String> headers) {
        try {
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

    /**
    * Adds a ballot for a user, in a window for a garden
    * If the window or garden is not found, throw an exception
    *
    * @param winId a String
    * @param payload which includes a String gardenName
    * @param headers containing the username as a key
    * @return the newly posted ballot in that window garden
    */
    @PostMapping(path = "/window/{winId}/ballot")
    public ResponseEntity<?> addBallotInWindowGarden(@PathVariable String winId, @RequestBody JsonNode payload,
            @RequestHeader Map<String, String> headers) {
        try {
            Relationship ballot = ballotService.addBallotInWindowGarden(winId, headers.get("username"), payload);
            return ResponseEntity.ok(ballot);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
    * Update the garden in which the ballot is posted for
    * If the requested garden is not available, throw an exception
    *
    * @param winId a String
    * @param payload which includes a String gardenName
    * @param headers containing the username as a key
    * @return the updated Ballot object
    */
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

    /**
    * Delete the ballot that was posted previously in a window
    * If the ballot to be deleted is invalid or not found, throw an exception
    *
    * @param winId a String
    * @param headers containing the username as a key
    * @return a message "Ballot deleted" if deleted successfully
    */
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

    // @PostMapping(path = "/doMagic")
    // public void doMagic(@RequestBody JsonNode payload) {
    //     algorithmServiceImpl.doMagic(payload.get("winId").asText());
    // }

}
