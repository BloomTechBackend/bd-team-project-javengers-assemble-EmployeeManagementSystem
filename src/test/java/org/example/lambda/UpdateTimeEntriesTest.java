package org.example.lambda;

import org.example.dynamodb.TimeEntryDao;
import org.example.model.TimeEntry;
import org.example.model.requests.UpdateTimeEntriesRequest;
import org.example.model.results.NewTimeEntryResult;
import org.example.model.results.UpdateTimeEntriesResult;
import org.example.utils.ModelConverter;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateTimeEntriesTest {
    private AutoCloseable mocks;
    @Mock
    private TimeEntryDao timeEntryDao;
    @InjectMocks
    private UpdateTimeEntriesHandler updateTimeEntriesHandler;

    private List<TimeEntry> timeEntryList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);

        String employeeId = "Emp123";

        timeEntryList.add(
          TimeEntry.builder()
                  .withEmployeeId(employeeId)
                  .withEntryId("TE123")
                  .withTimeIn(LocalDateTime.of(2024,5,25,8,30,30))
                  .withTimeOut(LocalDateTime.of(2024,5,25,17,30))
                  .withDuration(9.0)
                  .build()
        );
        timeEntryList.add(
                TimeEntry.builder()
                        .withEmployeeId(employeeId)
                        .withEntryId("TE124")
                        .withTimeIn(LocalDateTime.of(2024,5,26,8,30,30))
                        .withTimeOut(LocalDateTime.of(2024,5,26,17,30))
                        .withDuration(9.0)
                        .build()
        );
        timeEntryList.add(
                TimeEntry.builder()
                        .withEmployeeId(employeeId)
                        .withEntryId("TE123")
                        .withTimeIn(LocalDateTime.of(2024,5,27,8,30,30))
                        .build()
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void handleRequest_updatesListOfTimeEntries() {
        // Given
        List<TimeEntry> originalList = new ArrayList<>(timeEntryList);
        when(timeEntryDao.saveTimeEntries(anyList())).thenReturn(ModelConverter.fromTimeEntryList(timeEntryList));

        // When
        timeEntryList.get(2).setTimeOut(LocalDateTime.of(2024,5,26,17,30));
        UpdateTimeEntriesRequest request = new UpdateTimeEntriesRequest();
        request.setTimeEntryList(timeEntryList);

        UpdateTimeEntriesResult result = JsonUtil.fromJson(updateTimeEntriesHandler.handleRequest(request, null), UpdateTimeEntriesResult.class);
        List<TimeEntry> updatedList = ModelConverter.fromTimeEntryModelList(result.getTimeEntryList());
        // Then
        verify(timeEntryDao).saveTimeEntries(anyList());

        assertTrue(result.isTimeEntriesUpdated());
        assertNotEquals(updatedList, originalList);
    }

    @Test
    public void requestHandler_throwsUnexpectedException() {
        // Given
        when(timeEntryDao.saveTimeEntries(anyList())).thenThrow(new RuntimeException("An unexpected error occurred."));

        // When
        timeEntryList.get(2).setTimeOut(LocalDateTime.of(2024,5,26,17,30));
        UpdateTimeEntriesRequest request = new UpdateTimeEntriesRequest();
        request.setTimeEntryList(timeEntryList);
        UpdateTimeEntriesResult result = JsonUtil.fromJson(updateTimeEntriesHandler.handleRequest(request, null), UpdateTimeEntriesResult.class);

        // Then
        assertFalse(result.isTimeEntriesUpdated(), "isNewTimeEntryCreated should be false.");
        assertEquals("An unexpected error occurred.", result.getError());
    }

}
