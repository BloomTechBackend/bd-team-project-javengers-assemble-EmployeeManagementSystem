package org.example.model.results;

public class NewTimeEntryResult implements Result {
    private final boolean newTimeEntryCreated;
    private final String employeeId;
    private final String entryId;
    private final String timeIn;
    private final String error;

    private NewTimeEntryResult(Builder builder) {
        this.newTimeEntryCreated = builder.newTimeEntryCreated;
        this.employeeId = builder.employeeId;
        this.entryId = builder.entryId;
        this.timeIn = builder.timeIn;
        this.error = builder.error;

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean newTimeEntryCreated;
        private String employeeId;
        private String entryId;
        private String timeIn;

        private String error;

        public Builder() {

        }

        public Builder withNewTimeEntryCreated(boolean newTimeEntryCreated) {
            this.newTimeEntryCreated = newTimeEntryCreated;
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

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public NewTimeEntryResult build() {
            return new NewTimeEntryResult(this);
        }

    }

    public boolean isNewTimeEntryCreated() {
        return newTimeEntryCreated;
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

    public String getError() {
        return error;
    }
}
