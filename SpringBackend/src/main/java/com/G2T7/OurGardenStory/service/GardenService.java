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

  // public Garden createGarden(final Garden garden) {
  //   try{
  //     System.out.println(garden);
  //     dynamoDBMapper.save(garden);
  //     return garden;    
  //   }catch(Exception e) {
  //     System.out.println(e);
  //   }
  //   return null;
  // }

    private Garden findGardenByPkSk(final String pk, final String sk) {
        return dynamoDBMapper.load(Garden.class, pk, sk);
    }

    public List<Garden> findAllGardens() {
      Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":GDN", new AttributeValue().withS("Garden"));
        DynamoDBQueryExpression<Garden> qe = new DynamoDBQueryExpression<Garden>()
                .withKeyConditionExpression("PK = :GDN").withExpressionAttributeValues(eav);

        PaginatedQueryList<Garden> foundGardenList = dynamoDBMapper.query(Garden.class, qe);
        if (foundGardenList.size() == 0) {
            throw new ResourceNotFoundException("There are no gardens.");
        }
        return foundGardenList;
    }

    public Garden createGarden(final Garden garden) {
      garden.setPK("Garden");
      garden.setSK(garden.getGardenAddress());

      //Find if already exist in table. Throw error.
      Garden findGarden = findGardenByPkSk(garden.getPK(), garden.getSK());
      if (findGarden != null && findGarden.getSK().equals(garden.getSK())) {
          throw new RuntimeException("Garden already exists.");

          /*
          * TODO:
          * Can make custom exceptions here, Then catch and throw 400 bad req
          */
      }
      dynamoDBMapper.save(garden);
      return garden;
    }

    public List<Garden> findGardenByGardenAddress(final String gardenAddress) { //queries must always return a paginiated list
  
      // Build query expression to Query by GardenAddress
      Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
      eav.put(":GDN", new AttributeValue().withS("Garden"));
      eav.put(":GDNADDR", new AttributeValue().withS(gardenAddress));
    
      DynamoDBQueryExpression<Garden> qe = new DynamoDBQueryExpression<Garden>()
              .withKeyConditionExpression("PK = :GDN and SK = :GDNADDR ")
              .withExpressionAttributeValues(eav);

      PaginatedQueryList<Garden> foundGardenList = dynamoDBMapper.query(Garden.class, qe);
      // Check if not found. Should only return a single value.
      if (foundGardenList.size() == 0) {
          throw new ResourceNotFoundException("Garden not found"); // might not be right exception
      }
      return foundGardenList;
    }

    public Garden putGarden(final String gardenAddress, final int numPlots) {
        Garden garden = findGardenByGardenAddress(gardenAddress).get(0);
        garden.setNumPlots(numPlots);
        dynamoDBMapper.save(garden);
        return garden;
    }

    public void deleteGarden(final String gardenAddress) {
      Garden toDeleteGarden = findGardenByGardenAddress(gardenAddress).get(0);
      dynamoDBMapper.delete(toDeleteGarden);
    }
}