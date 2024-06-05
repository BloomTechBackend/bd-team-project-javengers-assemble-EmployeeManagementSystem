package org.example.model.requests;

public class AdminResetPasswordRequest {
    private String employeeId;
    private String username;
    private String password;

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

    @Override
    public String toString() {
        return "AdminResetPasswordRequest{\n" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nusername='" + username + '\'' +
                "\n}";
    }
}
