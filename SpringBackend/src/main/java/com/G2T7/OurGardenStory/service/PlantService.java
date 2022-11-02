package com.G2T7.OurGardenStory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Plant;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.*;

@Service
public class PlantService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<Plant> findAllPlants() {
        // Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        // eav.put(":PLNT", new AttributeValue().withS("Plant"));
        // DynamoDBQueryExpression<Plant> qe = new DynamoDBQueryExpression<Plant>()
        //     .withKeyConditionExpression("PK = :PLNT").withExpressionAttributeValues(eav);
    
        // PaginatedQueryList<Plant> foundGardenList = dynamoDBMapper.query(Plant.class, qe);
        // if (foundGardenList.size() == 0) {
        //   throw new ResourceNotFoundException("No gardens found.");
        // }
        // return foundGardenList;

        PaginatedScanList<Plant> plantList = dynamoDBMapper.scan(Plant.class, null);
        return plantList;
      }

    public Plant findPlantByName(final String plantName) {
        // process query param. Remove dashes.
        Plant foundPlant = dynamoDBMapper.load(Plant.class, "Plant", plantName);
        if (foundPlant == null) {
          throw new ResourceNotFoundException("Plant not found"); // might not be right exception
        }
        return foundPlant;
      }

      public Plant createPlant(final Plant plant) {
        if (plant == null) {
          throw new IllegalArgumentException("Invalid plant provided.");
        }
        plant.setPK("Plant");
    
        // Find if already exist in table. Throw error.
        Plant foundPlant = dynamoDBMapper.load(Plant.class, "Plant", plant.getSK());
        if (foundPlant != null) {
          throw new RuntimeException("Plant already exists.");
        }
        dynamoDBMapper.save(plant);
        return plant;
      }

      public Plant putPlant(final String plantName, final String description) {
        Plant foundPlant = findPlantByName(plantName);
        if (description == null) {
          throw new IllegalArgumentException("Invalid plant description.");
        }
        foundPlant.setDescription(description);
        dynamoDBMapper.save(foundPlant);
        return foundPlant;
      }

      public void deletePlant(final String plantName) {
        Plant toDeletePlant = findPlantByName(plantName);
        dynamoDBMapper.delete(toDeletePlant);
      }
}
