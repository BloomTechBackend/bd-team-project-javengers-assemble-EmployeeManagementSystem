package org.example.lambda;

import org.example.dynamodb.TimeEntryDao;
import org.example.model.TimeEntry;
import org.example.model.requests.UpdateTimeEntryRequest;
import org.example.model.results.UpdateTimeEntryResult;
import org.example.utils.ModelConverter;
import org.example.utils.gson.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateTimeEntryHandlerTest {
    private AutoCloseable mocks;
    @Mock
    private TimeEntryDao timeEntryDao;
    @InjectMocks
    private UpdateTimeEntryHandler updateTimeEntryHandler;
    private TimeEntry timeEntry;
    private UpdateTimeEntryRequest updateTimeEntryRequest;

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);

        String employeeId = "Emp123";
        String entryId = "TE123";
        LocalDateTime timeIn = LocalDateTime.of(2024,06,05,8,30,31);

        timeEntry = TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withEntryId(entryId)
                .withTimeIn(timeIn)
                .build();

        updateTimeEntryRequest = new UpdateTimeEntryRequest();
        updateTimeEntryRequest.setEmployeeId(employeeId);
        updateTimeEntryRequest.setEntryId(entryId);
        updateTimeEntryRequest.setTimeIn(timeIn);
        updateTimeEntryRequest.setTimeOut(LocalDateTime.of(2024,06,05,4,30,15));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void requestHandler_createsNewTimeEntry() {
        // Given
        when(timeEntryDao.saveTimeEntry(any(TimeEntry.class))).thenReturn(ModelConverter.fromTimeEntry(timeEntry));

        // When
        UpdateTimeEntryResult result = JsonUtil.fromJson(updateTimeEntryHandler.handleRequest(updateTimeEntryRequest, null), UpdateTimeEntryResult.class);


        // Then
        verify(timeEntryDao).saveTimeEntry(any(TimeEntry.class));

        assertTrue(result.isTimeEntryUpdated(), "isNewTimeEntryCreated should return true.");
        assertEquals(timeEntry.getEmployeeId(), result.getEmployeeId(), "Employee IDs should match.");
        assertEquals(timeEntry.getEntryId(), result.getEntryId(), "Entry IDs should match.");
        assertEquals(timeEntry.getTimeIn(), ModelConverter.convertStringToLocalDateTime(result.getTimeIn()), "timeIn timestamps should match.");

    }

    @Test
    public void requestHandler_throwsUnexpectedException() {
        // Given
        when(timeEntryDao.saveTimeEntry(any(TimeEntry.class))).thenThrow(new RuntimeException("An unexpected error occurred."));

        // When
        UpdateTimeEntryResult result = JsonUtil.fromJson(updateTimeEntryHandler.handleRequest(updateTimeEntryRequest, null), UpdateTimeEntryResult.class);

        // Then
        assertFalse(result.isTimeEntryUpdated(), "isNewTimeEntryCreated should be false.");
        assertEquals(timeEntry.getEmployeeId(), result.getEmployeeId(), "Employee ID Should be included in the result an match the provided ID.");
        assertEquals("An unexpected error occurred.", result.getError());
    }

}
