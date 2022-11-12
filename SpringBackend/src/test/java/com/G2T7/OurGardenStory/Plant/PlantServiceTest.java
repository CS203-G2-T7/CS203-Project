package com.G2T7.OurGardenStory.Plant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.annotation.Role;
import org.yaml.snakeyaml.LoaderOptions;

import com.G2T7.OurGardenStory.model.Plant;
import com.G2T7.OurGardenStory.service.PlantService;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlantServiceTest {
    @Mock
    private DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
    @InjectMocks
    private PlantService plantService;

    @Test
    void findAllPlants_allPlants_ReturnAllPlants() {
        // //DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        // PaginatedQueryList<Plant> expected = mock(PaginatedQueryList.class);
        // // Note that when needs to be completed before thenReturn can be called.
        // when(mapperMock.query(eq(Plant.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        // QueryService queryService = new QueryService(mapperMock);
        // PaginatedQueryList<Plant> actual = queryService.query();

        // assertEquals(expected, actual);
        
        //mock the query method
        //when(mapperMock.query(Plant.class, any(DynamoDBQueryExpression.class))).thenReturn(PaginatedQueryList<Plant>.class);
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
        // DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        // Plant expected = mock(Plant.class);

        // when(mapperMock.load(eq(Plant.class),any(String.class), any(String.class)))
        //     .thenReturn(expected);

        // LoadService loadService = new LoadService(mapperMock);
        // Plant actual = loadService.load();

        // assertEquals(expected, actual);
        Plant plant = new Plant("Plant", "New Plant", "New Plant Species", "New plant description");

        when(mapperMock.load(eq(Plant.class), any(String.class))).thenReturn(null);

        Plant foundPlant = plantService.findPlantByName("No name");
        assertEquals(foundPlant, null);

        //verify(mapperMock).load()

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

    @Test
    void createPlant_NewPlant_ReturnPlant() {
        Plant plant = new Plant("Plant", "New Plant", "New Plant Species", "New plant description");

        //mock the load operation
        when(mapperMock.load(eq(Plant.class), any(String.class))).thenReturn(null);
        //mock the save operation
        doNothing().when(mapperMock).save(plant);

        Plant savedPLant = plantService.createPlant(plant);
        assertNotNull(savedPLant);

        //verify
        verify(mapperMock).load(Plant.class, plant.getPK(), plant.getSK());
        verify(mapperMock).save(plant);
    }

}

