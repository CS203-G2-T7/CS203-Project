package com.G2T7.OurGardenStory.controller;

import java.util.*;

import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.G2T7.OurGardenStory.service.CommunityService;

import io.swagger.annotations.*;

@CrossOrigin("*")
@RestController
@Api(value = "Community Controller", description = "Operations pertaining to Community model")
public class CommunityController {

    private final CommunityService communityService;

    @Autowired public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    /**
     * Gets a list of users with successful ballots for their own garden to connect with
     * If the garden is not found, throw an exception
     *
     * @params headers containing logged in user
     * @return the list of users with successful ballots for their own garden
     */
    @ApiOperation(value = "Get all successful Ballots in a Garden")
    @GetMapping(path = "/community")
    public ResponseEntity<?> findSuccessfulBallotsInGarden(@RequestHeader Map<String, String> headers) {
        try {
            JSONObject jsonObject = communityService.findUserWithSuccessfulBallotInGarden(headers.get("username"));
            return ResponseEntity.ok(jsonObject);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
