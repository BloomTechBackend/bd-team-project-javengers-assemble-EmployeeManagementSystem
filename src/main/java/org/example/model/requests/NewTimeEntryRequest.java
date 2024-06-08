package org.example.model.requests;

import org.example.utils.ModelConverter;

import java.time.LocalDateTime;

public class NewTimeEntryRequest {
    private String employeeId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private double duration;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
}
