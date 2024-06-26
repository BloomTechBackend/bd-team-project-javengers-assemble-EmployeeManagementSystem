package org.example.lambda;

import org.example.dynamodb.EmployeeDao;
import org.example.dynamodb.model.EmployeeModel;
import org.example.model.Employee;
import org.example.model.PermissionLevel;
import org.example.model.requests.UpdateEmployeeRequest;
import org.example.model.results.UpdateEmployeeResult;
import org.example.utils.ModelConverter;
import org.example.utils.gson.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateEmployeeHandlerTest {
    private AutoCloseable mocks;
    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private UpdateEmployeeHandler updateEmployeeHandler;

    private UpdateEmployeeRequest request;
    private Employee employee;

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);

        String employeeId = "Emp123456";
        String firstName = "John";
        String lastName = "Doe";
        String middleName = "A";
        String email = "john.doe@example.com";
        String department = "Engineering";
        LocalDate hireDate = LocalDate.of(2024, 06, 04);
        String phone = "+1-123-456-7890";
        String address = "123 Main St";
        String city = "Anytown";
        String state = "CA";
        String zipCode = "12345";
        String payRate = "3500/bw";
        PermissionLevel permissionAccess = PermissionLevel.STANDARD;

        employee = Employee.builder()
                .withEmployeeId(employeeId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withMiddleName(middleName)
                .withEmail(email)
                .withDepartment(department)
                .withHireDate(hireDate)
                .withPhone(phone)
                .withCity(city)
                .withState(state)
                .withZipCode(zipCode)
                .withPayRate(payRate)
                .withPermissionAccess(permissionAccess)
                .build();

        request = new UpdateEmployeeRequest();
        request.setEmployeeId(employeeId);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setMiddleName(middleName);
        request.setEmail(email);
        request.setDepartment(department);
        request.setHireDate(hireDate.toString());
        request.setPhone(phone);
        request.setAddress(address);
        request.setCity(city);
        request.setState(state);
        request.setZipCode(zipCode);
        request.setPayRate("6500/bw");
        request.setPermissionAccess("ADMIN");

    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void handleRequest_validRequest_updatesEmployee() {
        // Given
        Employee originalEmployee = employee;
        String originalPayRate = employee.getPayRate();
        EmployeeModel employeeModel = ModelConverter.fromEmployee(employee);
        employeeModel.setPayRate(request.getPayRate());
        employeeModel.setPermissionAccess(request.getPermissionAccess().name());
        when(employeeDao.getEmployee(anyString())).thenReturn(employee);
        when(employeeDao.saveEmployee(any(Employee.class))).thenReturn(employeeModel);

        // When
        UpdateEmployeeResult result = JsonUtil.fromJson(updateEmployeeHandler.handleRequest(request, null), UpdateEmployeeResult.class);

        // Then
        verify(employeeDao).saveEmployee(any(Employee.class));

        assertTrue(result.isEmployeeUpdated(), "Result isEmployeeUpdated should return true.");
        assertNotEquals(originalEmployee, convertResultToEmployee(result), "Updated Employee should not be equal to the original Employee Instance.");
        assertNotEquals(originalPayRate, convertResultToEmployee(result).getPayRate(), "Pay rates shouldn't match after update.");
        assertEquals(PermissionLevel.ADMIN, convertResultToEmployee(result).getPermissionAccess(), "Permission access shouldn't match after update.");
    }

    @Test
    public void handleRequest_validRequest_throwsException() {
        // Given
        when(employeeDao.getEmployee(anyString())).thenReturn(employee);
        when(employeeDao.saveEmployee(any(Employee.class))).thenThrow(new RuntimeException("An unexpected error occurred."));

        // When
        UpdateEmployeeResult result = JsonUtil.fromJson(updateEmployeeHandler.handleRequest(request, null), UpdateEmployeeResult.class);

        // Then
        assertFalse(result.isEmployeeUpdated());
        assertEquals("An unexpected error occurred.", result.getError());
    }

    private Employee convertResultToEmployee(UpdateEmployeeResult result) {
        return Employee.builder()
                .withEmployeeId(result.getEmployeeId())
                .withFirstName(result.getFirstName())
                .withLastName(result.getLastName())
                .withMiddleName(result.getMiddleName())
                .withEmail(result.getEmail())
                .withDepartment(result.getDepartment())
                .withHireDate(ModelConverter.convertStringToLocalDate(result.getHireDate()))
                .withCurrentlyEmployed(result.isCurrentlyEmployed())
                .withTerminatedDate(ModelConverter.convertStringToLocalDate(result.getTerminatedDate()))
                .withPhone(result.getPhone())
                .withAddress(result.getAddress())
                .withCity(result.getCity())
                .withState(result.getState())
                .withZipCode(result.getZipCode())
                .withPayRate(result.getPayRate())
                .withPermissionAccess(PermissionLevel.valueOf(result.getPermissionAccess().toUpperCase()))
                .build();
    }
}
