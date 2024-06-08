package org.example.lambda;

import org.example.dynamodb.TimeEntryDao;
import org.example.model.TimeEntry;
import org.example.model.requests.NewTimeEntryRequest;
import org.example.model.results.NewTimeEntryResult;
import org.example.utils.ModelConverter;
import org.example.utils.gson.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class NewTimeEntryHandlerTest {
    private AutoCloseable mocks;
    @Mock
    private TimeEntryDao timeEntryDao;
    @InjectMocks
    private NewTimeEntryHandler timeEntryHandler;
    private TimeEntry timeEntry;
    private NewTimeEntryRequest timeEntryRequest;

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

        timeEntryRequest = new NewTimeEntryRequest();
        timeEntryRequest.setEmployeeId(employeeId);
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
        NewTimeEntryResult result = JsonUtil.fromJson(timeEntryHandler.handleRequest(timeEntryRequest, null), NewTimeEntryResult.class);

        // Then
        verify(timeEntryDao).saveTimeEntry(any(TimeEntry.class));

        assertTrue(result.isNewTimeEntryCreated(), "isNewTimeEntryCreated should return true.");
        assertEquals(timeEntry.getEmployeeId(), result.getEmployeeId(), "Employee IDs should match.");
        assertEquals(timeEntry.getEntryId(), result.getEntryId(), "Entry IDs should match.");
        assertEquals(timeEntry.getTimeIn(), ModelConverter.convertStringToLocalDateTime(result.getTimeIn()), "timeIn timestamps should match.");

    }

    @Test
    public void requestHandler_throwsUnexpectedException() {
        // Given
        when(timeEntryDao.saveTimeEntry(any(TimeEntry.class))).thenThrow(new RuntimeException("An unexpected error occurred."));

        // When
        NewTimeEntryResult result = JsonUtil.fromJson(timeEntryHandler.handleRequest(timeEntryRequest, null), NewTimeEntryResult.class);

        // Then
        assertFalse(result.isNewTimeEntryCreated(), "isNewTimeEntryCreated should be false.");
        assertEquals(timeEntry.getEmployeeId(), result.getEmployeeId(), "Employee ID Should be included in the result an match the provided ID.");
        assertEquals("An unexpected error occurred.", result.getError());
    }


}
