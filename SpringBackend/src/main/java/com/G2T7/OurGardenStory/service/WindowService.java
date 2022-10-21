package com.G2T7.OurGardenStory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Service
public class WindowService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Window createWindow(final Window window) {
        try{
            System.out.println(window);
            dynamoDBMapper.save(window);
            return window;    
        }catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
