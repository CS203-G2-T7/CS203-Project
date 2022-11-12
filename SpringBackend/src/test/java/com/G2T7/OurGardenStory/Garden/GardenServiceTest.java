//package com.G2T7.OurGardenStory.Garden;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;
//
//import static org.mockito.Mockito.spy;
//
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.*;
//
//import org.apache.catalina.core.ApplicationContext;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.stubbing.Answer;
//import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
//
//import com.G2T7.OurGardenStory.config.DynamoDBConfig;
//import com.G2T7.OurGardenStory.model.Garden;
//import com.G2T7.OurGardenStory.service.GardenService;
//import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
//import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//
//@ExtendWith(MockitoExtension.class)
//public class GardenServiceTest {
//
//    @Mock
//    private DynamoDBMapper dynamoDBMapper;
//
//    @Mock
//    private DynamoDBMapperConfig dynamoDBMapperConfig;
//
//    @Mock
//    private AmazonDynamoDB dynamoDB;
//
//    @InjectMocks
//    private GardenService gardenService;
//
//  	private DynamoDBTemplate dynamoDBTemplate;
//
//    @Mock
//    private ApplicationContext applicationContext;
//
//    // @BeforeAll
//  	// public void setUp() {
//  	// 	this.dynamoDBTemplate = new DynamoDBTemplate(dynamoDB, dynamoDBMapper, dynamoDBMapperConfig);
// 	// 	this.dynamoDBTemplate.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
//    // }
//
//    DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
//
//
//    @Test
//    void findAllGarden_allGarden_ReturnAllGarden() {
//        PaginatedQueryList<Garden> expected = mock(PaginatedQueryList.class);
//        // Note that when needs to be completed before thenReturn can be called.
//        when(mapperMock.query(eq(Garden.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);
//
//        QueryService queryService = new QueryService(mapperMock);
//        PaginatedQueryList<Garden> actual = queryService.queryAllGardens();
//
//        assertEquals(expected, actual);
//        verify(mapperMock).query(eq(Garden.class), Mockito.any(DynamoDBQueryExpression.class));
//    }
//
//    @Test
//    void findGardenByGardenName_noSuchGarden_ThrowResourceNotFoundException() {
//        // DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
//        // Garden expected = mock(Garden.class);
//
//        // when(mapperMock.load(eq(Garden.class),any(String.class), any(String.class)))
//        //     .thenReturn(expected);
//
//        // LoadService loadService = new LoadService(mapperMock);
//        // Garden actual = loadService.load("No such Garden", "No such Garden");
//        //Garden actual = gardenService.findGardenByGardenName("No such Garden");
//        assertThrows(NullPointerException.class, () -> gardenService.findGardenByGardenName("No such Garden"));
//        //assertNull(actual);
//        //assertEquals(expected, actual);
//    }
//
//    void findGardenByGardenName_noSuchGarden_ReturnNull() {
//        Garden expected = mock(Garden.class);
//
//        when(mapperMock.load(eq(Garden.class), any(String.class), any(String.class)))
//                .thenReturn(expected);
//
//        LoadService loadService = new LoadService(mapperMock);
//        Garden actual = loadService.load("No such Garden", "No such Garden");
//
//        assertEquals(expected, actual);
//        verify(mapperMock).load(Garden.class, "No such Garden", "No such Garden");
//    }
//
//    @Test
//    void createGarden_newGarden_returnSavedGarden() throws Exception {
//        GardenService spyImpl = spy(gardenService);
//        DynamoDBMapper spyImpl2 = spy(dynamoDBMapper);
//
//        Garden garden = new Garden("Garden", "Garden Name", "Garden Address", 5, "3.5", "3.5");
//        doNothing().when(spyImpl2).save(garden);
//
//        Garden savedGarden = spyImpl.createGarden(garden);
//        assertNotNull(savedGarden);
//
//        verify(spyImpl2).load(garden);
//    }
//
//    @Test
//    void findGardenByGardenName_GardenPresent_ReturnGarden() {
//        Garden expected = mock(Garden.class);
//
//        when(mapperMock.load(Garden.class, "Garden", "Sembawang Park"))
//                .thenReturn(expected);
//
//        // LoadService loadService = new LoadService(mapperMock);
//        // Garden actual = loadService.load("Garden", "Sembawang Park");
//        Garden actual = gardenService.findGardenByGardenName("Sembawang Park");
//
//        // LoadService loadService = new LoadService(mapperMock);
//        // Garden actual = loadService.load("Garden", "Sembawang Park");
//        // verify(mapperMock).load(Garden.class, "Garden", "Sembawang Park");
//        assertEquals(expected, actual);
//    }
//}
//
//class LoadService {
//    private final DynamoDBMapper mapper;
//
//    public LoadService(DynamoDBMapper mapper) {
//        this.mapper = mapper;
//    }
//
//    public Garden load(String pk, String sk) {
//        return mapper.load(Garden.class, pk, sk);
//    }
//}
//
//class QueryService {
//    private final DynamoDBMapper mapper;
//
//    public QueryService(DynamoDBMapper mapper) {
//        this.mapper = mapper;
//    }
//
//    public PaginatedQueryList<Garden> queryAllGardens() {
//        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
//        eav.put(":GDN", new AttributeValue().withS("Garden"));
//        DynamoDBQueryExpression<Garden> qe = new DynamoDBQueryExpression<Garden>()
//                .withKeyConditionExpression("PK = :GDN").withExpressionAttributeValues(eav);
//
//        return mapper.query(Garden.class, qe);
//    }
//}