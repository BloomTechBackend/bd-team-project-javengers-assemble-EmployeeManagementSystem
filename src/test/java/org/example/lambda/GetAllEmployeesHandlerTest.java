package org.example.lambda;

import org.example.dynamodb.EmployeeDao;
import org.example.exceptions.EmployeeNotFoundException;
import org.example.model.Employee;
import org.example.model.PermissionLevel;
import org.example.model.requests.GetAllEmployeesRequest;
import org.example.model.results.GetAllEmployeesResult;
import org.example.utils.ModelConverter;
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
import static org.mockito.MockitoAnnotations.initMocks;

public class GetAllEmployeesHandlerTest {
    @Mock
    EmployeeDao employeeDao;
    @InjectMocks
    GetAllEmployeesHandler employeeHandler;

    private Employee employee1;
    private Employee employee2;
    private List<Employee> employeeList = new ArrayList<>();

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

        employee1 = Employee.builder()
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

        employee2 = Employee.builder()
                .withEmployeeId("Z7GWRU96")
                .withFirstName(firstName)
                .withLastName(lastName)
                .withMiddleName("Middle")
                .withEmail("john.m.doe@example.com")
                .withDepartment("Sales")
                .withHireDate(hireDate)
                .withCurrentlyEmployed(currentlyEmployed)
                .withPhone(phone)
                .withAddress(address)
                .withCity(city)
                .withState(state)
                .withZipCode(zipCode)
                .withPayRate(payRate)
                .build();

        employeeList.add(employee1);
        employeeList.add(employee2);
    }

    @Test
    public void handleRequest_retrievesAllEmployees() {
        when(employeeDao.getAllEmployees()).thenReturn(employeeList);
        GetAllEmployeesRequest request = new GetAllEmployeesRequest();
        request.setEmployeeId(employee1.getEmployeeId());
        request.setPermissionLevel(employee1.getPermissionAccess().toString());

        GetAllEmployeesResult result = employeeHandler.handleRequest(request, null);
        List<Employee> retrievedEmployees = ModelConverter.fromEmployeeModelList(result.getEmployeeList());

        verify(employeeDao, never()).saveEmployee(any(Employee.class));
        assertTrue(result.isEmployeesRetrieved());
        assertEquals(employee1, retrievedEmployees.get(0));
        assertEquals(employee2, retrievedEmployees.get(1));
    }

    @Test
    public void handleRequest_withInsufficientAccess_throwsUnauthorizedAccessException() {
        String errorMessage = "User does not have permission to access this resource. Resource: GetAllEmployeesHandler";
        when(employeeDao.getEmployee(anyString())).thenThrow(new EmployeeNotFoundException(errorMessage));
        GetAllEmployeesRequest request = new GetAllEmployeesRequest();
        request.setEmployeeId(employee2.getEmployeeId());
        request.setPermissionLevel(employee2.getPermissionAccess().toString());

        GetAllEmployeesResult result = employeeHandler.handleRequest(request, null);

        verify(employeeDao, never()).saveEmployee(any(Employee.class));
        assertFalse(result.isEmployeesRetrieved());
        assertEquals(errorMessage, result.getError());
    }

    @Test
    public void handleRequest_throwsUnexpectedException() {
        String errorMessage = "An unexpected error occurred.";
        when(employeeDao.getAllEmployees()).thenThrow(new RuntimeException(errorMessage));
        GetAllEmployeesRequest request = new GetAllEmployeesRequest();
        request.setEmployeeId(employee1.getEmployeeId());
        request.setPermissionLevel(employee1.getPermissionAccess().toString());

        GetAllEmployeesResult result = employeeHandler.handleRequest(request, null);

        verify(employeeDao, never()).saveEmployee(any(Employee.class));
        assertFalse(result.isEmployeesRetrieved());
        assertEquals(errorMessage, result.getError());
    }
}
