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
public class WinGardenService {
  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  // Relationship services
  public List<Relationship> findAllGardensInWindow(String windowId) {
    String capWinId = StringUtils.capitalize(windowId);
    if (!validateWinExist(capWinId)) {
      throw new ResourceNotFoundException("Window " + capWinId + " cannot be found");
    }
    Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
    eav.put(":WINID", new AttributeValue().withS(capWinId));
    DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
        .withKeyConditionExpression("PK = :WINID").withExpressionAttributeValues(eav);

    PaginatedQueryList<Relationship> foundRelationList = dynamoDBMapper.query(Relationship.class, qe);
    if (foundRelationList.isEmpty() || foundRelationList == null) {
      throw new ResourceNotFoundException("There are no gardens in " + capWinId + ".");
    }
    
    List<Relationship> result = new ArrayList<>();

        for (Relationship r : foundRelationList) {
            String sk = r.getSK();
            if (validateGardenExist(sk) == true) {
                result.add(r);
            }
        }
        return result;

  }

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

  public List<Relationship> addGardensInWindow(String windowId, JsonNode payload) {
    String capWinId = StringUtils.capitalize(windowId);
    if (!validateWinExist(capWinId)) {
      throw new ResourceNotFoundException("Window " + capWinId + " cannot be found");
    }

    List<Relationship> toAddRelationshipList = new ArrayList<Relationship>();
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

  /*
   * Validations. Doesn't throw, only return boolean.
   * 1. Validate window exist
   * 2. Validate garden exist
   * 3. Check relationship exist
   */

  // Check window exists
  public boolean validateWinExist(final String winId) {
    String capWinId = StringUtils.capitalize(winId);
    Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
    eav.put(":WID", new AttributeValue().withS(capWinId));
    DynamoDBQueryExpression<Window> qe = new DynamoDBQueryExpression<Window>().withIndexName("WindowId-index")
        .withConsistentRead(false)
        .withKeyConditionExpression("WindowId = :WID").withExpressionAttributeValues(eav);
    PaginatedQueryList<Window> foundWindowList = dynamoDBMapper.query(Window.class, qe);
    return foundWindowList != null && !foundWindowList.isEmpty() && foundWindowList.get(0) != null;
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
    if (!validateWinExist(winId)) {
      throw new ResourceNotFoundException("Window " + winId + " cannot be found");
    } else if (!validateGardenExist(gardenName)) {
      throw new ResourceNotFoundException("Garden " + gardenName + " cannot be found");
    }
    return;
  }

  // TODO: Validation for JSON payload. Check num ballots and leaseDuration valid.
  // private void validateRelationPayload(JsonNode payload);
}
