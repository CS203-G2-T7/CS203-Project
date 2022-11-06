package com.G2T7.OurGardenStory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Plant;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.*;

@Service
public class PlantService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
    * Gets a list of all Plant objects in database
    * If there are no Plants in database, throw ResourceNotFoundException
    *
    * @return a list of all Plant objects in database
    */
    public List<Plant> findAllPlants() {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":PLNT", new AttributeValue().withS("Plant"));
        DynamoDBQueryExpression<Plant> qe = new DynamoDBQueryExpression<Plant>()
            .withKeyConditionExpression("PK = :PLNT").withExpressionAttributeValues(eav);
    
        PaginatedQueryList<Plant> foundPlantList = dynamoDBMapper.query(Plant.class, qe);
        if (foundPlantList.size() == 0) {
          throw new ResourceNotFoundException("No gardens found.");
        }
        return foundPlantList;
      }

    /**
    * Get the Plant object that corresponds to a plantName
    * If the plantName does not correspond to an existing Plant, throw ResourceNotFoundException
    *
    * @param plantName a String
    * @return the Plant object corresponding to the given plantName
    */
    public Plant findPlantByName(final String plantName) {
        // process query param. Remove dashes.
        Plant foundPlant = dynamoDBMapper.load(Plant.class, "Plant", plantName);
        if (foundPlant == null) {
          throw new ResourceNotFoundException("Plant not found"); // might not be right exception
        }
        return foundPlant;
      }

    /**
    * Save a new Plant object into the database
    * If the Plant object parsed is invalid, throw IllegalArgumentException
    *
    * @param plant a Plant object
    * @return the newly created Plant object
    */
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

    /**
    * Update the description of an already existing Plant object
    * If Plant object corresponding to the given plantName is not found, or description is null, throw an Exception
    *
    * @param plantName
    * @param description the new description to be updated
    * @return the updated Plant object, if update was successful
    */
      public Plant putPlant(final String plantName, final String description) {
        Plant foundPlant = findPlantByName(plantName);
        if (description == null) {
          throw new IllegalArgumentException("Invalid plant description.");
        }
        foundPlant.setDescription(description);
        dynamoDBMapper.save(foundPlant);
        return foundPlant;
      }


    /**
    * Delete an already existing Plant object from the database
    * If there is no Plant object corresponding to the given plantName, throw ResourceNotFoundException
    *
    * @param plantName
    * @return the deleted Plant object, if deletion was successful
    */
      public void deletePlant(final String plantName) {
        Plant toDeletePlant = findPlantByName(plantName);
        dynamoDBMapper.delete(toDeletePlant);
      }
}
