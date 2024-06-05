package org.example.model.requests;

import org.example.utils.ModelConverter;

import java.time.LocalDateTime;

public class UpdateCredentialsRequest {
    private String employeeId;
    private String username;
    private String password;
    private LocalDateTime lastUpdated;
    private boolean accountLocked;
    private boolean forceChangeAfterLogin;
    private int failedAttempts;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = ModelConverter.convertStringToLocalDateTime(lastUpdated);
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isForceChangeAfterLogin() {
        return forceChangeAfterLogin;
    }

    public void setForceChangeAfterLogin(boolean forceChangeAfterLogin) {
        this.forceChangeAfterLogin = forceChangeAfterLogin;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}
