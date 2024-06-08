package org.example.lambda;

import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.exceptions.AccountLockedException;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.LoginRequest;
import org.example.model.results.LoginResult;
import org.example.utils.gson.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class LoginHandlerTest {
    private AutoCloseable mocks;

    @Mock
    private EmployeeCredentialsDao credentialsDao;

    @InjectMocks
    private LoginHandler loginHandler;

    private LoginRequest request;
    private EmployeeCredentials employeeCredentials;

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);
        request = new LoginRequest();
        request.setUsername("jdoe");
        request.setPassword("p@ssw0rd");

        // Mock EmployeeCredentials to include the correct hashed password and salt
        String employeeId = "Emp123456";
        String username = "jdoe";
        String salt = "sl2Z7n6DMz+sbubYymSjCg==";
        String password = "rnlQUWqkHahOs7JSRhJteRG2qjQ1EZNUmBkP+Ziu498=";
        LocalDateTime lastUpdated = LocalDateTime.of(2024,06,01, 12,23,37);
        boolean accountLocked = false;
        boolean forceChangeAfterLogin = false;
        int failedAttempts = 0;

        employeeCredentials = new EmployeeCredentials(
                employeeId,
                username,
                salt,
                password,
                lastUpdated,
                accountLocked,
                forceChangeAfterLogin,
                failedAttempts
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void handleRequest_validCredentials_loginSuccess() throws Exception {
        // Given
        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenReturn(employeeCredentials);

        // When
        LoginResult result = JsonUtil.fromJson(loginHandler.handleRequest(request, null), LoginResult.class);

        // Then
        verify(credentialsDao, never()).saveEmployeeCredentials(any(EmployeeCredentials.class));
        assertTrue(result.isLoginSuccess());
        assertEquals("jdoe", result.getUsername());
        assertEquals("Emp123456", result.getEmployeeId());
        assertFalse(result.isForceChangeAfterLogin());
    }

    @Test
    public void handleRequest_invalidPassword_loginFail() throws Exception {
        // Given
        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenReturn(employeeCredentials);
        when(credentialsDao.saveEmployeeCredentials(any(EmployeeCredentials.class))).thenReturn(null);

        // When
        request.setPassword("Invalid Password");
        LoginResult result = JsonUtil.fromJson(loginHandler.handleRequest(request, null), LoginResult.class);

        // Then
        verify(credentialsDao).saveEmployeeCredentials(any(EmployeeCredentials.class));
        assertFalse(result.isLoginSuccess());
        assertEquals("jdoe", result.getUsername());
        assertEquals("Invalid Password!", result.getError());
    }

    @Test
    public void handleRequest_accountLockedException() throws Exception {
        // Given
        when(credentialsDao.getEmployeeCredentials(request.getUsername().toLowerCase())).thenThrow(new AccountLockedException("Account is locked"));

        // When
        LoginResult result = JsonUtil.fromJson(loginHandler.handleRequest(request, null), LoginResult.class);

        // Then
        assertFalse(result.isLoginSuccess());
        assertTrue(result.isAccountLocked());
        assertEquals("jdoe", result.getUsername());
        assertEquals("Account is locked", result.getError());
    }

    @Test
    public void handleRequest_usernameNotFoundException() throws Exception {
        // Given
        when(credentialsDao.getEmployeeCredentials(request.getUsername().toLowerCase())).thenThrow(new UsernameNotFoundException("Username not found"));

        // When
        LoginResult result = JsonUtil.fromJson(loginHandler.handleRequest(request, null), LoginResult.class);

        // Then
        assertFalse(result.isLoginSuccess());
        assertEquals("jdoe", result.getUsername());
        assertEquals("Username not found", result.getError());
    }

    @Test
    public void handleRequest_unexpectedException() throws Exception {
        // Given
        when(credentialsDao.getEmployeeCredentials(request.getUsername().toLowerCase())).thenThrow(new RuntimeException("Unexpected error"));

        // When
        LoginResult result = JsonUtil.fromJson(loginHandler.handleRequest(request, null), LoginResult.class);

        // Then
        assertFalse(result.isLoginSuccess());
        assertEquals("jdoe", result.getUsername());
        assertEquals("Unexpected error", result.getError());
    }
}
