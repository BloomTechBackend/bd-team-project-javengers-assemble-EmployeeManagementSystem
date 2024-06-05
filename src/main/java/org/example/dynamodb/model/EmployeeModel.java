package org.example.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.Objects;

@DynamoDBTable(tableName = "employee")
public class EmployeeModel {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String department;
    private String hireDate;
    private boolean currentlyEmployed;
    private String terminatedDate;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String payRate;
    private String permissionAccess;

    @DynamoDBHashKey(attributeName = "employee_id")
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @DynamoDBAttribute(attributeName = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @DynamoDBRangeKey(attributeName = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDBAttribute(attributeName = "middle_name")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "department")
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @DynamoDBAttribute(attributeName = "hire_date")
    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    @DynamoDBAttribute(attributeName = "currently_employed")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
    public boolean isCurrentlyEmployed() {
        return currentlyEmployed;
    }

    public void setCurrentlyEmployed(boolean currentlyEmployed) {
        this.currentlyEmployed = currentlyEmployed;
    }

    @DynamoDBAttribute(attributeName = "terminated_date")
    public String getTerminatedDate() {
        return terminatedDate;
    }

    public void setTerminatedDate(String terminatedDate) {
        this.terminatedDate = terminatedDate;
    }

    @DynamoDBAttribute(attributeName = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @DynamoDBAttribute(attributeName = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute(attributeName = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @DynamoDBAttribute(attributeName = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @DynamoDBAttribute(attributeName = "zip_code")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @DynamoDBAttribute(attributeName = "pay_rate")
    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    @DynamoDBAttribute(attributeName = "permission_access")
    public String getPermissionAccess() {
        return permissionAccess;
    }

    public void setPermissionAccess(String permissionAccess) {
        this.permissionAccess = permissionAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeModel)) return false;
        EmployeeModel that = (EmployeeModel) o;
        return isCurrentlyEmployed() == that.isCurrentlyEmployed() &&
                Objects.equals(getEmployeeId(), that.getEmployeeId()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getMiddleName(), that.getMiddleName()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getDepartment(), that.getDepartment()) &&
                Objects.equals(getHireDate(), that.getHireDate()) &&
                Objects.equals(getTerminatedDate(), that.getTerminatedDate()) &&
                Objects.equals(getPhone(), that.getPhone()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getState(), that.getState()) &&
                Objects.equals(getZipCode(), that.getZipCode()) &&
                Objects.equals(getPayRate(), that.getPayRate()) &&
                Objects.equals(getPermissionAccess(), that.getPermissionAccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getFirstName(),
                getLastName(), getMiddleName(), getEmail(),
                getDepartment(), getHireDate(), isCurrentlyEmployed(),
                getTerminatedDate(), getPhone(), getAddress(), getCity(),
                getState(), getZipCode(), getPayRate(), getPermissionAccess());
    }

    @Override
    public String toString() {
        return "EmployeeModel{\n" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nfirstName='" + firstName + '\'' +
                ", \nlastName='" + lastName + '\'' +
                ", \nmiddleName='" + (middleName != null ? middleName : "") + '\'' +
                ", \nemail='" + (email != null ? email : "") + '\'' +
                ", \ndepartment='" + (department != null ? department : "") + '\'' +
                ", \nhireDate='" + (hireDate != null ? hireDate : "") + '\'' +
                ", \ncurrentlyEmployed=" + currentlyEmployed +
                ", \nterminatedDate='" + (terminatedDate != null ? terminatedDate : "") + '\'' +
                ", \nphone='" + (phone != null ? phone : "") + '\'' +
                ", \naddress='" + (address != null ? address : "") + '\'' +
                ", \ncity='" + (city != null ? city : "") + '\'' +
                ", \nstate='" + (state != null ? state : "") + '\'' +
                ", \nzipCode='" + (zipCode != null ? zipCode : "") + '\'' +
                ", \npayRate='" + (payRate != null ? payRate : "") + '\'' +
                ", \npermissionAccess='" + (permissionAccess != null ? permissionAccess : "") + '\'' +
                "\n}";
    }
}
