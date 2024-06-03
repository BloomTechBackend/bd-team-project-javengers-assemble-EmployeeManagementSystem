package org.example.utils;

import org.example.dynamodb.model.EmployeeModel;
import org.example.dynamodb.model.TimeEntryModel;
import org.example.exceptions.InvalidEmployeeException;
import org.example.exceptions.InvalidEmployeeModelException;
import org.example.exceptions.InvalidTimeEntryException;
import org.example.exceptions.InvalidTimeEntryModelException;
import org.example.model.Employee;
import org.example.model.PermissionLevel;
import org.example.model.TimeEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelConverterTest {
    private Employee employee;
    private EmployeeModel employeeModel;
    private TimeEntry timeEntry;
    private TimeEntryModel timeEntryModel;

    private final List<Employee> employeeList = new ArrayList<>();
    private final List<EmployeeModel> employeeModelList = new ArrayList<>();
    private final List<TimeEntryModel> timeEntryModelList = new ArrayList<>();
    private final List<TimeEntry> timeEntryList = new ArrayList<>();

    private final String employeeId = "Emp123456";
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String middleName = "Middle";
    private final String email = "john_doe@example.com";
    private final String department = "Department Name";
    private final String phone = "+1-123-123-4567";
    private final String address = "123 Example Ave";
    private final String city = "Example City";
    private final String state = "Colorado";
    private final String zipCode = "12345";
    private final String payRate = "2500/bw";
    private final PermissionLevel permissionLevel = PermissionLevel.ADMIN;
    private final String timeEntryId = "TE123456";
    private final LocalDateTime timeIn = LocalDateTime.of(2024, 01, 01, 8, 30);
    private final LocalDateTime timeOut = LocalDateTime.of(2024, 01, 01, 17, 30);


    @BeforeEach
    void setUp() {
        // Employee Setup
        employee = Employee.builder()
                .withEmployeeId(employeeId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withMiddleName(middleName)
                .withEmail(email)
                .withDepartment(department)
                .withHireDate(LocalDate.of(2020, 1,1))
                .withPhone(phone)
                .withAddress(address)
                .withCity(city)
                .withState(state)
                .withZipCode(zipCode)
                .withPayRate(payRate)
                .withPermissionAccess(permissionLevel)
                .build();

        // Employee List Setup
        employeeList.add(employee);
        employeeList.add(Employee.builder()
                        .withEmployeeId("Emp654321")
                        .withFirstName("Jane")
                        .withLastName("Doe")
                        .withEmail("jane_doe@example.com")
                        .withDepartment(department)
                        .withHireDate(LocalDate.of(2022, 6, 26))
                        .withPhone(phone)
                        .withAddress(address)
                        .withCity(city)
                        .withState(state)
                        .withZipCode(zipCode)
                        .withPayRate(payRate)
                        .build()
        );

        // EmployeeModel Setup
        employeeModel = new EmployeeModel();
        employeeModel.setEmployeeId(employeeId);
        employeeModel.setFirstName(firstName);
        employeeModel.setLastName(lastName);
        employeeModel.setMiddleName(middleName);
        employeeModel.setEmail(email);
        employeeModel.setDepartment(department);
        employeeModel.setCurrentlyEmployed(true);
        employeeModel.setHireDate("2020-01-01");
        employeeModel.setPhone(phone);
        employeeModel.setAddress(address);
        employeeModel.setCity(city);
        employeeModel.setState(state);
        employeeModel.setZipCode(zipCode);
        employeeModel.setPayRate(payRate);
        employeeModel.setPermissionAccess("Admin");

        // EmployeeModel List Setup
        EmployeeModel employeeModel1 = new EmployeeModel();
                employeeModel1.setEmployeeId("Emp654321");
                employeeModel1.setFirstName("Jane");
                employeeModel1.setLastName("Doe");
                employeeModel1.setEmail("jane_doe@example.com");
                employeeModel1.setDepartment(department);
                employeeModel1.setHireDate("2022-06-26");
                employeeModel1.setCurrentlyEmployed(true);
                employeeModel1.setPhone(phone);
                employeeModel1.setAddress(address);
                employeeModel1.setCity(city);
                employeeModel1.setState(state);
                employeeModel1.setZipCode(zipCode);
                employeeModel1.setPayRate(payRate);
                employeeModel1.setPermissionAccess("Standard");

        employeeModelList.add(employeeModel);
        employeeModelList.add(employeeModel1);

        // TimeEntry Setup
        timeEntry = TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withEntryId(timeEntryId)
                .withTimeIn(timeIn)
                .withTimeOut(timeOut)
                .withDuration(9.0)
                .build();

        // TimeEntryModel Setup
        timeEntryModel = new TimeEntryModel();
        timeEntryModel.setEmployeeId(employeeId);
        timeEntryModel.setEntryId(timeEntryId);
        timeEntryModel.setTimeIn(timeIn.toString());
        timeEntryModel.setTimeOut(timeOut.toString());
        timeEntryModel.setDuration(9.0);

        // timeEntryList Setup
        timeEntryList.add(timeEntry);
        timeEntryList.add(TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withEntryId("TE123457")
                .withTimeIn(timeIn)
                .withTimeOut(timeOut)
                .withDuration(9.0)
                .build()
        );
        timeEntryList.add(TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withEntryId("TE123458")
                .withTimeIn(timeIn)
                .withTimeOut(timeOut)
                .withDuration(9.0)
                .build()
        );
        timeEntryList.add(TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withEntryId("TE123459")
                .withTimeIn(timeIn)
                .withTimeOut(timeOut)
                .withDuration(9.0)
                .build()
        );

        // timeEntryModelList Setup
        TimeEntryModel timeEntryModel1 = new TimeEntryModel();
        timeEntryModel1.setEmployeeId(employeeId);
        timeEntryModel1.setEntryId("TE123457");
        timeEntryModel1.setTimeIn(timeIn.toString());
        timeEntryModel1.setTimeOut(timeOut.toString());
        timeEntryModel1.setDuration(9.0);

        TimeEntryModel timeEntryModel2 = new TimeEntryModel();
        timeEntryModel2.setEmployeeId(employeeId);
        timeEntryModel2.setEntryId("TE123458");
        timeEntryModel2.setTimeIn(timeIn.toString());
        timeEntryModel2.setTimeOut(timeOut.toString());
        timeEntryModel2.setDuration(9.0);

        TimeEntryModel timeEntryModel3 = new TimeEntryModel();
        timeEntryModel3.setEmployeeId(employeeId);
        timeEntryModel3.setEntryId("TE123459");
        timeEntryModel3.setTimeIn(timeIn.toString());
        timeEntryModel3.setTimeOut(timeOut.toString());
        timeEntryModel3.setDuration(9.0);

        timeEntryModelList.add(timeEntryModel);
        timeEntryModelList.add(timeEntryModel1);
        timeEntryModelList.add(timeEntryModel2);
        timeEntryModelList.add(timeEntryModel3);
    }

    @Test
    public void fromEmployeeModel_convertsEmployeeModelToEmployee() {
        Employee convertedObject = ModelConverter.fromEmployeeModel(employeeModel);

        assertInstanceOf(Employee.class, convertedObject);
        assertNotNull(convertedObject);
        assertEquals(employee, convertedObject);
    }

    @Test
    public void fromEmployeeModel_withIncompleteFields_convertsEmployeeModelToEmployee() {
        EmployeeModel employeeModel = new EmployeeModel();
        employeeModel.setEmployeeId(employeeId);
        employeeModel.setFirstName(firstName);
        employeeModel.setLastName(lastName);
        employeeModel.setCurrentlyEmployed(true);
        employeeModel.setPermissionAccess("Standard");

        Employee employee = Employee.builder()
                .withEmployeeId(employeeId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();

        Employee convertedObject = ModelConverter.fromEmployeeModel(employeeModel);

        assertInstanceOf(Employee.class, convertedObject);
        assertNotNull(convertedObject);
        assertEquals(employee, convertedObject);
    }

    @Test
    public void fromEmployeeModelList_convertsEmployeeModelsToEmployees() {
        List<Employee> convertedList = ModelConverter.fromEmployeeModelList(employeeModelList);

        assertTrue(convertedList instanceof List<?>); // Check if it's a list
        if (!convertedList.isEmpty()) {
            assertInstanceOf(Employee.class, convertedList.get(0)); // Check if the elements are instances of Employee
        }

        assertNotNull(convertedList);
        assertEquals(employeeList, convertedList);
    }

    @Test
    public void fromEmployee_convertsEmployeeToEmployeeModel() {
        EmployeeModel convertedObject = ModelConverter.fromEmployee(employee);

        assertInstanceOf(EmployeeModel.class, convertedObject);
        assertNotNull(convertedObject);
        assertEquals(employeeModel, convertedObject);
    }

    @Test
    public void fromEmployee_withIncompleteFields_convertsEmployeeToEmployeeModel() {
        Employee employee = Employee.builder()
                .withEmployeeId(employeeId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();

        EmployeeModel employeeModel = new EmployeeModel();
        employeeModel.setEmployeeId(employeeId);
        employeeModel.setFirstName(firstName);
        employeeModel.setLastName(lastName);
        employeeModel.setCurrentlyEmployed(true);
        employeeModel.setPermissionAccess("Standard");


        EmployeeModel convertedObject = ModelConverter.fromEmployee(employee);

        assertInstanceOf(EmployeeModel.class, convertedObject);
        assertNotNull(convertedObject);
        assertEquals(employeeModel, convertedObject);
    }


    @Test
    public void fromEmployeeList_convertsEmployeesToEmployeeModels() {
        List<EmployeeModel> convertedList = ModelConverter.fromEmployeeList(employeeList);

        assertTrue(convertedList instanceof List<?>); // Check if it's a list
        if (!convertedList.isEmpty()) {
            assertInstanceOf(EmployeeModel.class, convertedList.get(0)); // Check if the elements are instances of Employee
        }

        assertNotNull(convertedList);
        assertEquals(employeeModelList, convertedList);
    }

    @Test
    public void fromTimeEntryModel_convertsTimeEntryModelToTimeEntry() {
        TimeEntry convertedObject = ModelConverter.fromTimeEntryModel(timeEntryModel);

        assertInstanceOf(TimeEntry.class, convertedObject);
        assertNotNull(convertedObject);
        assertEquals(timeEntry, convertedObject);
    }

    @Test
    public void fromTimeEntryModel_withIncompleteFields_convertsTimeEntryModelToTimeEntry() {
        TimeEntryModel timeEntryModel = new TimeEntryModel();
        timeEntryModel.setEmployeeId(employeeId);
        timeEntryModel.setEntryId(timeEntryId);
        timeEntryModel.setTimeIn(timeIn.toString());

        TimeEntry timeEntry = TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withEntryId(timeEntryId)
                .withTimeIn(timeIn)
                .build();

        TimeEntry convertedObject = ModelConverter.fromTimeEntryModel(timeEntryModel);

        assertInstanceOf(TimeEntry.class, convertedObject);
        assertNotNull(convertedObject);
        assertEquals(timeEntry, convertedObject);
    }

    @Test
    public void fromTimeEntryModelList_convertsTimeEntriesModelsToTimeEntries() {
        List<TimeEntry> convertedList = ModelConverter.fromTimeEntryModelList(timeEntryModelList);

        assertTrue(convertedList instanceof List<?>); // Check if it's a list
        if (!convertedList.isEmpty()) {
            assertInstanceOf(TimeEntry.class, convertedList.get(0)); // Check if the elements are instances of TimeEntry
        }

        assertNotNull(convertedList);
        assertEquals(timeEntryList, convertedList);
    }

    @Test
    public void fromTimeEntry_convertsTimeEntryToTimeEntryModel() {
        TimeEntryModel convertedObject = ModelConverter.fromTimeEntry(timeEntry);

        assertInstanceOf(TimeEntryModel.class, convertedObject);
        assertNotNull(convertedObject);
        assertEquals(timeEntryModel, convertedObject);
    }

    @Test
    public void fromTimeEntry_withIncompleteFields_convertsTimeEntryToTimeEntryModel() {
        TimeEntryModel timeEntryModel = new TimeEntryModel();
        timeEntryModel.setEmployeeId(employeeId);
        timeEntryModel.setEntryId(timeEntryId);
        timeEntryModel.setTimeIn(timeIn.toString());

        TimeEntry timeEntry = TimeEntry.builder()
                .withEmployeeId(employeeId)
                .withEntryId(timeEntryId)
                .withTimeIn(timeIn)
                .build();

        TimeEntryModel convertedObject = ModelConverter.fromTimeEntry(timeEntry);

        assertInstanceOf(TimeEntryModel.class, convertedObject);
        assertNotNull(convertedObject);
        assertEquals(timeEntryModel, convertedObject);
    }

    @Test
    public void fromTimeEntryList_convertsTimeEntriesToTimeEntryModels() {
        List<TimeEntryModel> convertedList = ModelConverter.fromTimeEntryList(timeEntryList);

        assertTrue(convertedList instanceof List<?>); // Check if it's a list
        if (!convertedList.isEmpty()) {
            assertInstanceOf(TimeEntryModel.class, convertedList.get(0)); // Check if the elements are instances of TimeEntryModel
        }

        assertNotNull(convertedList);
        assertEquals(timeEntryModelList, convertedList);
    }

    @Test
    public void fromEmployeeModel_withNullEmployeeModel_throwsInvalidEmployeeModelException() {
        assertThrows(InvalidEmployeeModelException.class, () -> {
            ModelConverter.fromEmployeeModel(null);
        });
    }

    @Test
    public void fromEmployee_withNullEmployee_throwsInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class, () -> {
            ModelConverter.fromEmployee(null);
        });
    }

    @Test
    public void fromTimeEntryModel_withNullTimeEntryModel_throwsInvalidTimeEntryModelException() {
        assertThrows(InvalidTimeEntryModelException.class, () -> {
            ModelConverter.fromTimeEntryModel(null);
        });
    }

    @Test
    public void fromTimeEntry_withNullTimeEntry_throwsInvalidTimeEntryException() {
        assertThrows(InvalidTimeEntryException.class, () -> {
            ModelConverter.fromTimeEntry(null);
        });
    }

    @Test
    public void fromEmployeeModelList_withNullEmployeeModelList_throwsInvalidEmployeeModelException() {
        assertThrows(InvalidEmployeeModelException.class, () -> {
            ModelConverter.fromEmployeeModelList(null);
        });
    }

    @Test
    public void fromEmployeeList_withNullEmployeeList_throwsInvalidEmployeeException() {
        assertThrows(InvalidEmployeeException.class, () -> {
            ModelConverter.fromEmployeeList(null);
        });
    }

    @Test
    public void fromTimeEntryModelList_withNullTimeEntryModelList_throwsInvalidTimeEntryModelException() {
        assertThrows(InvalidTimeEntryModelException.class, () -> {
            ModelConverter.fromTimeEntryModelList(null);
        });
    }

    @Test
    public void fromTimeEntryList_withNullTimeEntryList_throwsInvalidTimeEntryException() {
        assertThrows(InvalidTimeEntryException.class, () -> {
            ModelConverter.fromTimeEntryList(null);
        });
    }

    @Test
    public void fromEmployeeList_withEmptyList_returnsEmptyList() {
        List<Employee> emptyList = new ArrayList<>();

        assertTrue(ModelConverter.fromEmployeeList(emptyList).isEmpty(),
                "When provided an empty list, this method should return an empty list!");
    }

    @Test
    public void fromEmployeeModelList_withEmptyList_returnsEmptyList() {
        List<EmployeeModel> emptyList = new ArrayList<>();

        assertTrue(ModelConverter.fromEmployeeModelList(emptyList).isEmpty(),
                "When provided an empty list, this method should return an empty list!");
    }

    @Test
    public void fromTimeEntryList_withEmptyList_returnsEmptyList() {
        List<TimeEntry> emptyList = new ArrayList<>();

        assertTrue(ModelConverter.fromTimeEntryList(emptyList).isEmpty(),
                "When provided an empty list, this method should return an empty list!");
    }

    @Test
    public void fromTimeEntryModelList_withEmptyList_returnsEmptyList() {
        List<TimeEntryModel> emptyList = new ArrayList<>();

        assertTrue(ModelConverter.fromTimeEntryModelList(emptyList).isEmpty(),
                "When provided an empty list, this method should return an empty list!");
    }
}
