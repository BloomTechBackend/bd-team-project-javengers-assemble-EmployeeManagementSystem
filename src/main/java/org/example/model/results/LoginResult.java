package org.example.model.results;

public class LoginResult implements Result {
    private final boolean loginSuccess;
    private final String username;
    private final String employeeId;
    private final boolean accountLocked;
    private final boolean forceChangeAfterLogin;
    private final String error;

    public LoginResult(Builder builder) {
        this.loginSuccess = builder.loginSuccess;
        this.username = builder.username;
        this.employeeId = builder.employeeId;
        this.accountLocked = builder.accountLocked;
        this.forceChangeAfterLogin = builder.forceChangeAfterLogin;
        this.error = builder.error;
    }

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {
        private boolean loginSuccess;
        private String username;
        private String employeeId;
        private boolean accountLocked;
        private boolean forceChangeAfterLogin;
        private String error;

        public Builder() {

        }

        public Builder withLoginSuccess(boolean loginSuccess) {
            this.loginSuccess = loginSuccess;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withEmployeeId(String employeeId) {
            this.employeeId = employeeId;
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

        public LoginResult build() {
            return new LoginResult(this);
        }

    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public String getUsername() {
        return username;
    }

    public String getEmployeeId() {
        return employeeId;
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
