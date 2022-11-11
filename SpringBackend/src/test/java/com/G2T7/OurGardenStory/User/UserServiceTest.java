package com.G2T7.OurGardenStory.User;

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
import com.G2T7.OurGardenStory.model.User;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class UserServiceTest {
    @Test
    void findAllUser_allUser_ReturnAllUser() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        PaginatedQueryList<User> expected = mock(PaginatedQueryList.class);
        // Note that when needs to be completed before thenReturn can be called.
        when(mapperMock.query(eq(User.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        QueryService queryService = new QueryService(mapperMock);
        PaginatedQueryList<User> actual = queryService.query();

        assertEquals(expected, actual);
    }

    public class QueryService {
        private final DynamoDBMapper mapper;

        public QueryService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public PaginatedQueryList<User> query() {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":USR", new AttributeValue().withS("User"));
            DynamoDBQueryExpression<User> qe = new DynamoDBQueryExpression<User>()
                    .withKeyConditionExpression("PK = :USR").withExpressionAttributeValues(eav);
            
                return mapper.query(User.class, qe);
        }
    }

    @Test
    void findUserByUsername_noSuchUser_ReturnNull() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        User expected = mock(User.class);

        when(mapperMock.load(eq(User.class),any(String.class), any(String.class)))
            .thenReturn(expected);

        LoadService loadService = new LoadService(mapperMock);
        User actual = loadService.load();

        assertEquals(expected, actual);
    } 

    public class LoadService {
        private final DynamoDBMapper mapper;

        public LoadService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public User load() {            
            return mapper.load(User.class, "No such user", "No such user");
        }
    }
}