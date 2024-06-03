package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TimeEntryTest {

    private TimeEntry timeEntryWithTimeOut;
    private TimeEntry timeEntryWithoutTimeOut;
    private final String employeeId = "employee1";
    private final LocalDateTime timeIn = LocalDateTime.of(2024, 5, 28, 9, 0);
    private final LocalDateTime timeOut = LocalDateTime.of(2024, 5, 28, 17, 30);

    @BeforeEach
    public void setUp() {
        timeEntryWithoutTimeOut = TimeEntry.builder()
                .withEmployeeId(employeeId)
                .build();

        timeEntryWithTimeOut = TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withTimeIn(timeIn)
                .withTimeOut(timeOut)
                .build();
    }

    @Test
    public void constructor_withTimeOut_initializesFieldsCorrectly() {
        assertEquals(employeeId, timeEntryWithTimeOut.getEmployeeId());
        assertNotNull(timeEntryWithTimeOut.getEntryId());
        assertEquals(timeIn, timeEntryWithTimeOut.getTimeIn());
        assertEquals(timeOut, timeEntryWithTimeOut.getTimeOut());
        assertEquals(8.5, timeEntryWithTimeOut.getDuration(), 0.01);
    }

    @Test
    public void constructor_withoutTimeOut_initializesFieldsCorrectly() {
        assertEquals(employeeId, timeEntryWithoutTimeOut.getEmployeeId());
        assertNotNull(timeEntryWithoutTimeOut.getEntryId());
        assertNotNull(timeEntryWithoutTimeOut.getTimeIn());
        assertEquals(0, timeEntryWithoutTimeOut.getDuration(), 0.01);
    }

    @Test
    public void calculateDuration_withTimeOut_calculatesCorrectDuration() {
        assertEquals(8.5, timeEntryWithTimeOut.calculateDuration(), 0.01);
    }

    @Test
    public void calculateDuration_withoutTimeOut_calculatesCorrectDuration() {
        timeEntryWithoutTimeOut.setTimeIn(LocalDateTime.now().minusMinutes(525));
        double duration = timeEntryWithoutTimeOut.calculateDuration();
        assertEquals(0.0, duration, 0.01);
    }

    @Test
    public void setTimeOut_updatesTimeOutAndDuration() {
        timeEntryWithoutTimeOut.setTimeIn(timeIn); // Set initial timeIn
        timeEntryWithoutTimeOut.setTimeOut(timeOut); // Set timeOut and recalculate duration
        assertEquals(timeOut, timeEntryWithoutTimeOut.getTimeOut());
        assertEquals(8.5, timeEntryWithoutTimeOut.getDuration(), 0.01); // Note: corrected expected duration
    }

    @Test
    public void setTimeIn_updatesTimeIn() {
        LocalDateTime newTimeIn = LocalDateTime.of(2024, 5, 28, 8, 0);
        timeEntryWithoutTimeOut.setTimeIn(newTimeIn);
        assertEquals(newTimeIn, timeEntryWithoutTimeOut.getTimeIn());
    }

    @Test
    public void getEmployeeId_returnsCorrectEmployeeId() {
        assertEquals(employeeId, timeEntryWithTimeOut.getEmployeeId());
    }

    @Test
    public void getEntryId_returnsNonNullEntryId() {
        assertNotNull(timeEntryWithTimeOut.getEntryId());
    }

    @Test
    public void getTimeIn_returnsCorrectTimeIn() {
        assertEquals(timeIn, timeEntryWithTimeOut.getTimeIn());
    }

    @Test
    public void getTimeOut_returnsCorrectTimeOut() {
        assertEquals(timeOut, timeEntryWithTimeOut.getTimeOut());
    }

    @Test
    public void getDuration_returnsCorrectDuration() {
        assertEquals(8.5, timeEntryWithTimeOut.getDuration(), 0.01);
    }
}
