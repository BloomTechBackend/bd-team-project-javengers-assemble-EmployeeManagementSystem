package org.example.model.results;

import org.example.dynamodb.model.TimeEntryModel;
import org.example.model.TimeEntry;

import java.util.ArrayList;
import java.util.List;

public class UpdateTimeEntriesResult implements Result {
    private final boolean timeEntriesUpdated;
    private final String employeeId;
    private final List<TimeEntryModel> timeEntryList;
    private final String error;

    private UpdateTimeEntriesResult(Builder builder) {
        this.timeEntriesUpdated = builder.timeEntriesUpdated;
        this.employeeId = builder.employeeId;
        this.timeEntryList = builder.timeEntryList;
        this.error = builder.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean timeEntriesUpdated;
        private String employeeId;
        private List<TimeEntryModel> timeEntryList = new ArrayList<>();
        private String error;

        public Builder() {

        }

        public Builder withTimeEntriesUpdated(boolean timeEntriesUpdated) {
            this.timeEntriesUpdated = timeEntriesUpdated;
            return this;
        }

        public Builder withEmployeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder withTimeEntryList(List<TimeEntryModel> timeEntryList) {
            this.timeEntryList = timeEntryList;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public UpdateTimeEntriesResult build() {
            return new UpdateTimeEntriesResult(this);
        }

    }

    public boolean isTimeEntriesUpdated() {
        return timeEntriesUpdated;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public List<TimeEntryModel> getTimeEntryList() {
        return timeEntryList;
    }

    public String getError() {
        return error;
    }
}
