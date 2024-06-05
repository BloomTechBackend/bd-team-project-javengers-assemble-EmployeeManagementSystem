package org.example.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "employee_credentials")
public class EmployeeCredentialsModel {
    private String employeeId;
    private String username;
    private String salt;
    private String password;
    private String lastUpdated;
    private boolean accountLocked;
    private boolean forceChangeAfterLogin;
    private int failedAttempts;

    @DynamoDBAttribute(attributeName = "employeeId")
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBAttribute(attributeName = "lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @DynamoDBAttribute(attributeName = "accountLocked")
    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    @DynamoDBAttribute(attributeName = "forceChangeAfterLogin")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeCredentialsModel)) return false;
        EmployeeCredentialsModel that = (EmployeeCredentialsModel) o;
        return isAccountLocked() == that.isAccountLocked() &&
                isForceChangeAfterLogin() == that.isForceChangeAfterLogin() &&
                getFailedAttempts() == that.getFailedAttempts() &&
                Objects.equals(getEmployeeId(), that.getEmployeeId()) &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getSalt(), that.getSalt()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getLastUpdated(), that.getLastUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getUsername(), getSalt(),
                getPassword(), getLastUpdated(), isAccountLocked(),
                isForceChangeAfterLogin(), getFailedAttempts());
    }

    @Override
    public String toString() {
        return "EmployeeCredentialsModel{" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nusername='" + username + '\'' +
                ", \nlastUpdated='" + lastUpdated + '\'' +
                ", \naccountLocked=" + accountLocked +
                ", \nforceChangeAfterLogin=" + forceChangeAfterLogin +
                ", \nfailedAttempts=" + failedAttempts +
                "\n}";
    }
}
