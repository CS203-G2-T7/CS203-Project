package com.G2T7.OurGardenStory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Plant;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import java.util.*;

@Service
public class PlantService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<Plant> findAllPlants() {
        return null;
    }

    public Plant findPlantByName(String plantName) {
        return null;
    }

    public Plant createPlant(Plant plant) {
        return null;
    }

    public Plant putPlant(final String plantName, final String description) {
        return null;
    }

    public Plant deletePlant(final String plantName) {
        return null;
    }
}
