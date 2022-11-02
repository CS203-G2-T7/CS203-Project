package com.G2T7.OurGardenStory.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.GardenWin;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class RelationshipService {
  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  // Relationship services
  public List<Relationship> findAllGardensInWindow(String windowId) {
    String capWinId = StringUtils.capitalize(windowId);
    if (!validateWinExist(capWinId)) {
      throw new ResourceNotFoundException(capWinId + " does not exist.");
    }
    Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
    eav.put(":WINID", new AttributeValue().withS(capWinId));
    DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
        .withKeyConditionExpression("PK = :WINID").withExpressionAttributeValues(eav);

    PaginatedQueryList<Relationship> foundRelationList = dynamoDBMapper.query(Relationship.class, qe);
    if (foundRelationList.isEmpty() || foundRelationList == null) {
      throw new ResourceNotFoundException("There are no gardens in " + capWinId + ".");
    }
    return foundRelationList;
  }

  public Relationship findGardenInWindow(String windowId, String gardenName) {
    System.out.println("validation garden in window starts");
    String capWinId = StringUtils.capitalize(windowId);
    System.out.println("yup1");
    String formatGardenName = gardenName.replace("-", " ");
    System.out.println("yup2");
    validateAndThrowWinAndGardenExist(capWinId, formatGardenName);
    System.out.println("yup3");

    Relationship foundRelationship = dynamoDBMapper.load(Relationship.class, capWinId,
        formatGardenName);
    if (foundRelationship == null) {
      throw new ResourceNotFoundException(formatGardenName + " not found in " + capWinId + ".");
    }
    return foundRelationship;
  }

  public List<Relationship> addGardensInWindow(String windowId, JsonNode payload) {
    String capWinId = StringUtils.capitalize(windowId);
    if (!validateWinExist(capWinId)) {
      throw new ResourceNotFoundException(capWinId + " does not exist.");
    }

    List<Relationship> toAddRelationshipList = new ArrayList<Relationship>();
    payload.forEach(relation -> {
      validateWinGardenRelation(capWinId, relation.get("gardenName").asText()); // throws if invalid
      Relationship gardenWin = new GardenWin(capWinId,
          relation.get("gardenName").asText(), relation.get("leaseDuration").asText(),
          relation.get("numPlotsForBalloting").asInt());
      toAddRelationshipList.add(gardenWin);
    });
    dynamoDBMapper.batchSave(toAddRelationshipList);
    return toAddRelationshipList;
  }

  public Relationship updateGardenInWindow(String windowId, String gardenName, JsonNode payload) throws Exception {
    String capWinId = StringUtils.capitalize(windowId);
    String formatGardenName = gardenName.replace("-", " ");
    Relationship toUpdateGarden = findGardenInWindow(windowId, formatGardenName);

    toUpdateGarden = new GardenWin(capWinId,
        formatGardenName, payload.get("leaseDuration").asText(),
        payload.get("numPlotsForBalloting").asInt());
    dynamoDBMapper.save(toUpdateGarden);
    return toUpdateGarden;
  }

  public void deleteGardenInWindow(String windowId, String gardenName) {
    Relationship gardenWinToDelete = findGardenInWindow(windowId, gardenName);
    dynamoDBMapper.delete(gardenWinToDelete);
  }

  public void deleteAllGardensInWindow(String windowId) {
    List<Relationship> allGardensToDelete = findAllGardensInWindow(windowId);
    dynamoDBMapper.batchDelete(allGardensToDelete);
  }

  public boolean validateWinExist(final String winId) {
    // Check window exists
    String capWinId = StringUtils.capitalize(winId);
    Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
    eav.put(":WID", new AttributeValue().withS(capWinId));
    DynamoDBQueryExpression<Window> qe = new DynamoDBQueryExpression<Window>().withIndexName("WindowId-index")
        .withConsistentRead(false)
        .withKeyConditionExpression("WindowId = :WID").withExpressionAttributeValues(eav);
    PaginatedQueryList<Window> foundWindowList = dynamoDBMapper.query(Window.class, qe);
    return foundWindowList != null && !foundWindowList.isEmpty() && foundWindowList.get(0) != null;
  }

  private boolean validateGardenExist(String gardenName) {
    Garden foundGarden = dynamoDBMapper.load(Garden.class, Garden.EntityName, gardenName);
    return foundGarden != null;
  }

  private void validateAndThrowWinAndGardenExist(String winId, String gardenName) {
    if (!validateWinExist(winId)) {
      throw new ResourceNotFoundException(winId + " does not exist.");
    } else if (!validateGardenExist(gardenName)) {
      throw new ResourceNotFoundException(gardenName + " does not exist");
    }
    // No errors
    return;
  }

  private void validateWinGardenRelation(String winId, String gardenName) {
    // Check relationship exists
    Relationship foundRelationship = dynamoDBMapper.load(Relationship.class, winId,
        gardenName);
    if (!validateGardenExist(gardenName)) {
      throw new ResourceNotFoundException("Garden " + gardenName + " cannot be found");
    } else if (foundRelationship != null) {
      throw new IllegalArgumentException(winId + " already has " + gardenName);
    }
  }

  // TODO: Validation for JSON payload. Check num ballots and leaseDuration valid.
  // private void validateRelationPayload(JsonNode payload);
}
