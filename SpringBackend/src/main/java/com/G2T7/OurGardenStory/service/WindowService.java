package com.G2T7.OurGardenStory.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class WindowService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    private Window findWindowByPkSk(final String pk, final String sk) {
        return dynamoDBMapper.load(Window.class, pk, sk);
    }

    public List<Window> findWindowById(final String windowId) { // queries must always return a paginated list
        String capWinId = StringUtils.capitalize(windowId);

        // Build query expression to Query GSI by windowID
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":WID", new AttributeValue().withS(capWinId));
        DynamoDBQueryExpression<Window> qe = new DynamoDBQueryExpression<Window>().withIndexName("WindowId-index")
                .withConsistentRead(false)
                .withKeyConditionExpression("WindowId = :WID").withExpressionAttributeValues(eav);

        PaginatedQueryList<Window> foundWindowList = dynamoDBMapper.query(Window.class, qe);
        // Check if not found. Should only return a single value.
        if (foundWindowList.size() == 0) {
            throw new ResourceNotFoundException("Window not found"); // might not be right exception
        }
        return foundWindowList;
    }

    public List<Window> findAllWindows() {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":WIN", new AttributeValue().withS("Window"));
        DynamoDBQueryExpression<Window> qe = new DynamoDBQueryExpression<Window>()
                .withKeyConditionExpression("PK = :WIN").withExpressionAttributeValues(eav);

        PaginatedQueryList<Window> foundWindowList = dynamoDBMapper.query(Window.class, qe);
        if (foundWindowList.size() == 0) {
            throw new ResourceNotFoundException("There are no windows.");
        }
        return foundWindowList;
    }

    public Window createWindow(final Window window) {
        window.setPK("Window");
        window.setWindowId("Win" + ++Window.numInstance);

        // Find if already exist in table. Throw error.
        Window findWindow = findWindowByPkSk(window.getPK(), window.getSK());
        if (findWindow != null && findWindow.getSK().equals(window.getSK())) {
            --Window.numInstance;
            throw new RuntimeException("Window already exists.");
            // Can make custom exceptions here, Then catch and throw 400 bad req
        }
        dynamoDBMapper.save(window);
        return window;
    }

    public Window putWindow(final String windowDuration, final String windowId) {
        Window window = findWindowById(windowId).get(0);
        window.setWindowDuration(windowDuration);
        dynamoDBMapper.save(window);
        return window;
    }

    public void deleteWindow(final String windowId) {
        Window toDeleteWindow = findWindowById(windowId).get(0);
        dynamoDBMapper.delete(toDeleteWindow);
    }

    // Relationship services
    public List<Relationship> findGardensInWindow(String windowId) {
        String capWinId = StringUtils.capitalize(windowId);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":WINID", new AttributeValue().withS(capWinId));
        DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
                .withKeyConditionExpression("PK = :WINID").withExpressionAttributeValues(eav);

        PaginatedQueryList<Relationship> foundRelationList = dynamoDBMapper.query(Relationship.class, qe);
        if (foundRelationList.size() == 0) {
            throw new ResourceNotFoundException("There are no windows.");
        }
        return foundRelationList;
    }

    public List<Relationship> addGardensInWindow(String windowId, JsonNode payload) {
        String capWinId = StringUtils.capitalize(windowId);
        findWindowById(capWinId).get(0); // validate window exists
        List<Relationship> toAddRelationshipList = new ArrayList<Relationship>();

        payload.forEach(relation -> {
            //need check if relation already exists.
            Garden foundGarden = dynamoDBMapper.load(Garden.class, "Garden", relation.get("gardenName").asText());
            if (foundGarden == null) {
                throw new IllegalArgumentException("Garden cannot be found");
            }

            Relationship newRelation = Relationship.createWindowGardenRelation(capWinId,
                    relation.get("gardenName").asText(), relation.get("leaseDuration").asText(),
                    relation.get("numPlotsForBalloting").asInt());
            toAddRelationshipList.add(newRelation);
        });
        dynamoDBMapper.batchSave(toAddRelationshipList);

        return toAddRelationshipList;
    }
}
