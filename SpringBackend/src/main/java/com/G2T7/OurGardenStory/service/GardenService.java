package com.G2T7.OurGardenStory.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Garden;

import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@Service
public class GardenService {

  private final DynamoDBMapper dynamoDBMapper;

  public GardenService (DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  /**
    * When gardenName is an input from the RequestParam, we cannot have space in the name.
    * Method replaces "-" with white space which can then be used for query
    *
    * @param gardenName a String
    * @return the updated gardenName used internally
    */
  private String parseGardenName(final String gardenName) {
    return gardenName.replace("-", " ");
  }

  /**
    * Gets the Garden object corresponding to the given gardenName
    * If there is no Garden object corresponding to the given gardenName, throw ResourceNotFoundException
    *
    * @param gardenName a String
    * @return the Garden object corresponding to the given gardenName
    */
  public Garden findGardenByGardenName(final String gardenName) {
    // process query param. Remove dashes.
    Garden foundGarden = dynamoDBMapper.load(Garden.class, "Garden", parseGardenName(gardenName));
    if (foundGarden == null) {
      throw new ResourceNotFoundException("Garden not found"); // might not be right exception
    }
    return foundGarden;
  }

  /**
    * Gets all Garden objects in database
    * If no Garden objects exist, throw ResourceNotFoundException
    *
    * @return a list of all Garden objects
    */
  public List<Garden> findAllGardens() {
    Map<String, AttributeValue> eav = new HashMap<>();
    eav.put(":GDN", new AttributeValue().withS("Garden"));
    DynamoDBQueryExpression<Garden> qe = new DynamoDBQueryExpression<Garden>()
        .withKeyConditionExpression("PK = :GDN").withExpressionAttributeValues(eav);

    PaginatedQueryList<Garden> foundGardenList = dynamoDBMapper.query(Garden.class, qe);
    if (foundGardenList.size() == 0) {
      throw new ResourceNotFoundException("No gardens found.");
    }
    return foundGardenList;
  }

  /**
    * Saves the Garden object into the database
    * If Garden object is invalid, or if Garden already exist, throw an Exception
    *
    * @param garden a Garden object
    * @return the Garden object created
    */
  public Garden createGarden(final Garden garden) {
    if (garden == null) {
      throw new IllegalArgumentException("Invalid garden provided.");
    }
    garden.setPK("Garden");

    // Find if already exist in table. Throw error.
    Garden foundGarden = dynamoDBMapper.load(Garden.class, "Garden", garden.getSK());
    if (foundGarden != null) {
      throw new RuntimeException("Garden already exists.");
    }
    dynamoDBMapper.save(garden);
    return garden;
  }

  /**
    * Update the gardenAddress and numPlots of a Garden object that corresponds to the gardenName
    * If gardenAddress is null, numPlots is less than or equal to zero, or gardenName does not 
    * correspond to an existing Garden, throw an Exception
    *
    * @param gardenName a String
    * @param gardenAddress a String
    * @param numPlots an int
    * @return the updated Garden object, if updated successfully
    */
  public Garden putGarden(final String gardenName, final String gardenAddress, final int numPlots) {
    Garden foundGarden = findGardenByGardenName(parseGardenName(gardenName));
    if (gardenAddress == null) {
      throw new IllegalArgumentException("Invalid garden address.");
    }
    if (numPlots <= 0) {
      throw new IllegalArgumentException("Invalid number of plots.");
    }
    foundGarden.setGardenAddress(gardenAddress);
    foundGarden.setNumPlots(numPlots);
    dynamoDBMapper.save(foundGarden);
    return foundGarden;
  }

  /**
    * Delete the Garden object that corresponds to given gardenName
    * if gardenName does not correspond to an existing Garden object, throw ResourceNotFoundException
    *
    * @param gardenName a String
    */
  public void deleteGarden(final String gardenName) {
    Garden toDeleteGarden = findGardenByGardenName(parseGardenName(gardenName));
    dynamoDBMapper.delete(toDeleteGarden);
  }
}