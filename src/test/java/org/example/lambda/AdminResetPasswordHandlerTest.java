package org.example.lambda;

import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.AdminResetPasswordRequest;
import org.example.model.results.AdminResetPasswordResult;
import org.example.utils.ModelConverter;
import org.example.utils.gson.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AdminResetPasswordHandlerTest {
    private AutoCloseable mocks;
    @InjectMocks
    AdminResetPasswordHandler adminResetPasswordHandler;

    @Mock
    private EmployeeCredentialsDao credentialsDao;

    private EmployeeCredentials employeeCredentials;

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);

        String employeeId = "Emp123456";
        String username = "jdoe";
        String salt = "sl2Z7n6DMz+sbubYymSjCg==";
        String password = "rnlQUWqkHahOs7JSRhJteRG2qjQ1EZNUmBkP+Ziu498=";
        LocalDateTime lastUpdated = LocalDateTime.of(2024, 06, 04, 04, 34, 37);
        boolean accountLocked = false;
        boolean forceChangeAfterLogin = false;
        int failedAttempts = 0;

        employeeCredentials = new EmployeeCredentials(employeeId, username, salt,
                password, lastUpdated, accountLocked, forceChangeAfterLogin,failedAttempts);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void handleRequest_validRequest_updatesEmployeesCredentials() {
        // Given
        String originalPassword = employeeCredentials.getPassword();

        AdminResetPasswordRequest request = new AdminResetPasswordRequest();
        request.setEmployeeId(employeeCredentials.getEmployeeId());
        request.setUsername(employeeCredentials.getUsername());
        request.setPassword("NewPassword");

        // Mock the initial getEmployeeCredentials call
        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenReturn(employeeCredentials);

        // Ensure the updated credentials reflect the password reset changes
        when(credentialsDao.saveEmployeeCredentials(any(EmployeeCredentials.class)))
                .thenAnswer(invocation -> {
                    EmployeeCredentials arg = invocation.getArgument(0);
                    arg.adminResetPassword("NewPassword"); // Simulate password reset
                    return ModelConverter.fromEmployeeCredentials(arg);
                });

        // When
        AdminResetPasswordResult result = JsonUtil.fromJson(adminResetPasswordHandler.handleRequest(request, null), AdminResetPasswordResult.class);

        // Then
        verify(credentialsDao).saveEmployeeCredentials(any(EmployeeCredentials.class));

        assertNotEquals(originalPassword, employeeCredentials.getPassword(), "New password should not match the old password after update.");
        assertTrue(result.isEmployeeCredentialsReset(), "employeeCredentialsReset variable should be true");
        assertEquals(result.getEmployeeId(), employeeCredentials.getEmployeeId(), "Result employeeId should match the EmployeeCredentials instance employeeId.");
        assertEquals(result.getUsername(), employeeCredentials.getUsername(), "Result username should match the EmployeeCredentials instance username.");
        assertNotEquals(originalPassword, employeeCredentials.getLastUpdated().toString(), "lastUpdated should not be equal after password update.");
        assertFalse(result.isAccountLocked(), "Result accountLocked should be false.");
        assertEquals(result.isAccountLocked(), employeeCredentials.isAccountLocked(), "Result accountLocked should match the EmployeeCredentials instance accountLocked");
        assertTrue(result.isForceChangeAfterLogin(), "Result forceChangeAfterLogin should be set to true");
        assertEquals(result.isForceChangeAfterLogin(), employeeCredentials.isForceChangeAfterLogin(), "Result forceChangeAfterLogin should match the EmployeeCredentials instance forceChangeAfterLogin");
    }

    @Test
    public void handleRequest_validRequest_throwsUsernameNotFoundException() {
        // Given
        AdminResetPasswordRequest request = new AdminResetPasswordRequest();
        request.setUsername(employeeCredentials.getUsername());
        request.setEmployeeId(employeeCredentials.getEmployeeId());
        request.setPassword("NewPassword");

        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenThrow(new UsernameNotFoundException("Username not found"));

        // When
        AdminResetPasswordResult result = JsonUtil.fromJson(adminResetPasswordHandler.handleRequest(request, null), AdminResetPasswordResult.class);
        // Then
        assertFalse(result.isEmployeeCredentialsReset());
        assertEquals(request.getEmployeeId(), result.getEmployeeId());
        assertEquals(request.getUsername(), result.getUsername());
        assertEquals("Username not found", result.getError());
    }

    @Test
    public void handleRequest_validRequest_throwsException() {
        // Given
        AdminResetPasswordRequest request = new AdminResetPasswordRequest();
        request.setUsername(employeeCredentials.getUsername());
        request.setEmployeeId(employeeCredentials.getEmployeeId());
        request.setPassword("NewPassword");

        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenThrow(new RuntimeException("An unexpected error occurred. "));

        // When
        AdminResetPasswordResult result = JsonUtil.fromJson(adminResetPasswordHandler.handleRequest(request, null), AdminResetPasswordResult.class);

        // Then
        assertFalse(result.isEmployeeCredentialsReset());
        assertEquals(request.getEmployeeId(), result.getEmployeeId());
        assertEquals(request.getUsername(), result.getUsername());
        assertEquals("An unexpected error occurred. ", result.getError());
    }

}
