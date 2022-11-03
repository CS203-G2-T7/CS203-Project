package com.G2T7.OurGardenStory.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.controller.GeocodeService;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Ballot;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
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
    private WinGardenService relationshipService;

    /**
     * Finds all ballots given a window and a garden
     * Has to query GSI of WinId_GardenName-index to get ballots.
     * 
     * @windowId ID of window
     * @gardenName name of garden
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

        if (!relationshipService.validateWinExist(capWinId)) {
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
        return foundBallots;
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
     * Validate:
     * 1. Window Exists
     * 2. Garden Exists
     * 3. Garden Window relationship exists
     * 4. Ballot post date within window frame
     * 5. User has not balloted in same window before.
     * 
     * @param windowId
     * @param username
     * @param payload
     * @return addedBallot
     */

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
        validateBallotPostDate(windowId, date);
        validateGardenInWindow(windowId, payload.get("gardenName").asText());
        validateUserHasBallotedBeforeInSameWindow(windowId, username);

        Relationship ballot = new Ballot(capWinId, username, garden.getSK(),
                "Ballot" + String.valueOf(++Ballot.numInstance), currentDate, distance,
                "PENDING");

        dynamoDBMapper.save(ballot);
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

        // Relationship ballot = new Ballot(capWinId, username, capWinId + "-" +
        // garden.getSK(), toUpdateBallot.getBallotId(), currentDate, distance,
        // Relationship.BallotStatus.PENDING);
        Relationship ballot = new Ballot(capWinId, username, garden.getSK(), toUpdateBallot.getBallotId(), currentDate,
                distance,
                "PENDING");

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    public void deleteBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);

        Relationship ballotToDelete = findUserBallotInWindow(capWinId, username);
        dynamoDBMapper.delete(ballotToDelete);
    }

    // Check ballot post date within window
    public void validateBallotPostDate(String windowId, LocalDate date) {
        Window foundWindow = windowService.findWindowById(windowId).get(0);
        LocalDate winStartDate = convertStringToLocalDate(foundWindow.getSK());

        String winDurationString = foundWindow.getWindowDuration(); // In Period format. 3M, 1d, 1Y
        Period winDuration = convertStringToPeriod(winDurationString); // P3M -> 3 months
        System.out.println("Window duration: " + winDuration);

        LocalDate winEndDate = winStartDate.plus(winDuration);
        System.out.println("End date: " + winEndDate);

        String errorMessage = null;
        if (date.isBefore(winStartDate)) {
            errorMessage = "before";
        } else if (date.isAfter(winEndDate)) {
            errorMessage = "after";
        }

        if (errorMessage != null) {
            throw new IllegalArgumentException(
                    "Ballot is submitted " + errorMessage + " " + StringUtils.capitalize(windowId)
                            + " balloting period.\nStart date: "
                            + winStartDate.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")) + "\nEnd date:   "
                            + winEndDate.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
        }
        // reach here no error
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

    /**
     * Converts string in the format MM-dd-yyyy to a LocalDate object.
     * 
     * @param date
     * @return convertedLocalDate object
     */
    public LocalDate convertStringToLocalDate(String date) {
        String dateComponents[] = date.split("-");

        int year = Integer.parseInt(dateComponents[2]);
        int month = Integer.parseInt(dateComponents[0]);
        int day = Integer.parseInt(dateComponents[1]);

        LocalDate convertedDate = LocalDate.of(year, month, day);
        return convertedDate;
    }

    // Obtains a Period from a text string such as PnYnMnD
    public Period convertStringToPeriod(String periodInput) {
        Period convertedPeriod = Period.parse(periodInput);
        return convertedPeriod;
    }
}
