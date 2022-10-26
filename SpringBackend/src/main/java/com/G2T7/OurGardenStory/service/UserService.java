package com.G2T7.OurGardenStory.service;

import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.ReqResModel.UserSignUpRequest;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public User findUserByUsername(final String username) {
        User foundUser = dynamoDBMapper.load(User.class, "User", username);
        if (foundUser == null) {
            throw new ResourceNotFoundException("User " + username + " not found.");
        }
        return foundUser;
    }

    public List<User> findAllUsers() {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":USR", new AttributeValue().withS("User"));
        DynamoDBQueryExpression<User> qe = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("PK = :USR").withExpressionAttributeValues(eav);

        PaginatedQueryList<User> foundUserList = dynamoDBMapper.query(User.class, qe);
        if (foundUserList.size() == 0) {
            throw new ResourceNotFoundException("No users found.");
        }
        return foundUserList;
    }

    // called on /my-plants. Authenticated. Get username from JWT Token.
    // can have another service for plant related user operations. CRUD on
    // user-plant O2M relationship.
    public List<String> addUserPlantId(final String username, final List<String> newPlantIdList) {
        User foundUser = findUserByUsername(username);
        List<String> updatedPlantIdList = new ArrayList<String>(foundUser.getPlant());
        newPlantIdList.forEach(plantId -> {
            updatedPlantIdList.add(plantId);
        });
        foundUser.setPlant(updatedPlantIdList);
        dynamoDBMapper.save(foundUser);
        return foundUser.getPlant();
    }

    // Package accessible. Used by signUpService.
    void createUser(final UserSignUpRequest userSignUpRequest) {
        User newUser = new User("User", userSignUpRequest.getUsername(), userSignUpRequest.getGivenName(),
                userSignUpRequest.getFamilyName(), userSignUpRequest.getBirthDate(), userSignUpRequest.getEmail(),
                userSignUpRequest.getAddress(), userSignUpRequest.getPhoneNumber(),
                LocalDate.now().toString(), new ArrayList<String>());
        dynamoDBMapper.save(newUser);
    }
}
