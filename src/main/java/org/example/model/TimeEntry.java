package org.example.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class TimeEntry {
    private final String employeeId;
    private final String entryId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private double duration;

    /**
     * Creates a new TimeEntry for an employee when they first clock in.
     * An entry ID is auto-generated and the current timestamp is recorded as the timeIn.
     * The initial duration is set to 0.
     *
     * @param employeeId the ID of the employee clocking in.
     */
    public TimeEntry(String employeeId) {
        this.employeeId = employeeId;
        this.entryId = UUID.randomUUID().toString();
        this.timeIn = getCurrentTimeStamp();
        this.duration = 0;
    }

    /**
     * Creates a new TimeEntry for an employee with specified clock-in and clock-out times.
     * This constructor is primarily used for administrative purposes when a manual time entry is needed.
     * An entry ID is auto-generated and the provided timeIn and timeOut timestamps are recorded.
     * The duration between the timestamps is automatically calculated.
     *
     * @param employeeId the ID of the employee to whom the time entry belongs.
     * @param timeIn the date and time the employee clocked in.
     * @param timeOut the date and time the employee clocked out.
     */
    public TimeEntry(String employeeId, LocalDateTime timeIn, LocalDateTime timeOut) {
        this.employeeId = employeeId;
        this.entryId = UUID.randomUUID().toString();
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        calculateDuration();
    }

    /**
     * Calculates the duration between the time an employee clocked in and either the current time or the time they clocked out.
     * If the employee has not clocked out, the duration is calculated from the clock-in time to the current time.
     * If the employee has clocked out, the duration is calculated from the clock-in time to the clock-out time.
     * The duration is returned in hours as a double, including fractional hours.
     *
     * @return the duration between timeIn and timeOut (or the current time if timeOut is null) in hours.
     */
    public double calculateDuration() {
        if (timeOut == null) {
            duration = Duration.between(timeIn, getCurrentTimeStamp()).toMinutes() / 60.0;
        } else {
            duration = Duration.between(timeIn, timeOut).toMinutes() / 60.0;
        }
        return duration;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEntryId() {
        return entryId;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
        calculateDuration();
    }

    public double getDuration() {
        return duration;
    }

    private LocalDateTime getCurrentTimeStamp() {
        return LocalDateTime.now();
    }
}
