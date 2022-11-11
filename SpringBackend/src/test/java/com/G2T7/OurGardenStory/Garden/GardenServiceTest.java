package com.G2T7.OurGardenStory.Garden;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.G2T7.OurGardenStory.model.Garden;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class GardenServiceTest {
    DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);

    @Test
    void findAllGarden_allGarden_ReturnAllGarden() {
        PaginatedQueryList<Garden> expected = mock(PaginatedQueryList.class);
        // Note that when needs to be completed before thenReturn can be called.
        when(mapperMock.query(eq(Garden.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        QueryService queryService = new QueryService(mapperMock);
        PaginatedQueryList<Garden> actual = queryService.queryAllGardens();

        assertEquals(expected, actual);
        verify(mapperMock).query(eq(Garden.class), Mockito.any(DynamoDBQueryExpression.class));
    }

    @Test
    void findGardenByGardenName_noSuchGarden_ReturnNull() {
        Garden expected = mock(Garden.class);

        when(mapperMock.load(eq(Garden.class), any(String.class), any(String.class)))
                .thenReturn(expected);

        LoadService loadService = new LoadService(mapperMock);
        Garden actual = loadService.load("No such Garden", "No such Garden");

        assertEquals(expected, actual);
        verify(mapperMock).load(Garden.class, "No such Garden", "No such Garden");
    }

    @Test
    void findGardenByGardenName_GardenPresent_ReturnGarden() {
        Garden expected = mock(Garden.class);

        when(mapperMock.load(Garden.class, "Garden", "Sembawang Park"))
                .thenReturn(expected);

        LoadService loadService = new LoadService(mapperMock);
        Garden actual = loadService.load("Garden", "Sembawang Park");
        verify(mapperMock).load(Garden.class, "Garden", "Sembawang Park");
        assertEquals(expected, actual);
    }
}

class LoadService {
    private final DynamoDBMapper mapper;

    public LoadService(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public Garden load(String pk, String sk) {
        return mapper.load(Garden.class, pk, sk);
    }
}

class QueryService {
    private final DynamoDBMapper mapper;

    public QueryService(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public PaginatedQueryList<Garden> queryAllGardens() {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":GDN", new AttributeValue().withS("Garden"));
        DynamoDBQueryExpression<Garden> qe = new DynamoDBQueryExpression<Garden>()
                .withKeyConditionExpression("PK = :GDN").withExpressionAttributeValues(eav);

        return mapper.query(Garden.class, qe);
    }
}