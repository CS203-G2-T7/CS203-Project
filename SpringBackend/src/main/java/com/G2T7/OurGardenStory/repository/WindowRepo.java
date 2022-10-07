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
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.Update;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

@Repository
public class WindowRepo {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private BallotRepo ballotRepo;

    public Window save(Window window) {
        window.setLeaseStart(findLeaseStart(window));
        dynamoDBMapper.save(window);
        return window;
    }

    public List<Window> listWindows() {
        List<Window> windowList = dynamoDBMapper.scan(Window.class, new DynamoDBScanExpression());
        return windowList;
    }

    public Window findLatestWindow() {
        List<Window> windowList = listWindows();
        TreeMap<LocalDateTime, Window> windowMap = new TreeMap<>();

        for (Window window : windowList) {
            windowMap.put(window.getStartDateTime(), window);
        }

        return windowMap.lastEntry().getValue();
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

    public void callAlgo(Garden garden, HashMap<String, Double> ballotters) {
        TimerTask task = new TimerTask() {
            public void run() {
                Algorithm algo = new Algorithm();
                ArrayList<String> output = new ArrayList<>();
                output = algo.getBallotSuccess(ballotters, garden.getNumPlots());
                
                // AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
                // ScanRequest scanRequest = new ScanRequest()
                //                             .withTableName("Ballot")
                //                             .withProjectionExpression("username");
                // ScanResult result = client.scan(scanRequest);
                // Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
                // eav.put(":val1", new AttributeValue().withS(String.valueOf(findLatestWindow().getStartDateTime())));
                // DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("startDateTime = :val1").withExpressionAttributeValues(eav);
                List<Ballot> ballotListForWindow = ballotRepo.listBallotsFromLatestWindow();
                List<Ballot> ballotListForWindowForGarden = new ArrayList<>();
                for (Ballot ballot : ballotListForWindow) {
                    try {
                        if (ballot.getGarden().equals(garden)) {
                            ballotListForWindowForGarden.add(ballot);
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                for (Ballot ballot : ballotListForWindow) {
                    for (String success : output) {
                        if (success.equals(ballot.getUsername())) {
                            UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                                                                    .withTableName("Ballot")
                                                                    .addAttributeUpdatesEntry("username", new AttributeValueUpdate().withValue(new AttributeValue().withS("SUCCESS")));
                            UpdateItemResult updateItemResult = new AmazonDynamoDB().updateItem(updateItemRequest);
                            break;
                        } 
                    }
                    UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                                                                    .withTableName("Ballot")
                                                                    .addAttributeUpdatesEntry("username", new AttributeValueUpdate().withValue(new AttributeValue().withS("FAIL")));
                    UpdateItemResult updateItemResult = client.updateItem(updateItemRequest);
                }
            }
        };
        LocalDateTime doAlgo = findLatestWindow().getLeaseStart();
        Date date = Date.from(doAlgo.atZone(ZoneId.systemDefault()).toInstant());
        new Timer().schedule(task, date);

}

protected LocalDateTime findLeaseStart(Window window) {
    int duration = Character.getNumericValue(window.getDuration().charAt(0));
    return window.getStartDateTime().plusMonths(duration);
}

}
