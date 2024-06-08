package org.example.lambda;

import org.example.dynamodb.TimeEntryDao;
import org.example.exceptions.TimeEntriesNotFoundException;
import org.example.model.TimeEntry;
import org.example.model.requests.GetTimeEntriesRequest;
import org.example.model.results.GetTimeEntriesResult;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetTimeEntriesTest {
    private AutoCloseable mocks;
    @Mock
    private TimeEntryDao timeEntryDao;
    @InjectMocks
    private GetTimeEntriesHandler timeEntriesHandler;

    private List<TimeEntry> timeEntryList = new ArrayList<>();
    private final String employeeId = "Emp123";

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);

        timeEntryList.add(
                TimeEntry.builder()
                        .withEmployeeId(employeeId)
                        .withEntryId("TE124")
                        .withTimeIn(LocalDateTime.of(2024, 5, 28, 9, 0))
                        .withTimeOut(LocalDateTime.of(2024, 5, 28, 17, 0))
                        .withDuration(8.0)
                        .build()
        );

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
    public void requestHandler_retrievesTimeEntries() {
        when(timeEntryDao.getTimeEntries(anyString())).thenReturn(timeEntryList);
        GetTimeEntriesRequest request = new GetTimeEntriesRequest();
        request.setEmployeeId(employeeId);

        GetTimeEntriesResult result = JsonUtil.fromJson(timeEntriesHandler.handleRequest(request, null), GetTimeEntriesResult.class);

        verify(timeEntryDao, never()).saveTimeEntries(anyList());
        assertTrue(result.isTimeEntriesRetrieved());
        assertEquals(6, result.getTimeEntryList().size());
    }

    @Test
    public void requestHandler_throwsTimeEntriesNotFoundException() {
        when(timeEntryDao.getTimeEntries(anyString())).thenThrow(new TimeEntriesNotFoundException("Time Entries Not Found."));
        GetTimeEntriesRequest request = new GetTimeEntriesRequest();
        request.setEmployeeId(employeeId);

        GetTimeEntriesResult result = JsonUtil.fromJson(timeEntriesHandler.handleRequest(request, null), GetTimeEntriesResult.class);

        verify(timeEntryDao, never()).saveTimeEntries(anyList());
        assertFalse(result.isTimeEntriesRetrieved());
        assertTrue(result.getTimeEntryList().isEmpty());
        assertEquals("Time Entries Not Found.", result.getError());
    }

    @Test
    public void requestHandler_throwsException() {
        when(timeEntryDao.getTimeEntries(anyString())).thenThrow(new RuntimeException("An unexpected error occurred"));
        GetTimeEntriesRequest request = new GetTimeEntriesRequest();
        request.setEmployeeId(employeeId);

        GetTimeEntriesResult result = JsonUtil.fromJson(timeEntriesHandler.handleRequest(request, null), GetTimeEntriesResult.class);

        verify(timeEntryDao, never()).saveTimeEntries(anyList());
        assertFalse(result.isTimeEntriesRetrieved());
        assertTrue(result.getTimeEntryList().isEmpty());
        assertEquals("An unexpected error occurred", result.getError());
    }
}
