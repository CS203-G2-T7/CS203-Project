package com.G2T7.OurGardenStory.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.G2T7.OurGardenStory.geocoder.AlgorithmService;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@Service
public class WindowService implements Job{
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private BallotService ballotService;
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private UserService userService;
    @Autowired
    private AlgorithmService algorithmService;
    @Autowired
    private MailService mailService;

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
            throw new RuntimeException("Window already exists.");
            // Can make custom exceptions here, Then catch and throw 400 bad req
        }

        dynamoDBMapper.save(window);
        scheduleAlgo(window.getWindowId());
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

    public void scheduleAlgo(String winId) throws SchedulerException {
        Window win = findWindowById(winId).get(0);
        String startDate = win.getSK();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate date = LocalDate.parse(startDate, formatter);
        
        String winDuration = win.getWindowDuration();
        Date d = null;
        if (winDuration.contains("M")) {
            int index = winDuration.indexOf("M");
            int duration = Integer.parseInt(winDuration.substring(0, index));
            date = date.plusMonths(duration);
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear();
            d = DateBuilder.dateOf(0, 0, 0, day, month,year);
        } else if (winDuration.contains("minute")) {
            int index = winDuration.indexOf("minute");
            int duration = Integer.parseInt(winDuration.substring(0, index));
            LocalTime time = LocalTime.now();
            time = time.plusMinutes(duration);
            int hour = time.getHour();
            int minute = time.getMinute();
            int seconds = time.getSecond();
            d = DateBuilder.dateOf(hour, minute, seconds);
        }
        
        


        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();  
        JobDetail job = newJob(WindowService.class)
                            .withIdentity("doAlgo")
                            .usingJobData("winId", winId)
                            .build();
        
        
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                            .withIdentity("doAlgo")
                            .startAt(d)
                            .forJob(job)
                            .build();
        
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();

            String winId = dataMap.getString("winId");
            List<Relationship> relationships = relationshipService.findAllGardensInWindow(winId);
            List<String> gardens = new ArrayList<>();
    
            for (Relationship r : relationships) {
                gardens.add(r.getSK());
            }
    
            for (String gardenName : gardens) {
                List<Relationship> ballots = ballotService.findAllBallotsInWindowGarden(winId, gardenName);
                HashMap<String, Double> usernameDistance = new HashMap<>();
                for (Relationship ballot : ballots) {
                    String username = ballot.getSK();
                    Double distance = ballot.getDistance();
                    usernameDistance.put(username, distance);
                }
                Relationship r = relationshipService.findGardenInWindow(winId, gardenName);
                int numPlotsAvailable = r.getNumPlotsForBalloting();
                ArrayList<String> ballotSuccesses= algorithmService.getBallotSuccess(usernameDistance, numPlotsAvailable);
                for (Relationship ballot : ballots) {
                    if (ballotSuccesses.contains(ballot.getSK())) {
                        ballot.setBallotStatus("Success");
                        String email = userService.findUserByUsername(ballot.getSK()).getEmail();
                        mailService.sendTextEmail(email, "Success"); //this throws IOException
                    } else {
                        ballot.setBallotStatus("Fail");
                        String email = userService.findUserByUsername(ballot.getSK()).getEmail();
                        mailService.sendTextEmail(email, "Fail"); //this throws IOException
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}

