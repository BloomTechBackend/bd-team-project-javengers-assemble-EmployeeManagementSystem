package org.example.lambda;

import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.dynamodb.EmployeeDao;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.Employee;
import org.example.model.EmployeeCredentials;
import org.example.model.PermissionLevel;
import org.example.model.requests.NewEmployeeRequest;
import org.example.model.results.NewEmployeeResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class NewEmployeeHandlerTest {

    @Mock
    private EmployeeCredentialsDao credentialsDao;

    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private NewEmployeeHandler newEmployeeHandler;

    private NewEmployeeRequest request;
    private EmployeeCredentials employeeCredentials;
    private Employee newEmployee;

    @BeforeEach
    void setUp() {
        initMocks(this);

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
        String payRate = "6000/bw";
        PermissionLevel permissionAccess = PermissionLevel.ADMIN;

        String username = "jdoe";
        String salt = "sl2Z7n6DMz+sbubYymSjCg==";
        String password = "rnlQUWqkHahOs7JSRhJteRG2qjQ1EZNUmBkP+Ziu498=";
        LocalDateTime lastUpdated = LocalDateTime.of(2024, 06, 04, 04, 34, 37);
        boolean accountLocked = false;
        boolean forceChangeAfterLogin = false;
        int failedAttempts = 0;

        request = new NewEmployeeRequest();
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
        request.setPayRate(payRate);
        request.setPermissionAccess(permissionAccess.toString().toUpperCase());
        request.setUsername(username);
        request.setPassword(password);

        newEmployee = Employee.builder()
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

        employeeCredentials = new EmployeeCredentials(employeeId, username, salt,
                password, lastUpdated, accountLocked, forceChangeAfterLogin,failedAttempts);
    }

    @Test
    public void handleRequest_validRequest_createsNewEmployee() {
        // Given
        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenThrow(new UsernameNotFoundException("Username not found"));
        when(employeeDao.saveEmployee(any(Employee.class))).thenReturn(null);
        when(credentialsDao.saveEmployeeCredentials(any(EmployeeCredentials.class))).thenReturn(null);

        // When
        NewEmployeeResult result = newEmployeeHandler.handleRequest(request, null);

        // Then
        verify(employeeDao).saveEmployee(any(Employee.class));
        verify(credentialsDao).saveEmployeeCredentials(any(EmployeeCredentials.class));

        assertTrue(result.isNewEmployeeCreated());
        assertEquals(request.getFirstName(), result.getFirstName());
        assertEquals(request.getLastName(), result.getLastName());
        assertEquals(request.getUsername(), result.getUsername());
    }

    @Test
    public void handleRequest_existingUsername_throwsUsernameAlreadyExistsException() {
        // Given
        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenReturn(employeeCredentials);

        // When
        NewEmployeeResult result = newEmployeeHandler.handleRequest(request, null);

        // Then
        assertFalse(result.isNewEmployeeCreated());
        assertEquals("Username \"jdoe\" is already taken. Please choose another.", result.getError());
    }

    @Test
    public void handleRequest_validRequest_throwsException() {
        // Given
        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenThrow(new RuntimeException("An unexpected error occurred."));

        // When
        NewEmployeeResult result = newEmployeeHandler.handleRequest(request, null);

        // Then
        assertFalse(result.isNewEmployeeCreated());
        assertEquals("An unexpected error occurred.", result.getError());
    }
}
