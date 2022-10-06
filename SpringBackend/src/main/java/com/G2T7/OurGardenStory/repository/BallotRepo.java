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
import java.util.Base64;
import java.util.List;

@Repository
public class BallotRepo {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

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
                return payload.substring(payload.indexOf(":\"") + 2, payload.length() - 2); // username is returned
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

    protected LocalDateTime findLeaseStart(Window window) {
        int duration = Character.getNumericValue(window.getDuration().charAt(0));
        return window.getStartDateTime().plusMonths(duration);
    }

    public Ballot save(Ballot ballot) {
        ballot.setSubmitDateTime(LocalDateTime.now());
        Window window = findWindow
                (dynamoDBMapper.scan(Window.class, new DynamoDBScanExpression()), ballot); // I do not know how to call listWindows in WindowRepo
        ballot.setStartDateTime(window.getStartDateTime());
        ballot.setLeaseStart(findLeaseStart(window));
        String postCode = "Singapore " + findPostCodeByIdToken(getPayloadAttributes()); // add Singapore prefix to address
        String username = findUsernameByIdToken(getPayloadAttributes());
        ballot.setUsername(username);
        dynamoDBMapper.save(ballot);
        return ballot;
    }
}
