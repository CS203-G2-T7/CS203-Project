package com.G2T7.OurGardenStory.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@Service
public class WindowService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
    * Get a Window object based on the PK and SK in the database
    *
    * @param pk which is the partition key in database
    * @param sk which is the sort key in database
    * @return the Window object corresponding to the pk and sk
    */
    private Window findWindowByPkSk(final String pk, final String sk) {
        return dynamoDBMapper.load(Window.class, pk, sk);
    }

    /**
    * Get the Window object corresponding to the given windowId
    * If no Window object is returned, throw ResourceNotFoundException
    *
    * @param windowId
    * @return a list containing only the Window object corresponding to the given windowId
    */
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

    /**
    * Get all Window objects in database
    * If there are no Windows in database, throw ResourceNotFoundException
    *
    * @return a list of all Window objects in database
    */
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


    /**
    * Saves a Window object into the database
    * If a Window with the same startDate already exists, throw Exception
    *
    * @param window
    * @return the newly created Window object
    */
    public Window createWindow(final Window window) {
        window.setPK("Window");
        window.setWindowId("Win" + ++Window.numInstance);

        // Find if already exist in table. Throw error.
        Window findWindow = findWindowByPkSk(window.getPK(), window.getSK());
        if (findWindow != null && findWindow.getSK().equals(window.getSK())) {
            --Window.numInstance;
            throw new RuntimeException("Window with start date " + window.getSK() + " already exists.");
        }

        dynamoDBMapper.save(window);
        return window;
    }

    /**
    * Update the windowDuration of an existing Window
    * If the windowId does not correspond to an already existing Window, throw ResourceNotFoundException
    *
    * @param windowDuration to be updated
    * @param windowId
    * @return the updated Window object, if update was successful
    */
    public Window putWindow(final String windowDuration, final String windowId) {
        Window window = findWindowById(windowId).get(0);
        window.setWindowDuration(windowDuration);
        dynamoDBMapper.save(window);
        return window;
    }

    /**
    * Delete an already existing Window, corresponding to the given windowId
    * If the windowId does not correspond t an already existing Window, throw ResourceNotFoundException
    *
    * @param windowId
    * @return no content
    */
    public void deleteWindow(final String windowId) {
        Window toDeleteWindow = findWindowById(windowId).get(0);
        dynamoDBMapper.delete(toDeleteWindow);
    }
}
