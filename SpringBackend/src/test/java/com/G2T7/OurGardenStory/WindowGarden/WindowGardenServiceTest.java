package com.G2T7.OurGardenStory.WindowGarden;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.management.relation.Relation;

import org.springframework.expression.spel.ast.RealLiteral;
import org.springframework.util.StringUtils;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.GardenWin;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class WindowGardenServiceTest {
    @Test
    void findAllGarden_allGarden_ReturnAllGarden() {
        Window window = new Window("Window", "11-07-2022", "win1","P3M"); //random values

        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        PaginatedQueryList<Relationship> expected = mock(PaginatedQueryList.class);
        // Note that when needs to be completed before thenReturn can be called.
        when(mapperMock.query(eq(Relationship.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        QueryService queryService = new QueryService(mapperMock);
        PaginatedQueryList<Relationship> actual = queryService.query(window.getWindowId());

        assertEquals(expected, actual);
    }

    public class QueryService {
        private final DynamoDBMapper mapper;

        public QueryService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public PaginatedQueryList<Relationship> query(String windowId) {
            String capWinId = StringUtils.capitalize(windowId);
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":WINID", new AttributeValue().withS(capWinId));
            DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
                .withKeyConditionExpression("PK = :WINID").withExpressionAttributeValues(eav);
            
                return mapper.query(Relationship.class, qe);
        }
    }

    @Test
    void findGardenInWindow_noSuchWindowGarden_ReturnNull() {
        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        GardenWin expected = mock(GardenWin.class);

        when(mapperMock.load(eq(GardenWin.class),any(String.class), any(String.class)))
            .thenReturn(expected);

        LoadService loadService = new LoadService(mapperMock);
        GardenWin actual = loadService.load();

        assertEquals(expected, actual);
    } 

    public class LoadService {
        private final DynamoDBMapper mapper;

        public LoadService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public GardenWin load() {            
            return mapper.load(GardenWin.class, "No such garden in window", "No such garden in window");
        }
    }
}
