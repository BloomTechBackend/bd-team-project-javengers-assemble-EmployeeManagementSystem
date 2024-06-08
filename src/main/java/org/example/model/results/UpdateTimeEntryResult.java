package org.example.model.results;

public class UpdateTimeEntryResult implements Result {
    private final boolean timeEntryUpdated;
    private final String employeeId;
    private final String entryId;
    private final String timeIn;
    private final String timeOut;
    private final double duration;
    private final String error;

    private UpdateTimeEntryResult(Builder build) {
        this.timeEntryUpdated = build.timeEntryUpdated;
        this.employeeId = build.employeeId;
        this.entryId = build.entryId;
        this.timeIn = build.timeIn;
        this.timeOut = build.timeOut;
        this.duration = build.duration;
        this.error = build.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean timeEntryUpdated;
        private String employeeId;
        private String entryId;
        private String timeIn;
        private String timeOut;
        private double duration;
        private String error;

        public Builder() {

        }

        public Builder withTimeUpdated(boolean timeEntryUpdated) {
            this.timeEntryUpdated = timeEntryUpdated;
            return this;
        }

        public Builder withEmployeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder withEntryId(String entryId) {
            this.entryId = entryId;
            return this;
        }

        public Builder withTimeIn(String timeIn) {
            this.timeIn = timeIn;
            return this;
        }

        public Builder withTimeOut(String timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder withDuration(double duration) {
            this.duration = duration;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public UpdateTimeEntryResult build() {
            return new UpdateTimeEntryResult(this);
        }

    }

    public boolean isTimeEntryUpdated() {
        return timeEntryUpdated;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public double getDuration() {
        return duration;
    }

    public String getError() {
        return error;
    }
}
