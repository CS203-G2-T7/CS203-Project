package com.G2T7.OurGardenStory.service;

import java.util.*;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.*;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;

@Service
public class CommunityService {

    private final WindowService windowService;
    private final BallotService ballotService;
    private final UserService userService;

    @Autowired
    public CommunityService(UserService userService, WindowService windowService, BallotService ballotService) {
        this.windowService = windowService;
        this.ballotService = ballotService;
        this.userService = userService;
    }

    /**
     * Gets a list of all users with successful ballots for a particular garden
     * This is done by finding all the windows and all the ballots associated with that particular window and garden
     * Removes ballots that are not successful
     * Returns a list of users from those ballots
     *
     * @param username a String
     * @return the list of users with successful ballots for a particular garden
     */
    public JSONArray findUserWithSuccessfulBallotInGarden(String username) {
        List<String> allWinId = new ArrayList<>();
        List<Relationship> allSuccessfulBallots = new ArrayList<>();
        List<User> allUsers = new ArrayList<>();
        String gardenName = "";

        for (Window window : windowService.findAllWindows()) {
            allWinId.add(window.getWindowId());
        }

        for (String winId : allWinId) {
            Relationship ballot = ballotService.findUserBallotInWindow(winId, username);
            if (ballot.getBallotStatus().equals("SUCCESS")) {
                gardenName = ballot.getWinId_GardenName().substring(5);
                allSuccessfulBallots.addAll(ballotService.findAllBallotsInWindowGarden(winId, gardenName));
            }
        }

        Iterator<Relationship> allSuccessfulBallotsIterator = allSuccessfulBallots.iterator();
        while (allSuccessfulBallotsIterator.hasNext()) {
            Relationship ballot = allSuccessfulBallotsIterator.next();
            if (!ballot.getBallotStatus().equals("SUCCESS") || ballot.getSK().equals(username)) {
                allSuccessfulBallotsIterator.remove();
            }
        }

        allSuccessfulBallotsIterator.forEachRemaining(allSuccessfulBallots::add);

        for (Relationship successfulBallots : allSuccessfulBallots) {
            allUsers.add(userService.findUserByUsername(successfulBallots.getSK()));
        }

        JSONArray arr = new JSONArray();
        for (User user: allUsers) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", user.getSK());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("gardenName", gardenName);
            arr.add(jsonObject);
        }

        return arr;
    }
}