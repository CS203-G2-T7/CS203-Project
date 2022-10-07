package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
