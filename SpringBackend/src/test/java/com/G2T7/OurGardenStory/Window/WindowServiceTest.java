package com.G2T7.OurGardenStory.Window;

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
import com.G2T7.OurGardenStory.model.Plant;
import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class WindowServiceTest {
    @Test
    void findAllWindow_allWindow_ReturnAllWindow() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        PaginatedQueryList<Window> expected = mock(PaginatedQueryList.class);
        // Note that when needs to be completed before thenReturn can be called.
        when(mapperMock.query(eq(Window.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        QueryService queryService = new QueryService(mapperMock);
        PaginatedQueryList<Window> actual = queryService.query();

        assertEquals(expected, actual);
    }

    public class QueryService {
        private final DynamoDBMapper mapper;

        public QueryService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public PaginatedQueryList<Window> query() {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":WIN", new AttributeValue().withS(Window.entityName));
            DynamoDBQueryExpression<Window> qe = new DynamoDBQueryExpression<Window>()
                    .withKeyConditionExpression("PK = :WIN").withExpressionAttributeValues(eav);
            
                return mapper.query(Window.class, qe);
        }
    }

    @Test
    void findWindowById_noSuchWindow_ReturnNull() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        Window expected = mock(Window.class);

        when(mapperMock.load(eq(Window.class),any(String.class), any(String.class)))
            .thenReturn(expected);

        LoadService loadService = new LoadService(mapperMock);
        Window actual = loadService.load();

        assertEquals(expected, actual);
    } 

    public class LoadService {
        private final DynamoDBMapper mapper;

        public LoadService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public Window load() {            
            return mapper.load(Window.class, "No such window", "No such window");
        }
    }
}
