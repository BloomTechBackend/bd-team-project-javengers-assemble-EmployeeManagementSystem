package org.example.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import org.example.dynamodb.model.EmployeeModel;
import org.example.exceptions.EmployeeNotFoundException;
import org.example.model.Employee;
import org.example.model.PermissionLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class EmployeeDaoTest {
    AutoCloseable mocks;
    @Mock
    DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    EmployeeDao employeeDao;

    Employee employee;
    EmployeeModel employeeModel;
    List<EmployeeModel> employeeModelList = new ArrayList<>();

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

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);

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
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void getEmployee_returnsInstanceOfEmployee() {
        when(dynamoDBMapper.load(any(), anyString())).thenReturn(employeeModel);

        assertInstanceOf(Employee.class, employeeDao.getEmployee(employeeId));
    }

    @Test
    public void getEmployee_withInvalidEmployeeId_throwsEmployeeNotFoundException() {
        when(dynamoDBMapper.load(any(), anyString())).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeDao.getEmployee("Invalid Employee ID");
        });
    }

    @Test
    public void saveEmployee_savesEmployeeToTable() {
        doNothing().when(dynamoDBMapper).save(any(EmployeeModel.class));

        EmployeeModel savedEmployeeModel = employeeDao.saveEmployee(employee);

        assertNotNull(savedEmployeeModel);
        verify(dynamoDBMapper, times(1)).save(any(EmployeeModel.class));
    }

    @Test
    public void saveEmployee_withInvalidEmployee_throwsException() {
        doNothing().when(dynamoDBMapper).save(any(EmployeeModel.class));

        assertThrows(Exception.class, () -> {
            employeeDao.saveEmployee(null);
        });
    }

    @Test
    public void getAllEmployees_retrievesAllEmployees() {
        // GIVEN
        PaginatedScanList<EmployeeModel> mockPaginatedScanList = mock(PaginatedScanList.class);
        when(mockPaginatedScanList.iterator()).thenReturn(employeeModelList.iterator());
        when(mockPaginatedScanList.size()).thenReturn(employeeModelList.size());
        when(dynamoDBMapper.scan(eq(EmployeeModel.class), any(DynamoDBScanExpression.class))).thenReturn(mockPaginatedScanList);

        // WHEN
        List<Employee> employeeList = employeeDao.getAllEmployees();

        // THEN
        assertFalse(employeeList.isEmpty(), "List of Employees should not be empty!");
        assertInstanceOf(Employee.class, employeeList.get(0));
    }

}
