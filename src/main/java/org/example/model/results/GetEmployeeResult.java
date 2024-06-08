package org.example.model.results;

import org.example.model.PermissionLevel;

import java.time.LocalDate;

public class GetEmployeeResult implements Result {
    private final boolean employeeRetrieved;
    private final String employeeId;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String email;
    private final String department;
    private final LocalDate hireDate;
    private final boolean currentlyEmployed;
    private final LocalDate terminatedDate;
    private final String phone;
    private final String address;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String payRate;
    private final PermissionLevel permissionAccess;
    private final String error;

    private GetEmployeeResult(Builder builder) {
        this.employeeRetrieved = builder.employeeRetrieved;
        this.employeeId = builder.employeeId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.middleName = builder.middleName;
        this.email = builder.email;
        this.department = builder.department;
        this.hireDate = builder.hireDate;
        this.currentlyEmployed = builder.currentlyEmployed;
        this.terminatedDate = builder.terminatedDate;
        this.phone = builder.phone;
        this.address = builder.address;
        this.city = builder.city;
        this.state = builder.state;
        this.zipCode = builder.zipCode;
        this.payRate = builder.payRate;
        this.permissionAccess = builder.permissionAccess;
        this.error = builder.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean employeeRetrieved;
        private String employeeId;
        private String firstName;
        private String lastName;
        private String middleName;
        private String email;
        private String department;
        private LocalDate hireDate;
        private boolean currentlyEmployed;
        private LocalDate terminatedDate;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String payRate;
        private PermissionLevel permissionAccess;
        private String error;

        public Builder() {

        }

        public Builder withEmployeeRetrieved(boolean employeeRetrieved) {
            this.employeeRetrieved = employeeRetrieved;
            return this;
        }
        public Builder withEmployeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withDepartment(String department) {
            this.department = department;
            return this;
        }

        public Builder withHireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
            return this;
        }

        public Builder withCurrentlyEmployed(boolean currentlyEmployed) {
            this.currentlyEmployed = currentlyEmployed;
            return this;
        }

        public Builder withTerminatedDate(LocalDate terminatedDate) {
            this.terminatedDate = terminatedDate;
            return this;
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder withPayRate(String payRate) {
            this.payRate = payRate;
            return this;
        }

        public Builder withPermissionAccess(PermissionLevel permissionAccess) {
            this.permissionAccess = permissionAccess;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }


        public GetEmployeeResult build() {
            return new GetEmployeeResult(this);
        }
    }

    public boolean isEmployeeRetrieved() {
        return employeeRetrieved;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public boolean isCurrentlyEmployed() {
        return currentlyEmployed;
    }

    public LocalDate getTerminatedDate() {
        return terminatedDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPayRate() {
        return payRate;
    }

    public PermissionLevel getPermissionAccess() {
        return permissionAccess;
    }

    public String getError() {
        return error;
    }
}
