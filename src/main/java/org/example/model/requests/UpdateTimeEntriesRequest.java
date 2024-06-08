package org.example.model.requests;

import org.example.model.TimeEntry;

import java.util.ArrayList;
import java.util.List;

public class UpdateTimeEntriesRequest {
    private String employeeId;
    private List<TimeEntry> timeEntryList = new ArrayList<>();

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<TimeEntry> getTimeEntryList() {
        return timeEntryList;
    }

    public void setTimeEntryList(List<TimeEntry> timeEntryList) {
        this.timeEntryList = timeEntryList;
    }
}
