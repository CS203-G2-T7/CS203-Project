package com.G2T7.OurGardenStory.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Calendar;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.G2T7.OurGardenStory.utils.DateUtil;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

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

    public Window createWindow(final Window window) throws SchedulerException {
        window.setPK("Window");
        window.setWindowId("Win" + ++Window.numInstance);

        // Find if already exist in table. Throw error.
        Window findWindow = findWindowByPkSk(window.getPK(), window.getSK());
        if (findWindow != null && findWindow.getSK().equals(window.getSK())) {
            --Window.numInstance;
            throw new RuntimeException("Window with start date " + window.getSK() + " already exists.");
        }

        dynamoDBMapper.save(window);
        //scheduleAlgo(window.getWindowId());
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

    // public void scheduleAlgo(String winId) throws SchedulerException {
    //     // Window foundWindow = findWindowById(winId).get(0);
    //     // String startDate = foundWindow.getSK();

    //     // String winDuration = foundWindow.getWindowDuration();
    //     // Calendar endDate = Calendar.getInstance();
    //     // if (winDuration.contains("P")) {
    //     //     LocalDate endLocalDate = DateUtil.getWindowEndDateFromStartDateAndDuration(startDate, winDuration);
    //     //     endDate = DateBuilder.dateOf(0, 0, 0, endLocalDate.getDayOfMonth(), endLocalDate.getMonthValue(),
    //     //             endLocalDate.getYear());
    //     //     endDate.set(0, 0, 0, endLocalDate., 0, 0);
    //     // } else if (winDuration.contains("minute")) {
    //     //     int index = winDuration.indexOf("minute");
    //     //     int duration = Integer.parseInt(winDuration.substring(0, index));
    //     //     LocalTime time = LocalTime.now();
    //     //     time = time.plusMinutes(duration);
    //     //     int hour = time.getHour();
    //     //     int minute = time.getMinute();
    //     //     int seconds = time.getSecond();
    //     //     endDate = DateBuilder.dateOf(hour, minute, seconds);
    //     // }

    //     // SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    //     // Scheduler scheduler = schedulerFactory.getScheduler();
    //     // JobDetail job = newJob(BallotService.class)
    //     //         .withIdentity("doAlgo")
    //     //         .usingJobData("winId", winId)
    //     //         .build();

    //     // SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
    //     //         .withIdentity("doAlgo")
    //     //         .startAt(endDate)
    //     //         .forJob(job)
    //     //         .build();

    //     // scheduler.start();
    //     // scheduler.scheduleJob(job, trigger);

    //     Timer T = new Timer();
    //     TimerTask doAlgo = new TimerTask() {
    //         @Override
    //         public void run() {
    //             algorithmServiceImpl.doMagic(winId);
    //         }
    //     };
    //     Calendar date = Calendar.getInstance();
    //     LocalDateTime time = LocalDateTime.now();
    //     date.set(time.getYear(), time.getMonthValue(), time.getDayOfMonth(), time.getHour(), time.getMinute() + 3, time.getSecond());
    //     T.schedule(doAlgo, date.getTime());
    // }

}
