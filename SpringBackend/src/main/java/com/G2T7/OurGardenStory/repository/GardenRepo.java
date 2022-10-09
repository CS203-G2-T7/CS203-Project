package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.model.Garden;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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

    public Garden getGardenByGardenName(String gardenName) {
        List<Garden> gardenList = listGardens();

        for (Garden garden : gardenList) {
            if (garden.getName().equals(gardenName)) {
                return garden;
            }
        }

        return null;
    }

    public Garden findGardenById(String gardenId) {
        Garden hashKObject = new Garden();
        hashKObject.setGardenId(gardenId);
        DynamoDBQueryExpression<Garden> queryExpression = new DynamoDBQueryExpression<Garden>()
                .withHashKeyValues(hashKObject);
        List<Garden> gardenList = dynamoDBMapper.query(Garden.class, queryExpression);
        if (gardenList.size() == 0) {
            // Throw some error. Return 404.
            // System.out.println("Garden with id: " + gardenId + " not found.");
            return null;
        }
        // System.out.println(gardenList.get(0).getName());
        return gardenList.get(0);
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
}
