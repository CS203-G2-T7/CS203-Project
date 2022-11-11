package com.G2T7.OurGardenStory.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.G2T7.OurGardenStory.model.*;
import com.G2T7.OurGardenStory.model.RelationshipModel.*;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class WinGardenService {

  private final DynamoDBMapper dynamoDBMapper;

  public WinGardenService(DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  /**
    * Get all Gardens that belong to a Window corresponding to the given windowId
    * If there is no Window corresponding to the given windowId, or if there are no Gardens 
    * posted to this Window, throw ResourceNotFoundException
    *
    * @param windowId a String
    * @return a list of all GardenWin Relationship objects that belong to the Window
    */
  public List<Relationship> findAllGardensInWindow(String windowId) {
    String capWinId = StringUtils.capitalize(windowId);
    System.out.println("capwinid: " + capWinId);
    if (validateWinExist(capWinId)) {
      throw new ResourceNotFoundException("Window " + capWinId + " cannot be found");
    }
    Map<String, AttributeValue> eav = new HashMap<>();
    eav.put(":WINID", new AttributeValue().withS(capWinId));
    DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
        .withKeyConditionExpression("PK = :WINID").withExpressionAttributeValues(eav);

    PaginatedQueryList<Relationship> foundRelationList = dynamoDBMapper.query(Relationship.class, qe);
    if (foundRelationList.isEmpty()) {
      throw new ResourceNotFoundException("There are no gardens in " + capWinId + ".");
    }
    
    List<Relationship> result = new ArrayList<>();

        for (Relationship r : foundRelationList) {
            String sk = r.getSK();
            if (validateGardenExist(sk)) {
                result.add(r);
            }
        }
        return result;
  }

  /**
    * Get a specific Garden in a Window
    * If there is no Window corresponding to the given windowId, or if there is no Garden
    * corresponding to the gardenName in this Window, throw ResourceNotFoundException
    *
    * @param windowId a String
    * @param gardenName a String
    * @return the GardenWin RelationSHip object corresponding to the windowId and gardenName
    */
  public Relationship findGardenInWindow(String windowId, String gardenName) {
    String capWinId = StringUtils.capitalize(windowId);
    String formatGardenName = gardenName.replace("-", " ");
    validateAndThrowWinAndGardenExist(capWinId, formatGardenName);

    Relationship foundRelationship = dynamoDBMapper.load(Relationship.class, capWinId,
        formatGardenName);
    if (foundRelationship == null) {
      throw new ResourceNotFoundException(formatGardenName + " not found in " + capWinId + ".");
    }
    return foundRelationship;
  }

  /**
    * Add an array of Gardens into the Window corresponding to the given windowId
    * If there is no Window object corresponding to the given windowId, throw ResourceNotFoundException
    *
    * @param windowId a String
    * @param payload containing an array of gardenNames
    * @return the added GardenWin Relationship objects
    */
  public List<Relationship> addGardensInWindow(String windowId, JsonNode payload) {
    String capWinId = StringUtils.capitalize(windowId);
    if (validateWinExist(capWinId)) {
      throw new ResourceNotFoundException("Window " + capWinId + " cannot be found");
    }

    List<Relationship> toAddRelationshipList = new ArrayList<>();
    payload.forEach(relation -> {
      validateWinGardenRelation(capWinId, relation.get("gardenName").asText());
      Relationship gardenWin = new GardenWin(capWinId,
          relation.get("gardenName").asText(), relation.get("leaseDuration").asText(),
          relation.get("numPlotsForBalloting").asInt());
      toAddRelationshipList.add(gardenWin);
    });
    dynamoDBMapper.batchSave(toAddRelationshipList);
    return toAddRelationshipList;
  }

  /**
    * Update the leaseDuration and numPlotsForBalloting for a specific GardenWin Relationship object
    * If there is no GardenWin Relationship object corresponding to the windowId and gardenName, throw ResourceNotFoundException
    *
    * @param windowId a String
    * @param gardenName a String
    * @param payload includes a String leaseDuration and an int numPlotsForBalloting
    * @return the Updated GardenWin Relationship object, if update was successful
    */
  public Relationship updateGardenInWindow(String windowId, String gardenName, JsonNode payload) {
    String capWinId = StringUtils.capitalize(windowId);
    String formatGardenName = gardenName.replace("-", " ");
    Relationship toUpdateGarden;

    toUpdateGarden = new GardenWin(capWinId,
        formatGardenName, payload.get("leaseDuration").asText(),
        payload.get("numPlotsForBalloting").asInt());
    dynamoDBMapper.save(toUpdateGarden);
    return toUpdateGarden;
  }

  /**
    * Delete a specific Garden in the Window corresponding to given windowId
    * If there is no GardenWin Relationship object corresponding to the windowId and gardenName, throw ResourceNotFoundException
    *
    * @param windowId a String
    * @param gardenName a String
    */
  public void deleteGardenInWindow(String windowId, String gardenName) {
    Relationship gardenWinToDelete = findGardenInWindow(windowId, gardenName);
    dynamoDBMapper.delete(gardenWinToDelete);
  }

  /**
    * Delete all Gardens in the Window corresponding to given windowId
    * If there is no GardenWin Relationship object corresponding to the windowId and gardenName, throw ResourceNotFoundException
    *
    * @param windowId a String
    */
  public void deleteAllGardensInWindow(String windowId) {
    List<Relationship> allGardensToDelete = findAllGardensInWindow(windowId);
    dynamoDBMapper.batchDelete(allGardensToDelete);
  }

  /**
   * Validations. Doesn't throw, only return boolean.
   * 1. Validate window exist
   * 2. Validate garden exist
   * 3. Check relationship exist
   */
  public boolean validateWinExist(final String winId) {
    String capWinId = StringUtils.capitalize(winId);
    Map<String, AttributeValue> eav = new HashMap<>();
    eav.put(":WID", new AttributeValue().withS(capWinId));
    DynamoDBQueryExpression<Window> qe = new DynamoDBQueryExpression<Window>().withIndexName("WindowId-index")
        .withConsistentRead(false)
        .withKeyConditionExpression("WindowId = :WID").withExpressionAttributeValues(eav);
    PaginatedQueryList<Window> foundWindowList = dynamoDBMapper.query(Window.class, qe);
    return foundWindowList == null || foundWindowList.isEmpty() || foundWindowList.get(0) == null;
  }

  // Check garden exists
  public boolean validateGardenExist(String gardenName) {
    Garden foundGarden = dynamoDBMapper.load(Garden.class, Garden.EntityName, gardenName);
    return foundGarden != null;
  }

  // Check relationship exists //For add gardens in window
  private void validateWinGardenRelation(String winId, String gardenName) {
    Relationship foundRelationship = dynamoDBMapper.load(Relationship.class, winId,
        gardenName);
    if (!validateGardenExist(gardenName)) {
      throw new ResourceNotFoundException("Garden " + gardenName + " cannot be found");
    } else if (foundRelationship != null) {
      throw new IllegalArgumentException(winId + " already has " + gardenName);
    }
  }

  private void validateAndThrowWinAndGardenExist(String winId, String gardenName) {
    if (validateWinExist(winId)) {
      throw new ResourceNotFoundException("Window " + winId + " cannot be found");
    } else if (!validateGardenExist(gardenName)) {
      throw new ResourceNotFoundException("Garden " + gardenName + " cannot be found");
    }
  }
}
