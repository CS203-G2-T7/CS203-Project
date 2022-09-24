package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.model.Garden;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GardenRepo {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Garden save(Garden garden) {
        dynamoDBMapper.save(garden);
        return garden;
    }

    public Garden getGardenById(String gardenId) {
        return dynamoDBMapper.load(Garden.class, gardenId);
    }

    public String delete(String gardenId) {
        Garden garden = dynamoDBMapper.load(Garden.class, gardenId);
        dynamoDBMapper.delete(garden);
        return "Garden Deleted!";
    }

    public String update(String gardenId, Garden garden) {
        dynamoDBMapper.save(garden,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("GardenId",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(gardenId)
                                )));
        return gardenId;
    }
}
