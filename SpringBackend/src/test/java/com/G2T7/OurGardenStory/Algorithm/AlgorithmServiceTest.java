package com.G2T7.OurGardenStory.Algorithm;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.mockito.quality.Strictness;

import com.G2T7.OurGardenStory.service.AlgorithmServiceImpl;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AlgorithmServiceTest {
    @Mock
    private DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
    @InjectMocks
    private AlgorithmServiceImpl algorithmService = mock(AlgorithmServiceImpl.class);

    @Test
    void getBallotSuccess_normalTest_returnSuccessfulBalloters() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("Alice", 2.1);
        map.put("Bob", 0.9);
        map.put("Charles", 4.8);
        map.put("Don", 6.9);

        when(algorithmService.getBallotSuccess(map, 3)).thenReturn(new ArrayList<String>());
        ArrayList<String> ballotSuccess = algorithmService.getBallotSuccess(map, 3);
        assertNotNull(ballotSuccess);

        verify(algorithmService).getBallotSuccess(map, 3);
    }

    @Test
    void callScheduleAlgo_haveSchedule_returnSchedule() {
        doNothing().when(algorithmService).scheduleAlgo("win1");

        verify(algorithmService).scheduleAlgo("win1");
    }

    // @Test
    // void doMagic_
}
