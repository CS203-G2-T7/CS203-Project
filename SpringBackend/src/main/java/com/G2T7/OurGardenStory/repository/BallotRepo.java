package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.controller.GeocodeController;
import com.G2T7.OurGardenStory.geocoder.Algorithm;
import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.UserSignInResponse;
import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Repository
public class BallotRepo {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private WindowRepo windowRepo;

    @Autowired
    private GardenRepo gardenRepo;

    @Autowired
    private GeocodeController geocodeController;

    protected String[] getPayloadAttributes() {
        String idToken = UserSignInResponse.getIdToken();
        String[] chunks = idToken.split("\\."); // chunk 0 is header, chunk 1 is payload

        Base64.Decoder decoder = Base64.getUrlDecoder(); // Decode via Base64

        String payload = new String(decoder.decode(chunks[1])); // gets payload from idToken
        String[] payload_attr = payload.split(",");

        return payload_attr;
    }

    protected String findPostCodeByIdToken(String[] payload_attr) {
        for (String payload : payload_attr) {
            if (payload.contains("address")) {
                return payload.substring(payload.indexOf(":\"") + 2, payload.indexOf("\"}")); // address is returned
            }
        }
        return null;
    }

    protected String findUsernameByIdToken(String[] payload_attr) {
        for (String payload : payload_attr) {
            if (payload.contains("username")) {
                return payload.substring(payload.indexOf(":\"") + 2, payload.length() - 1); // username is returned
            }
        }
        return null;
    }

    public Ballot save(String gardenName) {
        List<String> gardenListNames = gardenRepo.listGardenNames();
        if (!gardenListNames.contains(gardenName)) {
            return null;
        }

        String username = findUsernameByIdToken(getPayloadAttributes());
        if (checkIfBalloted(username)) {
            return null;
        }

        Ballot ballot = new Ballot();
        ballot.setSubmitDateTime(LocalDateTime.now());
        Window window = windowRepo.findLatestWindow();
        ballot.setStartDateTime(window.getStartDateTime());
        ballot.setLeaseStart(window.getLeaseStart());
        ballot.setNumBidsPlaced(getBidsPlaced(gardenName));
        String postCode = "Singapore " + findPostCodeByIdToken(getPayloadAttributes()); // add Singapore prefix to address
        Garden garden = gardenRepo.getGardenByGardenName(gardenName);
        Double distance = geocodeController.saveDistance(username, postCode, garden.getLongitude(), garden.getLatitude());
        ballot.setDistance(distance);
        ballot.setUsername(username);
        ballot.setGarden(garden);

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    public List<Ballot> listBallots() {
        List<Ballot> ballotList = dynamoDBMapper.scan(Ballot.class, new DynamoDBScanExpression());
        return ballotList;
    }

    public List<Ballot> listBallotsFromLatestWindow() {
        Window window = windowRepo.findLatestWindow();
        List<Ballot> ballotList = listBallots();
        List<Ballot> returnBallotList = new ArrayList<>();
        for (Ballot ballot : ballotList) {
            if (ballot.getStartDateTime().equals(window.getStartDateTime())) {
                returnBallotList.add(ballot);
            }
        }
        return returnBallotList;
    }

    public boolean checkIfBalloted(String username) {
        for (Ballot ballot : listBallotsFromLatestWindow()) {
            if (ballot.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public int getBidsPlaced(String gardenName) {
        int count = 0;
        for (Ballot ballot : listBallotsFromLatestWindow()) {
            if (ballot.getGarden().getName().equals(gardenName)) {
                count += 1;
            }
        }

        return count + 1;
    }
    public List<Ballot> doMagic() {
        List<Ballot> returnBallotList = new ArrayList<>();

        for (Garden garden : windowRepo.findLatestWindow().getGardenList()) {
            System.out.println(garden);
            List<Ballot> ballotList = callAlgo(garden);
            for (Ballot ballot : ballotList) {
                System.out.println(ballot);
                returnBallotList.add(ballot);
            }
        }

        return returnBallotList;
    }

    public List<Ballot> callAlgo(Garden garden) {
        Algorithm algo = new Algorithm();
        HashMap<String, Double> ballotters = new HashMap<>();
            for (Ballot ballot : listBallotsFromLatestWindow()) {
                if (ballot.getGarden().getName().equals(garden.getName())) {
                    ballotters.put(ballot.getUsername(), ballot.getDistance());
                }
        }
        ArrayList<String> output = new ArrayList<>();
        output = algo.getBallotSuccess(ballotters, 1);
        List<Ballot> ballotListForWindow = listBallotsFromLatestWindow();
        List<Ballot> ballotListForWindowForGarden = new ArrayList<>();
        List<Ballot> returnBallotList = new ArrayList<>();
        for (Ballot ballot : ballotListForWindow) {
            try {
                if (ballot.getGarden().getName().equals(garden.getName())) {
                    ballotListForWindowForGarden.add(ballot);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        for (Ballot ballot : ballotListForWindowForGarden) {
            System.out.println(ballot.getStatus() + " " + ballot.getUsername());
            for (String success : output) {
                if (success.equals(ballot.getUsername())) {
                    ballot.setStatus("Successful :)");
                    dynamoDBMapper.save(ballot);
                    returnBallotList.add(ballot);
                }
            }
        }

        for (Ballot ballot : ballotListForWindowForGarden) {
            if (ballot.getStatus().equals("PENDING")) {
                ballot.setStatus("Unsuccessful :(");
                dynamoDBMapper.save(ballot);
                returnBallotList.add(ballot);
            }
        }

        return returnBallotList;
    }
}
