package org.example.model.results;

public class UpdateCredentialsResult implements Result {
    private final boolean credentialsUpdated;
    private final String employeeId;
    private final String username;
    private final String lastUpdated;
    private final boolean accountLocked;
    private final boolean forceChangeAfterLogin;
    private final int failedAttempts;
    private final String error;

    private UpdateCredentialsResult(Builder builder) {
        this.credentialsUpdated = builder.credentialsUpdated;
        this.employeeId = builder.employeeId;
        this.username = builder.username;
        this.lastUpdated = builder.lastUpdated;
        this.accountLocked = builder.accountLocked;
        this.forceChangeAfterLogin = builder.forceChangeAfterLogin;
        this.failedAttempts = builder().failedAttempts;
        this.error = builder.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean credentialsUpdated;
        private String employeeId;
        private String username;
        private String lastUpdated;
        private boolean accountLocked;
        private boolean forceChangeAfterLogin;
        private int failedAttempts;

        private String error;

        public Builder withCredentialsUpdated(boolean credentialsUpdated) {
            this.credentialsUpdated = credentialsUpdated;
            return this;
        }

        public Builder withEmployeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder withAccountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public Builder withForceChangeAfterLogin(boolean forceChangeAfterLogin) {
            this.forceChangeAfterLogin = forceChangeAfterLogin;
            return this;
        }

        public Builder withFailedAttempts(int failedAttempts) {
            this.failedAttempts = failedAttempts;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public UpdateCredentialsResult build() {
            return new UpdateCredentialsResult(this);
        }

    }

    public boolean isCredentialsUpdated() {
        return credentialsUpdated;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getUsername() {
        return username;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public boolean isForceChangeAfterLogin() {
        return forceChangeAfterLogin;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public String getError() {
        return error;
    }
}
