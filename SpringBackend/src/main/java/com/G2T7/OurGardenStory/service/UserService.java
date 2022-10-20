package com.G2T7.OurGardenStory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.repository.UserRepo;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Service
public class UserService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    
    public User createUser(final User user) {
        try{
            System.out.println(user);
            dynamoDBMapper.save(user);
            return user;    
          }catch(Exception e) {
            System.out.println(e);
          }
          return null;
    }
}
