package org.example.model.requests;

import org.example.model.PermissionLevel;
import org.example.utils.ModelConverter;

import java.time.LocalDate;

/**
 * Represents a request to update an employee's information.
 */
public class UpdateEmployeeRequest {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String department;
    private LocalDate hireDate;
    private Boolean currentlyEmployed;
    private LocalDate terminatedDate;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String payRate;
    private PermissionLevel permissionAccess;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    /**
     * Takes in the employee's hire date formatted as a String and converts it to LocalDate before
     * assigning the value to the hireDate variable.
     * @param hireDate the date the employee was hired.
     */
    public void setHireDate(String hireDate) {
        if (hireDate != null) {
            this.hireDate = ModelConverter.convertStringToLocalDate(hireDate);
        }
    }

    public Boolean isCurrentlyEmployed() {
        return currentlyEmployed;
    }

    public void setCurrentlyEmployed(Boolean currentlyEmployed) {
        if (currentlyEmployed != null) {
            this.currentlyEmployed = currentlyEmployed;
        }
    }

    public LocalDate getTerminatedDate() {
        return terminatedDate;
    }

    /**
     * Takes in the employee's terminated date formatted as a String and converts it to LocalDate before
     * assigning the value to the terminatedDate variable.
     * @param terminatedDate the employee's termination date.
     */
    public void setTerminatedDate(String terminatedDate) {
        if (terminatedDate != null) {
            this.terminatedDate = ModelConverter.convertStringToLocalDate(terminatedDate);
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    public PermissionLevel getPermissionAccess() {
        return permissionAccess;
    }

    /**
     * Takes in a String of the employee's permission level, normalizes it to all uppercase and then converts
     * it to a {@link PermissionLevel} enum.
     * @param permissionAccess the permission access of the employee.
     */
    public void setPermissionAccess(String permissionAccess) {
        this.permissionAccess = PermissionLevel.valueOf(permissionAccess.toUpperCase());
    }

    @Override
    public String toString() {
        return "UpdateEmployeeRequest{" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nfirstName='" + firstName + '\'' +
                ", \nlastName='" + lastName + '\'' +
                ", \nmiddleName='" + (middleName != null ? middleName : "") + '\'' +
                ", \nemail='" + (email != null ? email : "") + '\'' +
                ", \ndepartment='" + (department != null ? department : "") + '\'' +
                ", \nhireDate=" + (hireDate != null ? hireDate : "") +
                ", \ncurrentlyEmployed=" + currentlyEmployed +
                ", \nterminatedDate=" + (terminatedDate != null ? terminatedDate : "") +
                ", \nphone='" + (phone != null ? phone : "") + '\'' +
                ", \naddress='" + (address != null ? address : "") + '\'' +
                ", \ncity='" + (city != null ? city : "") + '\'' +
                ", \nstate='" + (state != null ? state : "") + '\'' +
                ", \nzipCode='" + (zipCode != null ? zipCode : "") + '\'' +
                ", \npayRate='" + (payRate != null ? payRate : "") + '\'' +
                ", \npermissionAccess=" + (permissionAccess != null ? permissionAccess.name() : "") +
                "\n}";
    }
}
