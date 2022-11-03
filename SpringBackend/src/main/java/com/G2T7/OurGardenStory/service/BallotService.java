package com.G2T7.OurGardenStory.service;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.controller.GeocodeService;
import com.G2T7.OurGardenStory.exception.CustomException;
import com.G2T7.OurGardenStory.geocoder.AlgorithmService;
import com.G2T7.OurGardenStory.geocoder.AlgorithmServiceImpl;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Ballot;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.xspec.L;

import java.util.*;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;

import ch.qos.logback.core.net.SyslogOutputStream;

@Service
public class BallotService implements Job {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;

    @Autowired
    private GeocodeService geocodeService;

    @Autowired
    private WindowService windowService;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private AlgorithmServiceImpl algorithmService;

    @Autowired
    private MailService mailService;

    public List<Relationship> findAllBallotsInWindowGarden(String windowId, String gardenName) {
        String capWinId = StringUtils.capitalize(windowId);
        if (!relationshipService.validateWinExist(capWinId)) {
          throw new ResourceNotFoundException(capWinId + " does not exist.");
        }
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":WINID", new AttributeValue().withS(capWinId));
        DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
            .withKeyConditionExpression("PK = :WINID")
            .withExpressionAttributeValues(eav)
            .withIndexName(capWinId + "|" + gardenName);
    
