package org.example.lambda;

import org.example.exceptions.EmployeeNotFoundException;
import org.example.model.Employee;
import org.example.dynamodb.EmployeeDao;
import org.example.model.PermissionLevel;
import org.example.model.requests.GetEmployeeRequest;
import org.example.model.results.GetEmployeeResult;
import org.example.utils.ModelConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetEmployeeHandlerTest {
    @Mock
    EmployeeDao employeeDao;
    @InjectMocks
    GetEmployeeHandler employeeHandler;

    private Employee employee;

    @BeforeEach
    void setUp() {
        initMocks(this);

        String employeeId = "Q7RWVU3O";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String department = "Engineering";
        LocalDate hireDate = LocalDate.of(2024,06,05);
        boolean currentlyEmployed = true;
        String phone = "+1-123-456-7890";
        String address = "123 Address Dr";
        String city = "Some City";
        String state = "CO";
        String zipCode = "12345-1234";
        String payRate = "6500/bw";
        PermissionLevel permissionAccess = PermissionLevel.ADMIN;

        employee = Employee.builder()
                .withEmployeeId(employeeId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withDepartment(department)
                .withHireDate(hireDate)
                .withCurrentlyEmployed(currentlyEmployed)
                .withPhone(phone)
                .withAddress(address)
                .withCity(city)
                .withState(state)
                .withZipCode(zipCode)
                .withPayRate(payRate)
                .withPermissionAccess(permissionAccess)
                .build();
    }

    @Test
    public void handleRequest_withValidEmployeeId_retrievesSingleEmployee() {
        when(employeeDao.getEmployee(anyString())).thenReturn(employee);
        GetEmployeeRequest request = new GetEmployeeRequest();
        request.setEmployeeId(employee.getEmployeeId());

        GetEmployeeResult result = employeeHandler.handleRequest(request, null);

        verify(employeeDao, never()).saveEmployee(any(Employee.class));
        assertTrue(result.isEmployeeRetrieved());
        assertEquals(employee, convertResultToEmployee(result));
    }

    @Test
    public void handleRequest_withValidEmployeeId_throwsEmployeeNotFoundException() {
        String errorMessage = "Employee not found. Employee ID \"Q7RWVU3O\" not in employee database, " +
                "but the credentials for this ID exist in the employee_credentials table.";
        when(employeeDao.getEmployee(anyString())).thenThrow(new EmployeeNotFoundException(errorMessage));
        GetEmployeeRequest request = new GetEmployeeRequest();
        request.setEmployeeId(employee.getEmployeeId());

        GetEmployeeResult result = employeeHandler.handleRequest(request, null);

        verify(employeeDao, never()).saveEmployee(any(Employee.class));
        assertFalse(result.isEmployeeRetrieved());
        assertEquals(errorMessage, result.getError());
    }

    @Test
    public void handleRequest_throwsUnexpectedException() {
        when(employeeDao.getEmployee(anyString())).thenThrow(new RuntimeException("Unexpected error"));
        GetEmployeeRequest request = new GetEmployeeRequest();
        request.setEmployeeId(employee.getEmployeeId());

        GetEmployeeResult result = employeeHandler.handleRequest(request, null);

        verify(employeeDao, never()).saveEmployee(any(Employee.class));
        assertFalse(result.isEmployeeRetrieved());
        assertEquals("Unexpected error", result.getError());
    }

    private Employee convertResultToEmployee(GetEmployeeResult result) {
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
