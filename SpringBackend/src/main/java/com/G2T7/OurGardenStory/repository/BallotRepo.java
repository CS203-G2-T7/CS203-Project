package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.model.UserSignInResponse;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Base64;

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
        for (String elem : payload_attr) {
            if (elem.contains("address")) {
                return elem.substring(elem.indexOf(":\"") + 2, elem.indexOf("\"}")); // address is returned
            }
        }
        return null;
    }

    protected String findUsernameByIdToken(String[] payload_attr) {
        for (String elem : payload_attr) {
            if (elem.contains("username")) {
                System.out.println(elem);
                return elem.substring(elem.indexOf(":\"") + 2, elem.length() - 2); // username is returned
            }
        }
        return null;
    }

    public Ballot save(Ballot ballot) {
        ballot.setSubmitDateTime(LocalDateTime.now());
        String postCode = "Singapore " + findPostCodeByIdToken(getPayloadAttributes()); // add Singapore prefix to address
        String username = findUsernameByIdToken(getPayloadAttributes());
        ballot.setUsername(username);
        dynamoDBMapper.save(ballot);
        return ballot;
    }
}
