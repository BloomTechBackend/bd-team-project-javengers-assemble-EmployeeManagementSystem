package org.example.model.results;

import java.time.LocalDateTime;

public class AdminResetPasswordResult {
    private boolean employeeCredentialsReset;
    private String employeeId;
    private String username;
    private String lastUpdated;
    private boolean accountLocked;
    private boolean forceChangeAfterLogin;
    private String error;

    private AdminResetPasswordResult(Builder builder) {
        this.employeeCredentialsReset = builder.employeeCredentialsReset;
        this.employeeId = builder.employeeId;
        this.username = builder.username;
        this.lastUpdated = builder.lastUpdated != null ? builder.lastUpdated.toString() : null;
        this.accountLocked = builder.accountLocked;
        this.forceChangeAfterLogin = builder.forceChangeAfterLogin;
        this.error = builder.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean employeeCredentialsReset;
        private String employeeId;
        private String username;
        private LocalDateTime lastUpdated;
        private boolean accountLocked;
        private boolean forceChangeAfterLogin;
        private String error;

        public Builder() {

        }

        public Builder withEmployeeCredentialsReset(boolean employeeCredentialsReset) {
            this.employeeCredentialsReset = employeeCredentialsReset;
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

        public Builder withLastUpdated(LocalDateTime lastUpdated) {
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

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public AdminResetPasswordResult build() {
            return new AdminResetPasswordResult(this);
        }

    }

    public boolean isEmployeeCredentialsReset() {
        return employeeCredentialsReset;
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

    public String getError() {
        return error;
    }
}
