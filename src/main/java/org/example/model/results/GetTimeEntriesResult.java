package org.example.model.results;

import org.example.model.TimeEntry;

import java.util.ArrayList;
import java.util.List;

public class GetTimeEntriesResult implements Result {
    private final boolean timeEntriesRetrieved;
    private final List<TimeEntry> timeEntryList;
    private final String error;

    private GetTimeEntriesResult(Builder builder) {
        this.timeEntriesRetrieved = builder.timeEntriesRetrieved;
        this.timeEntryList = builder.timeEntryList;
        this.error = builder.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean timeEntriesRetrieved;
        private List<TimeEntry> timeEntryList = new ArrayList<>();
        private String error;

        public Builder() {

        }

        public Builder withTimeEntriesRetrieved(boolean timeEntriesRetrieved) {
            this.timeEntriesRetrieved = timeEntriesRetrieved;
            return this;
        }

        public Builder withTimeEntryList(List<TimeEntry> timeEntryList) {
            this.timeEntryList = timeEntryList;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public GetTimeEntriesResult build() {
            return new GetTimeEntriesResult(this);
        }

    }

    public boolean isTimeEntriesRetrieved() {
        return timeEntriesRetrieved;
    }

    public List<TimeEntry> getTimeEntryList() {
        return timeEntryList;
    }

    public String getError() {
        return error;
    }

}
