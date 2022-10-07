package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public Garden getGardenByGardenName(String gardenName) {
        List<Garden> gardenList = listGardens();

        for (Garden garden : gardenList) {
            if (garden.getName().equals(gardenName)) {
                return garden;
            }
        }

        return null;
    }

    public List<Garden> listGardens() {
        List<Garden> gardenList = dynamoDBMapper.scan(Garden.class, new DynamoDBScanExpression());
        return gardenList;
    }

    public List<String> listGardenNames() {
        List<String> gardenListNames = new ArrayList<>();
        for (Garden garden : listGardens()) {
            gardenListNames.add(garden.getName());
        }
        return gardenListNames;
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
