package org.example.model.requests;

import org.example.model.PermissionLevel;

public class GetAllEmployeesRequest {
    private String employeeId;
    private PermissionLevel permissionLevel;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = PermissionLevel.valueOf(permissionLevel.toUpperCase());
    }
}
