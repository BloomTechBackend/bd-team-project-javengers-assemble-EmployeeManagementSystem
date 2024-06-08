package org.example.model.results;

import org.example.model.TimeEntry;

import java.util.ArrayList;
import java.util.List;

public class GetLastFiveTimeEntriesResult implements Result{
    private final boolean lastFiveTimeEntriesRetrieved;
    private final List<TimeEntry> timeEntryList;
    private final String error;

    private GetLastFiveTimeEntriesResult(Builder builder) {
        this.lastFiveTimeEntriesRetrieved = builder.lastFiveTimeEntriesRetrieved;
        this.timeEntryList = builder.timeEntryList;
        this.error = builder.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean lastFiveTimeEntriesRetrieved;
        private List<TimeEntry> timeEntryList = new ArrayList<>();
        private String error;

        public Builder() {

        }

        public Builder withLastFiveTimeEntriesRetrieved(boolean lastFiveTimeEntriesRetrieved) {
            this.lastFiveTimeEntriesRetrieved = lastFiveTimeEntriesRetrieved;
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

        public GetLastFiveTimeEntriesResult build() {
            return new GetLastFiveTimeEntriesResult(this);
        }

    }

    public boolean isLastFiveTimeEntriesRetrieved() {
        return lastFiveTimeEntriesRetrieved;
    }

    public List<TimeEntry> getTimeEntryList() {
        return timeEntryList;
    }

    public String getError() {
        return error;
    }
}
