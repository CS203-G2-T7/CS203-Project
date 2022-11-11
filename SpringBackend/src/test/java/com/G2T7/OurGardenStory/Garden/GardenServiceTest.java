package com.G2T7.OurGardenStory.Garden;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import com.G2T7.OurGardenStory.model.Garden;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class GardenServiceTest {
    @Test
    void findAllGarden_allGarden_ReturnAllGarden() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        PaginatedQueryList<Garden> expected = mock(PaginatedQueryList.class);
        // Note that when needs to be completed before thenReturn can be called.
        when(mapperMock.query(eq(Garden.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        QueryService queryService = new QueryService(mapperMock);
        PaginatedQueryList<Garden> actual = queryService.query();

        assertEquals(expected, actual);
    }

    public class QueryService {
        private final DynamoDBMapper mapper;

        public QueryService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public PaginatedQueryList<Garden> query() {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":GDN", new AttributeValue().withS("Garden"));
            DynamoDBQueryExpression<Garden> qe = new DynamoDBQueryExpression<Garden>()
                .withKeyConditionExpression("PK = :GDN").withExpressionAttributeValues(eav);
            
                return mapper.query(Garden.class, qe);
        }
    }

    @Test
    void findGardenByGardenName_noSuchGarden_ReturnNull() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        Garden expected = mock(Garden.class);

        when(mapperMock.load(eq(Garden.class),any(String.class), any(String.class)))
            .thenReturn(expected);

        LoadService loadService = new LoadService(mapperMock);
        Garden actual = loadService.load();

        assertEquals(expected, actual);
    } 

    public class LoadService {
        private final DynamoDBMapper mapper;

        public LoadService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public Garden load() {            
            return mapper.load(Garden.class, "No such garden", "No such garden");
        }
    }
}
