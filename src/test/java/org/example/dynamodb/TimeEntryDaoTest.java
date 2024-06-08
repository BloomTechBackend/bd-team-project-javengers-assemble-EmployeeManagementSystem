package org.example.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import org.example.dynamodb.model.TimeEntryModel;
import org.example.exceptions.TimeEntriesNotFoundException;
import org.example.model.TimeEntry;
import org.example.utils.ModelConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class TimeEntryDaoTest {
    private AutoCloseable mocks;
    @Mock
    DynamoDBMapper dynamoDBMapper;
    @InjectMocks
    TimeEntryDao timeEntryDao;

    private TimeEntryModel timeEntryModel;
    private TimeEntry timeEntry;
    private final String employeeId = "employee1";
    private final String entryId = "TE123456";
    private final LocalDateTime timeIn = LocalDateTime.of(2024, 5, 28, 9, 0);
    private final LocalDateTime timeOut = LocalDateTime.of(2024, 5, 28, 17, 0);

    private List<TimeEntryModel> timeEntryModelList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        mocks = openMocks(this);

        // TimeEntry Setup
        timeEntry = TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withEntryId(entryId)
                .withTimeIn(timeIn)
                .withTimeOut(timeOut)
                .build();

        // TimeEntryModel Setup
        timeEntryModel = new TimeEntryModel();

        timeEntryModel.setEmployeeId(employeeId);
        timeEntryModel.setEntryId(entryId);
        timeEntryModel.setTimeIn(timeIn.toString());
        timeEntryModel.setTimeOut(timeOut.toString());
        timeEntryModel.setDuration(8.0);

        TimeEntryModel timeEntryModel1 = new TimeEntryModel();

        timeEntryModel1.setEmployeeId(employeeId);
        timeEntryModel1.setEntryId(entryId);
        timeEntryModel1.setTimeIn(LocalDateTime.of(2024, 5, 29, 9, 0).toString());
        timeEntryModel1.setTimeOut(LocalDateTime.of(2024, 5, 29, 17, 0).toString());
        timeEntryModel1.setDuration(8.0);

        TimeEntryModel timeEntryModel2 = new TimeEntryModel();

        timeEntryModel2.setEmployeeId(employeeId);
        timeEntryModel2.setEntryId(entryId);
        timeEntryModel2.setTimeIn(LocalDateTime.of(2024, 5, 31, 9, 0).toString());
        timeEntryModel2.setTimeOut(LocalDateTime.of(2024, 5, 31, 17, 0).toString());
        timeEntryModel2.setDuration(8.0);

        TimeEntryModel timeEntryModel3 = new TimeEntryModel();

        timeEntryModel3.setEmployeeId(employeeId);
        timeEntryModel3.setEntryId(entryId);
        timeEntryModel3.setTimeIn(LocalDateTime.of(2024, 6, 1, 9, 0).toString());
        timeEntryModel3.setTimeOut(LocalDateTime.of(2024, 6, 1, 17, 0).toString());
        timeEntryModel3.setDuration(8.0);

        TimeEntryModel timeEntryModel4 = new TimeEntryModel();

        timeEntryModel4.setEmployeeId(employeeId);
        timeEntryModel4.setEntryId(entryId);
        timeEntryModel4.setTimeIn(LocalDateTime.of(2024, 6, 2, 9, 0).toString());
        timeEntryModel4.setTimeOut(LocalDateTime.of(2024, 6, 2, 17, 0).toString());
        timeEntryModel4.setDuration(8.0);

        TimeEntryModel timeEntryModel5 = new TimeEntryModel();

        timeEntryModel5.setEmployeeId(employeeId);
        timeEntryModel5.setEntryId(entryId);
        timeEntryModel5.setTimeIn(LocalDateTime.of(2024, 5, 24, 9, 0).toString());
        timeEntryModel5.setTimeOut(LocalDateTime.of(2024, 5, 24, 17, 0).toString());
        timeEntryModel5.setDuration(8.0);

        // List of TimeEntries Setup
        timeEntryModelList.add(timeEntryModel);
        timeEntryModelList.add(timeEntryModel1);
        timeEntryModelList.add(timeEntryModel2);
        timeEntryModelList.add(timeEntryModel3);
        timeEntryModelList.add(timeEntryModel4);

    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void getTimeEntry_returnsInstanceOfTimeEntry() {
        when(dynamoDBMapper.load(any(), anyString(), anyString())).thenReturn(timeEntryModel);

        assertInstanceOf(TimeEntry.class, timeEntryDao.getTimeEntry(employeeId, entryId));
    }

    @Test
    public void getTimeEntry_withInvalidSearchCriteria_throwsTimeEntriesNotFoundException() {
        when(dynamoDBMapper.load(any(), anyString(), anyString())).thenReturn(null);

        assertThrows(TimeEntriesNotFoundException.class, () -> {
            timeEntryDao.getTimeEntry("Invalid", "Invalid");
        });
    }

    @Test
    public void getLastFiveTimeEntries_successfullyRetrievesEntries() {
        PaginatedQueryList<TimeEntryModel> mockPaginatedQueryList = mock(PaginatedQueryList.class);

        when(mockPaginatedQueryList.iterator()).thenReturn(timeEntryModelList.iterator());
        when(mockPaginatedQueryList.size()).thenReturn(timeEntryModelList.size());
        when(dynamoDBMapper.query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);

        List<TimeEntry> timeEntries = timeEntryDao.getLastFiveTimeEntries(employeeId);

        assertNotNull(timeEntries);
        assertEquals(5, timeEntries.size());
        verify(dynamoDBMapper, times(1)).query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class));
    }

    @Test
    public void getLastFiveTimeEntries_noEntriesFoundThrowsException() {
        PaginatedQueryList<TimeEntryModel> mockPaginatedQueryList = mock(PaginatedQueryList.class);

        when(mockPaginatedQueryList.iterator()).thenReturn(new ArrayList<TimeEntryModel>().iterator());
        when(mockPaginatedQueryList.size()).thenReturn(0);
        when(mockPaginatedQueryList.isEmpty()).thenReturn(true);
        when(dynamoDBMapper.query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);

        assertThrows(TimeEntriesNotFoundException.class, () -> {
            timeEntryDao.getLastFiveTimeEntries("InvalidEmployeeId");
        });

        verify(dynamoDBMapper, times(1)).query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class));
    }

    @Test
    public void getLastFiveTimeEntries_entriesAreSortedCorrectly() {
        PaginatedQueryList<TimeEntryModel> mockPaginatedQueryList = mock(PaginatedQueryList.class);

        when(mockPaginatedQueryList.iterator()).thenReturn(timeEntryModelList.iterator());
        when(mockPaginatedQueryList.size()).thenReturn(timeEntryModelList.size());
        when(dynamoDBMapper.query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);

        List<TimeEntry> timeEntries = timeEntryDao.getLastFiveTimeEntries(employeeId);

        assertNotNull(timeEntries);
        assertEquals(5, timeEntries.size());
        verify(dynamoDBMapper, times(1)).query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class));

        for (int i = 0; i < timeEntries.size() - 1; i++) {
            assertTrue(timeEntries.get(i).getTimeIn().isAfter(timeEntries.get(i + 1).getTimeIn()));
        }
    }

    @Test
    public void getTimeEntries_successfullyRetrievesEntries() {
        List<TimeEntry> timeEntryList = ModelConverter.fromTimeEntryModelList(timeEntryModelList);

        PaginatedQueryList<TimeEntryModel> mockPaginatedQueryList = mock(PaginatedQueryList.class);
        when(mockPaginatedQueryList.iterator()).thenReturn(timeEntryModelList.iterator());
        when(mockPaginatedQueryList.size()).thenReturn(timeEntryModelList.size());
        when(dynamoDBMapper.query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);

        List<TimeEntry> retrievedTimeEntries = timeEntryDao.getTimeEntries(employeeId);

        assertNotNull(retrievedTimeEntries);
        assertEquals(timeEntryList.size(), retrievedTimeEntries.size());
        verify(dynamoDBMapper, times(1)).query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class));
    }

    @Test
    public void getTimeEntries_noEntriesFoundThrowsException() {
        PaginatedQueryList<TimeEntryModel> mockPaginatedQueryList = mock(PaginatedQueryList.class);
        when(mockPaginatedQueryList.iterator()).thenReturn(new ArrayList<TimeEntryModel>().iterator());
        when(mockPaginatedQueryList.size()).thenReturn(0);
        when(mockPaginatedQueryList.isEmpty()).thenReturn(true);
        when(dynamoDBMapper.query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mockPaginatedQueryList);

        assertThrows(TimeEntriesNotFoundException.class, () -> {
            timeEntryDao.getTimeEntries("InvalidEmployeeId");
        });

        verify(dynamoDBMapper, times(1)).query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class));
    }

    @Test
    public void getTimeEntries_unexpectedErrorThrowsException() {
        when(dynamoDBMapper.query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            timeEntryDao.getTimeEntries(employeeId);
        });

        assertEquals("Unexpected error", thrown.getMessage());
        verify(dynamoDBMapper, times(1)).query(eq(TimeEntryModel.class), any(DynamoDBQueryExpression.class));
    }

    @Test
    public void saveTimeEntry_savesTimeEntryToTable() {
        doNothing().when(dynamoDBMapper).save(any(TimeEntryModel.class));

        TimeEntryModel savedTimeEntryModel = timeEntryDao.saveTimeEntry(timeEntry);

        assertNotNull(savedTimeEntryModel);
        verify(dynamoDBMapper, times(1)).save(any(TimeEntryModel.class));
    }

    @Test
    public void saveTimeEntry_withInvalidCriteria_throwsException() {
        doNothing().when(dynamoDBMapper).save(any(TimeEntryModel.class));

        assertThrows(Exception.class, () -> {
            timeEntryDao.saveTimeEntry(null);
        });
    }

    @Test
    public void saveTimeEntries_savesTimeEntriesToTable() {
        when(dynamoDBMapper.batchSave(any(List.class))).thenReturn(null);

        List<TimeEntry> timeEntries = ModelConverter.fromTimeEntryModelList(timeEntryModelList);
        List<TimeEntryModel> savedTimeEntryModels = timeEntryDao.saveTimeEntries(timeEntries);

        assertNotNull(savedTimeEntryModels);
        assertEquals(timeEntryModelList.size(), savedTimeEntryModels.size());
        verify(dynamoDBMapper, times(1)).batchSave(any(List.class));
    }

    @Test
    public void saveTimeEntries_withInvalidCriteria_throwsException() {
        when(dynamoDBMapper.batchSave(any(List.class))).thenReturn(null);

        assertThrows(Exception.class, () -> {
            timeEntryDao.saveTimeEntries(null);
        });
    }

}
