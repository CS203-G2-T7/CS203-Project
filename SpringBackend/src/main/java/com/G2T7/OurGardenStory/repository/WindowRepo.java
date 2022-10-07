package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.geocoder.Algorithm;
import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.Update;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class WindowRepo {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Window save(Window window) {
        dynamoDBMapper.save(window);
        return window;
    }

    public List<Window> listWindows() {
        List<Window> windowList = dynamoDBMapper.scan(Window.class, new DynamoDBScanExpression());
        return windowList;
    }

    public Window findLatestWindow() {
        List<Window> windowList = listWindows();
        int latestWindowNum = 0;
        Window returnWindow = null;
        for (Window window : windowList) {
            int windowNum = window.getWindowNum();

            if (windowNum > latestWindowNum) {
                latestWindowNum = windowNum;
                returnWindow = window;
            }
        }

        return returnWindow;
    }

    public Window update(Window updateWindow) {
        List<Window> windowList = listWindows(); // very inefficient way to update :(
        for (Window window : windowList) {
            if (window.getWindowNum() == updateWindow.getWindowNum()) {
                Set<String> updateGardenSet = updateWindow.getGardenSet();
                Set<String> gardenSet = window.getGardenSet();

                for (String garden : updateGardenSet) {
                    gardenSet.add(garden);
                    window.setGardenSet(gardenSet);
                }

                dynamoDBMapper.save(window);
                return window;
            }
        }

        return null;
    }

    public void callAlgo() {
        TimerTask task = new TimerTask() {
            public void run() {
                Algorithm algo = new Algorithm();
                ArrayList<String> output = new ArrayList<>();
                output = algo.getBallotSuccess(null, 0);
                
                AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
                ScanRequest scanRequest = new ScanRequest()
                                            .withTableName("Ballot")
                                            .withProjectionExpression("username");
                ScanResult result = client.scan(scanRequest);
                for (Map<String, AttributeValue> username : result.getItems()) {
                    for (String success : output) {
                        if (success.equals(username)) {
                            UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                                                                    .withTableName("Ballot")
                                                                    .addAttributeUpdatesEntry(username, "SUCCESS");
                        }
                    }
                }
            }
            LocalDateTime doAlgo = 
            new Timer().schedule(task, doAlgo);
        };
    }
}
