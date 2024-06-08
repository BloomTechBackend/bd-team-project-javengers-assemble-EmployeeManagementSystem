package org.example.model.results;

import org.example.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class GetAllEmployeesResult implements Result {
    private final boolean employeesRetrieved;
    private final List<Employee> employeeList;
    private final String error;

    private GetAllEmployeesResult(Builder builder) {
        this.employeesRetrieved = builder.employeesRetrieved;
        this.employeeList = builder.employeeList;
        this.error = builder.error;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean employeesRetrieved;
        private List<Employee> employeeList = new ArrayList<>();
        private String error;

        public Builder() {

        }

        public Builder withEmployeesRetrieved(boolean employeesRetrieved) {
            this.employeesRetrieved = employeesRetrieved;
            return this;
        }

        public Builder withEmployeesList(List<Employee> employeesList) {
            this.employeeList = employeesList;
            return this;
        }

        public Builder withError(String error) {
            this.error = error;
            return this;
        }

        public GetAllEmployeesResult build() {
            return new GetAllEmployeesResult(this);
        }
    }

    public boolean isEmployeesRetrieved() {
        return employeesRetrieved;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public String getError() {
        return error;
    }
}
