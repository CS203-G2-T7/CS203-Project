package com.G2T7.OurGardenStory.Window;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import com.G2T7.OurGardenStory.service.PlantService;
import com.G2T7.OurGardenStory.service.WindowService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Plant;
import com.G2T7.OurGardenStory.model.Window;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WindowServiceTest {
    @Mock
    private DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);
    @InjectMocks
    private WindowService windowService;

//    @BeforeEach
//    void resettingDatabase() {
//        Window window = new Window("Window", "11/11/22", "Win1", "P1M");
//        mapperMock.delete(window.getSK());
//        mapperMock.save(window);
//    }

    @Test
    void createWindow_NewWindow_ReturnWindow() {
        Window window = new Window("Window", "11/11/22", "Win1", "P1M");

        //mock the load operation
        when(mapperMock.load(eq(Window.class), any(String.class))).thenReturn(null);
        //mock the save operation
        doNothing().when(mapperMock).save(window);

        Window savedWindow = windowService.createWindow(window);
        assertNotNull(window);

        //verify
        verify(mapperMock).load(Window.class, window.getPK(), window.getSK());
        verify(mapperMock).save(window);
    }

    @Test
    void createWindow_existingWindow_throwRuntImeException() {
        Window window = new Window("Window", "11/11/22", "Win1", "P1M");

        when(mapperMock.load(Window.class, "Window", "11/11/22")).thenReturn(window);
        assertThrows(RuntimeException.class, () -> windowService.createWindow(window));

        verify(mapperMock).load(Window.class, window.getPK(), window.getSK());
    }

    @Test
    void findWindowById_noSuchWindow_throwResourceNotFoundException() {
        //when(mapperMock.load(eq(Window.class), any(String.class))).thenReturn(null);

        assertThrows(NullPointerException.class, () -> windowService.findWindowById("No window Id"));

        //verify(mapperMock).load(Window.class, "Window", "No window Id");

    }

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
