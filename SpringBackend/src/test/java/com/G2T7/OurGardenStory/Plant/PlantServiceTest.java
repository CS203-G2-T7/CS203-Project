package com.G2T7.OurGardenStory.Plant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.yaml.snakeyaml.LoaderOptions;

import com.G2T7.OurGardenStory.model.Plant;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class PlantServiceTest {
    @Test
    void findAllPlants_allPlants_ReturnAllPlants() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        PaginatedQueryList<Plant> expected = mock(PaginatedQueryList.class);
        // Note that when needs to be completed before thenReturn can be called.
        when(mapperMock.query(eq(Plant.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        QueryService queryService = new QueryService(mapperMock);
        PaginatedQueryList<Plant> actual = queryService.query();

        assertEquals(expected, actual);
    }

    public class QueryService {
        private final DynamoDBMapper mapper;

        public QueryService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public PaginatedQueryList<Plant> query() {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":PLNT", new AttributeValue().withS("Plant"));
            DynamoDBQueryExpression<Plant> qe = new DynamoDBQueryExpression<Plant>()
                .withKeyConditionExpression("PK = :PLNT").withExpressionAttributeValues(eav);
            
                return mapper.query(Plant.class, qe);
        }
    }

    @Test
    void findPlantByName_noSuchPlant_ReturnNull() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        Plant expected = mock(Plant.class);

        when(mapperMock.load(eq(Plant.class),any(String.class), any(String.class)))
            .thenReturn(expected);

        LoadService loadService = new LoadService(mapperMock);
        Plant actual = loadService.load();

        assertEquals(expected, actual);
    } 

    public class LoadService {
        private final DynamoDBMapper mapper;

        public LoadService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public Plant load() {            
            return mapper.load(Plant.class, "No such plant", "No such plant");
        }
    }

}

