package com.G2T7.OurGardenStory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.repository.GardenRepo;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Service
public class GardenService {
  @Autowired
  private GardenRepo gardenRepository;

  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  public Garden createGarden(final Garden garden) {
    try{
      dynamoDBMapper.save(garden);
      return garden;
    }catch(Exception e) {
      System.out.println(e);
    }
    return null;
  }
}