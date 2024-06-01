package org.example.model;


import java.util.ArrayList;
import java.util.List;

public class TimeEntries {
    List<TimeEntry> timeEntryList = new ArrayList<>();

    public TimeEntries() {
    }

    public TimeEntries(List<TimeEntry> timeEntryList) {
        this.timeEntryList = timeEntryList;
    }

    public void addTimeEntry(TimeEntry timeEntry) {
        this.timeEntryList.add(timeEntry);
    }

    public List<TimeEntry> getTimeEntryList() {
        return timeEntryList;
    }

    
}
