package org.example.model.requests;

import org.example.utils.ModelConverter;

import java.time.LocalDateTime;

public class UpdateTimeEntryRequest {
    private String employeeId;
    private String entryId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private double duration;
    private boolean employeeClockOut = false;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = ModelConverter.convertStringToLocalDateTime(timeIn);
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = ModelConverter.convertStringToLocalDateTime(timeOut);
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isEmployeeClockOut() {
        return employeeClockOut;
    }

    public void setEmployeeClockOut(boolean employeeClockOut) {
        this.employeeClockOut = employeeClockOut;
    }
}
