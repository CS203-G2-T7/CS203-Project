package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.model.Ballot;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class BallotRepo {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Ballot save(Ballot ballot) {
        ballot.setSubmitDateTime(LocalDateTime.now());
        dynamoDBMapper.save(ballot);
        return ballot;
    }
}
