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
import java.util.*;

@Repository
public class WindowRepo {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private GardenRepo gardenRepo;

    public Window save(Window window) {
        window.setLeaseStart(findLeaseStart(window));
        List<Garden> gardenList = new ArrayList<>();
        gardenList.add(gardenRepo.getGardenByGardenName(window.getGardenName()));
        window.setGardenList(gardenList);
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

    public Window findWindowByStartDateTime(LocalDateTime startDateTime) {
        List<Window> windowList = listWindows();

        for (Window window : windowList) {
            if (window.getStartDateTime().equals(startDateTime)) {
                return window;
            }
        }

        return null;
    }

    public Window update(Window updateWindow) {
        Window window = findWindowByStartDateTime(updateWindow.getStartDateTime());
        List<Garden> gardenList = window.getGardenList();
        gardenList.add(gardenRepo.getGardenByGardenName(updateWindow.getGardenName()));
        window.setGardenList(gardenList);
        return window;
    }

    protected LocalDateTime findLeaseStart(Window window) {
        int duration = Character.getNumericValue(window.getDuration().charAt(0));
        return window.getStartDateTime().plusMonths(duration);
    }
}
