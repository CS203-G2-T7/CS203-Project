package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.model.Ballot;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Repository
public class BallotRepo {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private WindowRepo windowRepo;

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

    protected Window findWindow(List<Window> windowList, Ballot ballot) {
        for (Window window : windowList) {
            LocalDateTime startDateTime = window.getStartDateTime();
            LocalDateTime submitDateTime = ballot.getSubmitDateTime();
            int days = submitDateTime.getDayOfYear() - startDateTime.getDayOfYear(); // just handle day diff for now
            if (days <= 30) {
                return window;
            }
        }

        return null;
    }

    public Ballot save(Ballot ballot) {
        ballot.setSubmitDateTime(LocalDateTime.now());
        Window window = findWindow
                (dynamoDBMapper.scan(Window.class, new DynamoDBScanExpression()), ballot); // I do not know how to call listWindows in WindowRepo
        ballot.setStartDateTime(window.getStartDateTime());
        ballot.setLeaseStart(window.getLeaseStart());
        String postCode = "Singapore " + findPostCodeByIdToken(getPayloadAttributes()); // add Singapore prefix to address
        Set<String> gardenSet = window.getGardenSet();
        if (!gardenSet.contains(ballot.getGarden())) {
            return null;
        }
        String username = findUsernameByIdToken(getPayloadAttributes());
        ballot.setUsername(username);
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
            try {
                if (ballot.getStartDateTime().equals(window.getStartDateTime())) {
                    returnBallotList.add(ballot);
                }
            } catch (NullPointerException e) {
                continue;
            }
        }
        return returnBallotList;
    }
}
