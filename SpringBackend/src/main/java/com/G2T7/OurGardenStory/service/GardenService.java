package com.G2T7.OurGardenStory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Garden;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@Service
public class GardenService {
  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  private String parseGardenName(final String gardenName) {
    return gardenName.replace("-", " ");
  }

  public Garden findGardenByGardenName(final String gardenName) {
    // process query param. Remove dashes.
    Garden foundGarden = dynamoDBMapper.load(Garden.class, "Garden", parseGardenName(gardenName));
    if (foundGarden == null) {
      throw new ResourceNotFoundException("Garden not found"); // might not be right exception
    }
    return foundGarden;
  }

  public List<Garden> findAllGardens() {
    Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
    eav.put(":GDN", new AttributeValue().withS("Garden"));
    DynamoDBQueryExpression<Garden> qe = new DynamoDBQueryExpression<Garden>()
        .withKeyConditionExpression("PK = :GDN").withExpressionAttributeValues(eav);

    PaginatedQueryList<Garden> foundGardenList = dynamoDBMapper.query(Garden.class, qe);
    if (foundGardenList.size() == 0) {
      throw new ResourceNotFoundException("No gardens found.");
    }
    return foundGardenList;
  }

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

  public void deleteGarden(final String gardenName) {
    Garden toDeleteGarden = findGardenByGardenName(parseGardenName(gardenName));
    dynamoDBMapper.delete(toDeleteGarden);
  }
}