package org.example.model;

import org.example.exceptions.AccountLockedException;
import org.example.utils.CredentialsUtility;

import java.time.LocalDateTime;
import java.util.Objects;

public class EmployeeCredentials {
    private final String employeeId;
    private String username;
    private String salt;
    private String password;
    private LocalDateTime lastUpdated;
    private boolean accountLocked;
    private boolean forceChangeAfterLogin;
    private int failedAttempts = 0;

    /**
     * This constructor is used when a new {@link Employee} is added to the database, and temporary credentials are
     * created. The employee will be prompted to change their password after logging in for the first time.
     *
     * @param employeeId the employee ID of the employee
     * @param username the username of the employee
     * @param password the temporary password of the employee
     */
    public EmployeeCredentials(String employeeId, String username, String password) {
        this.employeeId = employeeId;
        this.username = username;
        this.salt = CredentialsUtility.generateSalt();
        this.password = CredentialsUtility.hashPassword(password, salt);
        this.lastUpdated = LocalDateTime.now();
        this.accountLocked = false;
        this.forceChangeAfterLogin = true;
    }

    /**
     * This constructor is used for existing {@link Employee} credentials.
     *
     * @param employeeId the employee ID of the employee
     * @param username the username of the employee
     * @param salt the salt associated with the employee's account
     * @param password the employee's hashed password with salt
     * @param lastUpdated the date and time when the password was last updated
     * @param accountLocked a flag indicating whether the account is locked
     * @param forceChangeAfterLogin a flag indicating if the employee must change their password after logging in
     * @param failedAttempts the number of failed login attempts
     */
    public EmployeeCredentials(String employeeId, String username, String salt, String password,
                               LocalDateTime lastUpdated, boolean accountLocked,
                               boolean forceChangeAfterLogin, int failedAttempts) {
        this.employeeId = employeeId;
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.lastUpdated = lastUpdated;
        this.accountLocked = accountLocked;
        this.forceChangeAfterLogin = forceChangeAfterLogin;
        this.failedAttempts = failedAttempts;
    }

    /**
     * Updates the employee's password. Generates a new salt and updates the lastUpdated timestamp.
     *
     * @param password the employee's new password
     */
    public void updatePassword(String password) {
        this.salt = CredentialsUtility.generateSalt();
        this.password = CredentialsUtility.hashPassword(password, this.salt);
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Verifies a user's credentials by checking the provided password against the stored hashed password and salt.
     * Increments the failed attempts counter if the verification fails and locks the account after 3 failed attempts.
     *
     * @param password the password to verify
     * @return true if the credentials are valid, false otherwise
     * @throws AccountLockedException if the account is locked due to too many failed login attempts
     */
    public boolean verifyCredentials(String password) {
        if (failedAttempts > 3) {
            this.accountLocked = true;
            throw new AccountLockedException("Account is locked due to too many failed login attempts.");
        }

        boolean result = CredentialsUtility.verifyPassword(password, this.salt, this.password);

        if (!result) {
            this.failedAttempts++;
        } else {
            this.failedAttempts = 0;
        }
        return result;
    }

    /**
     * Resets an employee's password with a temporary password set by an admin. The employee
     * will be prompted to change their password after logging in.
     *
     * @param password the temporary password set by the admin
     */
    public void adminResetPassword(String password) {
        this.salt = CredentialsUtility.generateSalt();
        this.password = CredentialsUtility.hashPassword(password, this.salt);
        this.lastUpdated = LocalDateTime.now();
        this.accountLocked = false;
        this.forceChangeAfterLogin = true;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
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

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeCredentials)) return false;
        EmployeeCredentials that = (EmployeeCredentials) o;
        return isAccountLocked() == that.isAccountLocked() &&
                isForceChangeAfterLogin() == that.isForceChangeAfterLogin() &&
                getFailedAttempts() == that.getFailedAttempts() &&
                Objects.equals(getEmployeeId(), that.getEmployeeId()) &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(salt, that.salt) && Objects.equals(password, that.password) &&
                Objects.equals(getLastUpdated(), that.getLastUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getUsername(), salt, password,
                getLastUpdated(), isAccountLocked(), isForceChangeAfterLogin(),
                getFailedAttempts());
    }

    @Override
    public String toString() {
        return "EmployeeCredentials{" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nusername='" + username + '\'' +
                ", \nlastUpdated=" + lastUpdated +
                ", \naccountLocked=" + accountLocked +
                ", \nforceChangeAfterLogin=" + forceChangeAfterLogin +
                ", \nfailedAttempts=" + failedAttempts +
                "\n}";
    }
}
