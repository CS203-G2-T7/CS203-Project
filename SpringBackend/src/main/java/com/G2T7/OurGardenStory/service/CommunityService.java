package com.G2T7.OurGardenStory.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.*;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;

@Service
public class CommunityService {

    private UserService userService;
    private WindowService windowService;
    private BallotService ballotService;

    @Autowired
    public CommunityService(UserService userService, WindowService windowService, BallotService ballotService) {
        this.userService = userService;
        this.windowService = windowService;
        this.ballotService = ballotService;
    }

    /**
     * Gets a list of all users with successful ballots for a particular garden
     * This is done by finding all the windows and all the ballots associated with that particular window and garden
     * Removes ballots that are not successful
     * Returns a list of users from those ballots
     *
     * @param gardenName a String
     * @return the list of users with successful ballots for a particular garden
     */
    public List<User> findUserWithSuccessfulBallotInGarden(String gardenName) {
        List<String> allWinId = new ArrayList<>();
        List<Relationship> allSuccessfulBallots = new ArrayList<>();
        List<User> allUsers = new ArrayList<>();

        for (Window window : windowService.findAllWindows()) {
            allWinId.add(window.getWindowId());
        }

        for (String winId : allWinId) {
            allSuccessfulBallots.addAll(ballotService.findAllBallotsInWindowGarden(winId, gardenName));
        }

        Iterator<Relationship> allSuccessfulBallotsIterator = allSuccessfulBallots.iterator();
        while (allSuccessfulBallotsIterator.hasNext()) {
            Relationship ballot = allSuccessfulBallotsIterator.next();
            if (!ballot.getBallotStatus().equals("SUCCESS")) {
                allSuccessfulBallotsIterator.remove();
            }
        }

        allSuccessfulBallotsIterator.forEachRemaining(allSuccessfulBallots::add);
        
        for (Relationship successfulBallots : allSuccessfulBallots) {
            allUsers.add(userService.findUserByUsername(successfulBallots.getSK()));
        }
        
        return allUsers;
    }
}