        PaginatedQueryList<Relationship> foundRelationList = dynamoDBMapper.query(Relationship.class, qe);
        if (foundRelationList.isEmpty() || foundRelationList == null) {
          throw new ResourceNotFoundException("There are no ballots in " + capWinId + ".");
        }
        return foundRelationList;
      }

    public Relationship findUserBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, capWinId, username);
        // if (foundBallot == null) {
        //     throw new ResourceNotFoundException("Ballot does not exist.");
        // }
        return foundBallot;
    }

    public Relationship addBallotInWindow(String windowId, String username, JsonNode payload) {
        String capWinId = StringUtils.capitalize(windowId);

        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        Garden garden = gardenService.findGardenByGardenName(payload.get("gardenName").asText());
        String latitude = garden.getLatitude();
        String longitude = garden.getLongitude();

        User user = userService.findUserByUsername(username);
        String userAddress = user.getAddress();

        double distance = geocodeService.saveDistance(username, userAddress, longitude, latitude);

        LocalDate date = LocalDate.now();
        String currentDate = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        // validations
        // validateBallotPostDate(windowId, date);
        // validateGardenInWindow(windowId, payload.get("gardenName").asText());
        // validateUserHasBallotedBeforeInSameWindow(windowId, username);

        //String winId, String username, String gardenName, String ballotId, String ballotDateTime,
        //double distance, String ballotStatus
        Relationship ballot = new Ballot(capWinId, username, garden.getSK(),
                "Ballot" + String.valueOf(++Ballot.numInstance), currentDate, distance,
                "PENDING");

        dynamoDBMapper.save(ballot);
        System.out.println("Ballot is successfully posted");
        return ballot;
    }

    public Relationship updateGardenInBallot(String windowId, String username, JsonNode payload) {
        String capWinId = StringUtils.capitalize(windowId);
        Relationship toUpdateBallot = findUserBallotInWindow(capWinId, username);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(formatter);

        Garden garden = gardenService.findGardenByGardenName(payload.get("gardenName").asText());
        String latitude = garden.getLatitude();
        String longitude = garden.getLongitude();
        User user = userService.findUserByUsername(username);
        String userAddress = user.getAddress();
        double distance = geocodeService.saveDistance(username, userAddress, longitude, latitude);

        // Relationship ballot = new Ballot(capWinId, username, capWinId + "|" + garden.getSK(), toUpdateBallot.getBallotId(), currentDate, distance,
        //                         Relationship.BallotStatus.PENDING);
        Relationship ballot = new Ballot(capWinId, username, garden.getSK(), toUpdateBallot.getBallotId(), currentDate, distance,
                                        "PENDING");

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    public void deleteBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);

        Relationship ballotToDelete = findUserBallotInWindow(capWinId, username);
        dynamoDBMapper.delete(ballotToDelete);
    }

    public String getUsername() {
        String idToken = "";
        String[] chunks = idToken.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decodedPayload = new String(decoder.decode(chunks[1]));
        String[] payload_attr = decodedPayload.split(",");

        for (String payload : payload_attr) {
            if (payload.contains("username")) {
                return payload.substring(payload.indexOf(":\"") + 2, payload.length() - 1); // username is returned
            }
        }
        return null;
    }

    public void validateUser(String username) {
        System.out.println("Validating user exists");
        User user = userService.findUserByUsername(username);
        System.out.println("validated user exists.");
        String DOB = user.getDOB();
        System.out.println("Generate dob" + DOB);
        LocalDate birthdate = LocalDate.of(Integer.parseInt(DOB.substring(6)),
                Integer.parseInt(DOB.substring(3, 5)), Integer.parseInt(DOB.substring(0, 2)));

        birthdate = birthdate.minusYears(18);
        if (birthdate.getYear() < 0) {
            System.out.println("Validating user age");
            throw new CustomException("User must be 18 to ballot");
        }
    }

    public void validateBallotPostDate(String windowId, LocalDate date) {
        Window win = windowService.findWindowById(windowId).get(0);
        String startDate = win.getSK();
        LocalDate winStartDate = convertStringToLocalDate(startDate);

        String winDuration = win.getWindowDuration();
        
        int winDurationMonth = Integer.parseInt(winDuration.substring(0, winDuration.indexOf('M')));
        LocalDate winEndDate = winStartDate.plusMonths(winDurationMonth); 

        if (date.isBefore(winStartDate) || date.isAfter(winEndDate)) {
            throw new IllegalArgumentException("Ballot is submitted outside of window balloting period");
        } 
    }

    public void validateGardenInWindow(String windowId, String gardenName) {
        relationshipService.findGardenInWindow(windowId, gardenName);
    }

    public void validateUserHasBallotedBeforeInSameWindow(String windowId, String username) {
        Relationship r = findUserBallotInWindow(windowId, username);
        if (r != null) {
            throw new IllegalArgumentException("User has already balloted in the same window");
        }
    }

    public LocalDate convertStringToLocalDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));

        LocalDate convertedDate = LocalDate.of(year, month, day);
        return convertedDate;
    }

    // public void validateIfGardenFull(Garden garden, String winId) {
    //     // TODO
    //     List<Relationship> ballots = findAllBallotsInWindowForGarden(winId, garden.getSK());
    //     int numBallots = ballots.size();

    //     Relationship gardenWin = dynamoDBMapper.load(Relationship.class, winId, garden.getSK());

    //     int numPlotsForBalloting = gardenWin.getNumPlotsForBalloting();

    //     if (numBallots >= numPlotsForBalloting) {
    //         throw new CustomException("Plots are full");
    //     }
    //     return;
    // }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        // try {
        //     JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        //     String winId = dataMap.getString("winId");
        //     String capWinId = StringUtils.capitalize(winId);
        //     List<Relationship> relationships = relationshipService.findAllGardensInWindow(capWinId);
        //     List<String> gardens = new ArrayList<>();
    
        //     for (Relationship r : relationships) {
        //         gardens.add(r.getSK());
        //     }
    
        //     for (String gardenName : gardens) {
        //         List<Relationship> ballots = findAllBallotsInWindowGarden(winId, gardenName);
        //         HashMap<String, Double> usernameDistance = new HashMap<>();
        //         for (Relationship ballot : ballots) {
        //             String username = ballot.getSK();
        //             Double distance = ballot.getDistance();
        //             usernameDistance.put(username, distance);
        //         }
        //         Relationship r = relationshipService.findGardenInWindow(winId, gardenName);
        //         int numPlotsAvailable = r.getNumPlotsForBalloting();
        //         ArrayList<String> ballotSuccesses= algorithmService.getBallotSuccess(usernameDistance, numPlotsAvailable);
        //         for (Relationship ballot : ballots) {
        //             if (ballotSuccesses.contains(ballot.getSK())) {
        //                 ballot.setBallotStatus("Success");
        //                 String email = userService.findUserByUsername(ballot.getSK()).getEmail();
        //                 mailService.sendTextEmail(email, "Success"); //this throws IOException
        //             } else {
        //                 ballot.setBallotStatus("Fail");
        //                 String email = userService.findUserByUsername(ballot.getSK()).getEmail();
        //                 mailService.sendTextEmail(email, "Fail"); //this throws IOException
        //             }
        //         }
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        
    }

    public void methodName(String winId) {
        try {
            System.out.println("test");
            List<Relationship> relationships = relationshipService.findAllGardensInWindow(winId);
            System.out.println(relationships.toString());
            List<String> gardens = new ArrayList<>();
    
            for (Relationship r : relationships) {
                System.out.println(r.getLeaseDuration());
                gardens.add(r.getSK());
            }

            System.out.println("======");
    
            for (String gardenName : gardens) {
                System.out.println(gardenName);
                List<Relationship> ballots = findAllBallotsInWindowGarden(winId, gardenName);
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


