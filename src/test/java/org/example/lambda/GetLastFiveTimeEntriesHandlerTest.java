package org.example.lambda;

import org.example.dynamodb.TimeEntryDao;
import org.example.exceptions.TimeEntriesNotFoundException;
import org.example.model.TimeEntry;
import org.example.model.requests.GetLastFiveTimeEntriesRequest;
import org.example.model.results.GetLastFiveTimeEntriesResult;
import org.example.utils.gson.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetLastFiveTimeEntriesHandlerTest {
    private AutoCloseable mocks;
    @Mock
    private TimeEntryDao timeEntryDao;
    @InjectMocks
    private GetLastFiveTimeEntriesHandler lastFiveTimeEntriesHandler;

    private List<TimeEntry> timeEntryList = new ArrayList<>();
    private String employeeId = "Emp123";

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);

        timeEntryList.add(
                TimeEntry.builder()
                        .withEmployeeId(employeeId)
                        .withEntryId("TE124")
                        .withTimeIn(LocalDateTime.of(2024, 5, 29, 9, 0))
                        .withTimeOut(LocalDateTime.of(2024, 5, 29, 17, 0))
                        .withDuration(8.0)
                        .build()
        );

        timeEntryList.add(
                TimeEntry.builder()
                        .withEmployeeId(employeeId)
                        .withEntryId("TE125")
                        .withTimeIn(LocalDateTime.of(2024, 5, 30, 9, 0))
                        .withTimeOut(LocalDateTime.of(2024, 5, 30, 17, 0))
                        .withDuration(8.0)
                        .build()
        );

        timeEntryList.add(
                TimeEntry.builder()
                        .withEmployeeId(employeeId)
                        .withEntryId("TE126")
                        .withTimeIn(LocalDateTime.of(2024, 5, 31, 9, 0))
                        .withTimeOut(LocalDateTime.of(2024, 5, 31, 17, 0))
                        .withDuration(8.0)
                        .build()
        );

        timeEntryList.add(
                TimeEntry.builder()
                        .withEmployeeId(employeeId)
                        .withEntryId("TE127")
                        .withTimeIn(LocalDateTime.of(2024, 6, 1, 9, 0))
                        .withTimeOut(LocalDateTime.of(2024, 6, 1, 17, 0))
                        .withDuration(8.0)
                        .build()
        );

        timeEntryList.add(
                TimeEntry.builder()
                        .withEmployeeId(employeeId)
                        .withEntryId("TE128")
                        .withTimeIn(LocalDateTime.of(2024, 6, 4, 9, 0))
                        .withTimeOut(LocalDateTime.of(2024, 6, 4, 17, 0))
                        .withDuration(8.0)
                        .build()
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void requestHandler_retrievesLast5Entries() {
        when(timeEntryDao.getLastFiveTimeEntries(anyString())).thenReturn(timeEntryList);
        GetLastFiveTimeEntriesRequest request = new GetLastFiveTimeEntriesRequest();
        request.setEmployeeId(employeeId);

        GetLastFiveTimeEntriesResult result = JsonUtil.fromJson(lastFiveTimeEntriesHandler.handleRequest(request, null), GetLastFiveTimeEntriesResult.class);

        verify(timeEntryDao, never()).saveTimeEntries(anyList());
        assertTrue(result.isLastFiveTimeEntriesRetrieved());
        assertTrue(result.getTimeEntryList().size() == 5);
    }

    @Test
    public void requestHandler_throwsTimeEntriesNotFoundException() {
        when(timeEntryDao.getLastFiveTimeEntries(anyString())).thenThrow(new TimeEntriesNotFoundException("Time Entries Not Found."));
        GetLastFiveTimeEntriesRequest request = new GetLastFiveTimeEntriesRequest();
        request.setEmployeeId(employeeId);

        GetLastFiveTimeEntriesResult result = JsonUtil.fromJson(lastFiveTimeEntriesHandler.handleRequest(request, null), GetLastFiveTimeEntriesResult.class);

        verify(timeEntryDao, never()).saveTimeEntries(anyList());
        assertFalse(result.isLastFiveTimeEntriesRetrieved());
        assertTrue(result.getTimeEntryList().isEmpty());
        assertEquals("Time Entries Not Found.", result.getError());
    }

    @Test
    public void requestHandler_throwsException() {
        when(timeEntryDao.getLastFiveTimeEntries(anyString())).thenThrow(new RuntimeException("An unexpected error occurred"));
        GetLastFiveTimeEntriesRequest request = new GetLastFiveTimeEntriesRequest();
        request.setEmployeeId(employeeId);

        GetLastFiveTimeEntriesResult result = JsonUtil.fromJson(lastFiveTimeEntriesHandler.handleRequest(request, null), GetLastFiveTimeEntriesResult.class);

        verify(timeEntryDao, never()).saveTimeEntries(anyList());
        assertFalse(result.isLastFiveTimeEntriesRetrieved());
        assertTrue(result.getTimeEntryList().isEmpty());
        assertEquals("An unexpected error occurred", result.getError());
    }
}
