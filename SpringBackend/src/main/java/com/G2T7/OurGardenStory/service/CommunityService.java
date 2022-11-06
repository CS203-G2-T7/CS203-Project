package com.G2T7.OurGardenStory.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;

@Service
public class CommunityService {

    @Autowired
    private UserService userService;

    @Autowired
    private WindowService windowService;

    @Autowired
    private BallotService ballotService;

    public List<User> findUserWithSucessfulBallotInGarden(String gardenName) {
        List<Window> allWindow = windowService.findAllWindows();
        List<String> allWinId = new ArrayList<>();
        List<Relationship> allSuccessfulBallots = new ArrayList<>();
        List<User> allUsers = new ArrayList<>();

        for (Window window : allWindow) {
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
