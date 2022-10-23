package com.G2T7.OurGardenStory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;

@Service
public class WindowService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    private Window findWindowByPkSk(final String pk, final String sk) {
        return dynamoDBMapper.load(Window.class, pk, sk);
    }

    // TODO Query by windowId. Need to use GSI.

    public Window createWindow(final Window window) {
        window.setPK("Window");
        window.setWindowId("Win" + ++Window.numInstance);
        // System.out.println("Save window: " + window);

        // Find if already exist in table. Throw error.
        Window findWindow = findWindowByPkSk(window.getPK(), window.getSK());
        // System.out.println("Find window: " + findWindow);
        if (findWindow != null && findWindow.getSK().equals(window.getSK())) {
            --Window.numInstance;
            throw new RuntimeException("Window already exists."); // can make custom exceptions here, Then catch and
                                                                  // throw appropriate status code. Error 400 bad
                                                                  // request
        }

        dynamoDBMapper.save(window);
        return window;
    }

    public Window putWindow(final Window window) {
        window.setPK("Window");

        // Find if doesn't exist in table. Throw error
        Window findWindow = findWindowByPkSk(window.getPK(), window.getSK());
        if (findWindow == null) {
            throw new RuntimeException("Window not found.");
        }

        window.setWindowId(findWindow.getWindowId());
        dynamoDBMapper.save(window);
        return window;
    }

    public void deleteWindowByPkSk(final String pk, final String sk) {
        // check if window exists.
        Window toDeleteWindow = findWindowByPkSk(pk, sk);
        if (toDeleteWindow == null) {
            throw new RuntimeException("Window not found.");
        }
        dynamoDBMapper.delete(toDeleteWindow);
    }
}
