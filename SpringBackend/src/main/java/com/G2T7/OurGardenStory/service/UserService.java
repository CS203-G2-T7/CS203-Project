package com.G2T7.OurGardenStory.service;

import com.G2T7.OurGardenStory.exception.CustomException;
import com.G2T7.OurGardenStory.model.*;
import com.G2T7.OurGardenStory.model.ReqResModel.UserSignUpRequest;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {

    private final DynamoDBMapper dynamoDBMapper;
    private final PlantService plantService;

    @Autowired
    public UserService(DynamoDBMapper dynamoDBMapper, PlantService plantService) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.plantService = plantService;
    }

    /**
    * Get the User object corresponding to a username
    * If the username does not correspond to a registered User, throw ResourceNotFoundException
    *
    * @param username a String
    * @return the User object corresponding to the given username
    */
    public User findUserByUsername(final String username) {
        User foundUser = dynamoDBMapper.load(User.class, "User", username);
        if (foundUser == null) {
            throw new ResourceNotFoundException("User " + username + " not found.");
        }
        return foundUser;
    }

    /**
    * Get all registered Users in the database
    * If there are no registered Users, throw ResourceNotFoundException
    *
    * @return a list of all Users 
    */
    public List<User> findAllUsers() {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":USR", new AttributeValue().withS("User"));
        DynamoDBQueryExpression<User> qe = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("PK = :USR").withExpressionAttributeValues(eav);

        PaginatedQueryList<User> foundUserList = dynamoDBMapper.query(User.class, qe);
        if (foundUserList.size() == 0) {
            throw new ResourceNotFoundException("No users found.");
        }
        return foundUserList;
    }

    /**
    * Create and save a new User object into database
    * If User already exists, throw IllegalArgumentException
    *
    * @param userSignUpRequest containing the user's signup details
    */
    // Package accessible. Used by signUpService.
    void createUser(final UserSignUpRequest userSignUpRequest) {
        User foundUser = dynamoDBMapper.load(User.class, "User", userSignUpRequest.getUsername());
        if (foundUser != null) {
            throw new IllegalArgumentException("User already exists.");
        }
        String birthday = LocalDate.parse(userSignUpRequest.getBirthDate(),
                DateTimeFormatter.ofPattern("MM-dd-yyyy")).atTime(0, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(birthday);
        User newUser = new User("User", userSignUpRequest.getUsername(), userSignUpRequest.getGivenName(),
                userSignUpRequest.getFamilyName(), birthday, userSignUpRequest.getEmail(),
                userSignUpRequest.getAddress(), userSignUpRequest.getPhoneNumber(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), new ArrayList<>());
        dynamoDBMapper.save(newUser);
    }

    // Plant user relation

    /**
    * Add an array of plants to a user's plantList
    * If the plantName does not correspond to an existing Plant object in database, throw ResourceNotFoundException
    *
    * @param username a String
    * @param payload containing an array of plantNames
    * @return the updated List of plantNames for the user
    */
    public List<String> addUserPlantName(final String username, JsonNode payload) {
        User foundUser = findUserByUsername(username);
        List<String> updatedPlantIdList = new ArrayList<>(foundUser.getPlant());
        payload.forEach(relation -> {
            String plantName = relation.get("plantName").asText();
            if (dynamoDBMapper.load(Plant.class, "Plant", plantName) == null) {
                throw new ResourceNotFoundException("Plant not found");
            }
            if (foundUser.getPlant().contains(plantName)) {
                throw new CustomException("Plant already added");
            }
            updatedPlantIdList.add(plantName);
        });

        foundUser.setPlant(updatedPlantIdList);
        dynamoDBMapper.save(foundUser);
        return foundUser.getPlant();
    }

    /**
    * Get all the user's Plants
    * If the username does not correspond to a registered user, throw ResourceNotFoundException
    *
    * @param username a String
    * @return a list of all Plants belonging to a user
    */
    public List<Plant> findAllUserPlants(final String username) {
        User user = findUserByUsername(username);
        List<String> plantNames = user.getPlant();
        List<Plant> returnPlants = new ArrayList<>();
        for (String s : plantNames) {
            Plant p = plantService.findPlantByName(s);
            returnPlants.add(p);
        }
        return returnPlants;
    }

    /**
    * Get a specific Plant object that belong to a user, given a plantName
    * If the username does not correspond to a registered User in database, or if the plantName does not
    * correspond to a plant in the user's plant list, throw an Exception
    *
    * @param username a String
    * @param plantName a String
    * @return the Plant object corresponding to the given plantName that belongs to a user
    */
    public Plant findUserPlant(final String username, final String plantName) {
        User user = findUserByUsername(username);
        List<String> plantNames = user.getPlant();
        if (!plantNames.contains(plantName)) {
            throw new CustomException("Plant is not in your collection");
        }
        return plantService.findPlantByName(plantName);
    }

    /**
    * Delete a specific Plant object that belong to a user, given a plantName
    * If the username does not correspond to a registered User in database, or if the plantName does not
    * correspond to a plant in the user's plant list, throw an Exception
    *
    * @param username a String
    * @param payload payload contains a String value plantName
    * @return the deleted Plant object corresponding to the given plantName that belongs to a user, if deletion was successful
    */
    public List<String> removeUserPlantName(final String username, JsonNode payload) {
        User foundUser = findUserByUsername(username);
        List<String> updatedPlantIdList = new ArrayList<>(foundUser.getPlant());
        payload.forEach(relation -> {
            String plantName = relation.get("plantName").asText();
            if (dynamoDBMapper.load(Plant.class, "Plant", plantName) == null) {
                throw new CustomException("Plant not found");
            }
            updatedPlantIdList.remove(plantName);
        });
        foundUser.setPlant(updatedPlantIdList);
        dynamoDBMapper.save(foundUser);
        return foundUser.getPlant();
    }
}
