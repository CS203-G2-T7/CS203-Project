package com.G2T7.OurGardenStory.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.G2T7.OurGardenStory.service.UserService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.G2T7.OurGardenStory.model.Plant;
import com.G2T7.OurGardenStory.model.User;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    DynamoDBMapper dynamoDBMapper;

//    @Test
//    void createSignUpRequest_underageUser_throwIllegalArgumentException() {
//        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
//        userSignUpRequest.setUsername("John");
//        userSignUpRequest.setPassword("Password1");
//        userSignUpRequest.setUsername("JohnTest");
//        userSignUpRequest.setEmail("John@email.com");
//        userSignUpRequest.setFamilyName("Tan");
//        userSignUpRequest.setGivenName("John");
//        userSignUpRequest.setPhoneNumber("+6591234567");
//        userSignUpRequest.setBirthDate("01-01-2010");
//        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userSignUpRequest));
//    }

    @Test
    void findAllUser_allUser_throwNullPointerException() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        PaginatedQueryList<User> expected = mock(PaginatedQueryList.class);
        // Note that when needs to be completed before thenReturn can be called.
        when(mapperMock.query(eq(User.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        QueryService queryService = new QueryService(mapperMock);

        assertThrows(NullPointerException.class, () -> userService.findAllUsers());
    }

    @Test
    void findAllUserPlants_allUserPlants_throwResourceNotFoundException() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        List<Plant> allPlants = userService.findAllUserPlants("wfwefewfewf");
        assertThrows(ResourceNotFoundException.class, () -> userService.findAllUserPlants("John"));
    }

    @Test
    void findUserPlant_UserPlant_throwResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> userService.findUserPlant("John", "Kang Kong"));
    }

    @Test
    void removeUserPlant_UserPlant_throwResourceNotFoundException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jfactory = new JsonFactory(mapper);
        JsonParser jp = jfactory.createJsonParser("{\"k1\":\"v1\"}");
        JsonNode actualObj = mapper.readTree(jp);
        assertThrows(ResourceNotFoundException.class, () -> userService.removeUserPlantName("John", actualObj));
    }

    @Test
    void addUserPlant_UserPlant_throwResourceNotFoundException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jfactory = new JsonFactory(mapper);
        JsonParser jp = jfactory.createJsonParser("{\"k1\":\"v1\"}");
        JsonNode actualObj = mapper.readTree(jp);
        assertThrows(ResourceNotFoundException.class, () -> userService.addUserPlantName("John", actualObj));
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
