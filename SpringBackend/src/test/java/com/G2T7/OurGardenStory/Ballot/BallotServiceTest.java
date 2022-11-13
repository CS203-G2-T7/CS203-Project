package com.G2T7.OurGardenStory.Ballot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.util.StringUtils;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Ballot;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.G2T7.OurGardenStory.service.BallotService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JsonNode;

import org.mockito.quality.Strictness;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BallotServiceTest {
        @Mock
        private DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        @InjectMocks
        private BallotService ballotService = mock(BallotService.class);

    @Test
    void findAllBallot_allBallot_ReturnAllBallot() {
        Window window = new Window("Window", "11-07-2022", "win1","P3M"); //random values
        Garden garden = new Garden("Garden", "New Garden Name", "New Garden Road", 20, "1.123", "9.987");

        DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
        PaginatedQueryList<Relationship> expected = mock(PaginatedQueryList.class);

        // Note that when needs to be completed before thenReturn can be called.
        when(mapperMock.query(eq(Relationship.class), Mockito.any(DynamoDBQueryExpression.class))).thenReturn(expected);

        QueryService queryService = new QueryService(mapperMock);
        PaginatedQueryList<Relationship> actual = queryService.query(window.getWindowId(), garden.getSK());

        assertEquals(expected, actual);
    }

    public class QueryService {
        private final DynamoDBMapper mapper;

        public QueryService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public PaginatedQueryList<Relationship> query(String windowId, String gardenName) {
            String capWinId = StringUtils.capitalize(windowId);

            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":GardenWinValue", new AttributeValue().withS(capWinId + "_" + gardenName.replace("-", " ")));
            DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
                    .withIndexName("WinId_GardenName-index")
                    .withConsistentRead(false)
                    .withKeyConditionExpression("WinId_GardenName = :GardenWinValue")
                    .withExpressionAttributeValues(eav);
            
                return mapper.query(Relationship.class, qe);
        }
    }

    // @Test
    // void findUserBallotInWindow_noSuchUserBallot_throwResourceNotFoundException() {
    //     when(mapperMock.load(eq(Ballot.class), any(String.class), any(String.class))).thenReturn(null);

    //     assertThrows(ResourceNotFoundException.class, () -> ballotService.findUserBallotInWindow("windowId", "username"));
    // } 

    // @Test
    // void addBallotInWindowGarden_successfulAddition_returnRelationship() {
    //     Ballot ballot = new Ballot("winId", "username", "gardenName", "ballotId", "04-04-2022", 33, 14, "PENDING", "PENDING");

    //     doNothing().when(mapperMock).save(ballot);

    //     JsonNode payload = new JsonNode() {
    //         "gardenName": "Sembawang Park"
    //     };
    //     Ballot savedBallot = ballotService.addBallotInWindowGarden("winId", "username", null);
    // }

    public class LoadService {
        private final DynamoDBMapper mapper;

        public LoadService(DynamoDBMapper mapper) {
            this.mapper = mapper;
        }

        public Relationship load() {            
            return mapper.load(Relationship.class, "No such user ballot in window", "No such user ballot in window");
        }
    }
}
