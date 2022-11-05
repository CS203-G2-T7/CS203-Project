package com.G2T7.OurGardenStory.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.exception.CustomException;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Ballot;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.G2T7.OurGardenStory.utils.*;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.*;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class BallotService {

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
    private WinGardenService winGardenService;


    /**
     * Finds all ballots given a window and a garden
     * Has to query GSI of WinId_GardenName-index to get ballots.
     * 
     * @param windowId ID of window
     * @param gardenName name of garden
     * @return List of found ballots
     * 
     *         Validations:
     *         1. Window exists
     *         2. Garden exists
     *         3. Garden is in window
     *         4. Ballots List not empty
     */


    public List<Relationship> findAllBallotsInWindowGarden(String windowId, String gardenName) {
        String capWinId = StringUtils.capitalize(windowId);

        if (!winGardenService.validateWinExist(capWinId)) {
            throw new ResourceNotFoundException(capWinId + " does not exist.");
        }
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":GardenWinValue", new AttributeValue().withS(capWinId + "_" + gardenName));
        DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
                .withIndexName("WinId_GardenName-index")
                .withConsistentRead(false)
                .withKeyConditionExpression("WinId_GardenName = :GardenWinValue")
                .withExpressionAttributeValues(eav);

        PaginatedQueryList<Relationship> foundBallots = dynamoDBMapper.query(Relationship.class, qe);
        if (foundBallots.isEmpty() || foundBallots == null) {
            throw new ResourceNotFoundException("There are no ballots in " + capWinId + ".");
        }

        List<Relationship> result = new ArrayList<>();

        for (Relationship r : foundBallots) {
            String sk = r.getSK();
            if (!winGardenService.validateGardenExist(sk)) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Gets a user's ballot given windowId and username. Username supposedly from
     * JWT token. Ballots are unique by PK = windowId and SK = username. Can just
     * use load from DynamoDBMapper.
     * 
     * Validate:
     * 1. windowId exists
     * 
     * @param windowId
     * @param username
     * @return FoundBallot
     */

    public Relationship findUserBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }
        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, capWinId, username);
        if (foundBallot == null) {
            throw new ResourceNotFoundException("Ballot for " + username + " in window " + windowId + " not found");
        }
        return foundBallot;
    }

    /**
     * Adds ballot given garden, window and username. Called when place ballot
     * clicked
     * on frontend.
     * 
     * Basic validation:
     * 1. Window Exists
     * 2. Garden Exists
     * 3. Garden Window relationship exists
     * 4. User authenticated
     * 
     * Advanced validation:
     * 5. Ballot post date within window frame
     * 6. User has not balloted in same window before.
     * 
     * Call geocode to get distance
     * Save new ballot
     * 
     * @param windowId
     * @param username
     * @param payload
     * @return addedBallot
     */
    public Relationship addBallotInWindowGarden(String windowId, String username, JsonNode payload) {
        String capWinId = StringUtils.capitalize(windowId);

        // Basic existence validations
        winGardenService.findGardenInWindow(capWinId, payload.get("gardenName").asText());
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        // Advanced validations
        if (windowService.findWindowById(capWinId).get(0).getWindowDuration().charAt(1) != 'T') {
            validateBallotPostDate(capWinId, LocalDate.now());
        }
        validateUserMultipleBallots(capWinId, username);

        // Passed all validations, then create call geocode. Create ballot and save.
        Garden garden = gardenService.findGardenByGardenName(payload.get("gardenName").asText());
        User user = userService.findUserByUsername(username);

        double distance = geocodeService.saveDistance(username, user.getAddress(), garden.getLongitude(),
                garden.getLatitude());

        Relationship ballot = new Ballot(capWinId, username, garden.getSK(),
                "Ballot" + String.valueOf(++Ballot.numInstance),
                DateUtil.convertLocalDateToString(LocalDate.now()),
                distance, Ballot.BallotStatus.PENDING.value, Ballot.PaymentStatus.PENDING.value);

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    /**
    * Update the Garden that the ballot is balloting for
    *
    * @param winId
    * @param gardenName
    * @param payload includes a String gardenName
    * @return the updated Ballot, if update is successful
    */
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

        // Relationship ballot = new Ballot(capWinId, username, capWinId + "-" +
        // garden.getSK(), toUpdateBallot.getBallotId(), currentDate, distance,
        // Relationship.BallotStatus.PENDING);
        Relationship ballot = new Ballot(capWinId, username, garden.getSK(), toUpdateBallot.getBallotId(), currentDate,
                distance,
                Ballot.BallotStatus.PENDING.value, Ballot.BallotStatus.PENDING.value);

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    /**
    * Update a ballot that was previously posted
    *
    * @param windowId
    * @param username
    * @return the deleted Ballot
    */
    public void deleteBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);

        Relationship ballotToDelete = findUserBallotInWindow(capWinId, username);
        dynamoDBMapper.delete(ballotToDelete);
    }

    /**
    * checks whether a user that is attempting to place a ballot is a registered user, and is at least 18 years old
    *
    * @param username
    */
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

    // Check ballot post date within window
    public void validateBallotPostDate(String capWinId, LocalDate date) {
        Window foundWindow = windowService.findWindowById(capWinId).get(0);
        LocalDate winStartDate = DateUtil.convertStringToLocalDate(foundWindow.getSK());

        String winDurationString = foundWindow.getWindowDuration(); // In Period format. 3M, 1d, 1Y
        Period winDuration = DateUtil.convertStringToPeriod(winDurationString); // P3M -> 3 months

        LocalDate winEndDate = winStartDate.plus(winDuration);

        String errorMessage = null;
        if (date.isBefore(winStartDate)) {
            errorMessage = "before";
        } else if (date.isAfter(winEndDate)) {
            errorMessage = "after";
        }

        if (errorMessage != null) {
            throw new IllegalArgumentException(
                    "Ballot is submitted " + errorMessage + " " + capWinId
                            + " balloting period.\nStart date: "
                            + winStartDate.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")) + "\nEnd date:   "
                            + winEndDate.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
        }
        // reach here no error
    }

    /**
    * Checks whether the user posting a ballot has already posted a ballot in the same window previously
    *
    * @param capWinId a String
    * @param username a String
    */
    public void validateUserMultipleBallots(String capWinId, String username) {
        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, capWinId, username);
        if (foundBallot != null) {
            throw new IllegalArgumentException("User " + username + " has already balloted in window " + capWinId);
        }
    }

    // public void validateIfGardenFull(Garden garden, String winId) {
    // List<Relationship> ballots = findAllBallotsInWindowForGarden(winId,
    // garden.getSK());
    // int numBallots = ballots.size();

    // Relationship gardenWin = dynamoDBMapper.load(Relationship.class, winId,
    // garden.getSK());

    // int numPlotsForBalloting = gardenWin.getNumPlotsForBalloting();

    // if (numBallots >= numPlotsForBalloting) {
    // throw new CustomException("Plots are full");
    // }
    // return;
    // }

}
