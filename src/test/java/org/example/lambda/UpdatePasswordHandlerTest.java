package org.example.lambda;

import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.exceptions.InvalidInputFormatException;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.UpdateCredentialsRequest;
import org.example.model.results.UpdateCredentialsResult;
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

public class UpdatePasswordHandlerTest {
    private AutoCloseable mocks;
    @InjectMocks
    UpdatePasswordHandler updatePasswordHandler;

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

        UpdateCredentialsRequest request = new UpdateCredentialsRequest();
        request.setEmployeeId(employeeCredentials.getEmployeeId());
        request.setUsername(employeeCredentials.getUsername());
        request.setPassword("NewPassword1!");

        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenReturn(employeeCredentials);
        when(credentialsDao.saveEmployeeCredentials(any(EmployeeCredentials.class)))
                .thenAnswer(invocation -> {
                    EmployeeCredentials arg = invocation.getArgument(0);
                    return ModelConverter.fromEmployeeCredentials(arg);
                });

        // When
        UpdateCredentialsResult result = JsonUtil.fromJson(updatePasswordHandler.handleRequest(request, null), UpdateCredentialsResult.class);

        //Then
        verify(credentialsDao).saveEmployeeCredentials(employeeCredentials);

        assertNotEquals(originalPassword, employeeCredentials.getPassword(), "New password should not match the old password after update.");
        assertTrue(result.isCredentialsUpdated(), "credentialsUpdated variable should be true");
        assertEquals(result.getEmployeeId(), employeeCredentials.getEmployeeId(), "Result employeeId should match the EmployeeCredentials instance employeeId.");
        assertEquals(result.getUsername(), employeeCredentials.getUsername(), "Result username should match the EmployeeCredentials instance username.");
        assertNotEquals(originalPassword, employeeCredentials.getLastUpdated().toString(), "lastUpdated should not be equal after password update.");
        assertEquals(result.getLastUpdated(), employeeCredentials.getLastUpdated().toString(), "Result lastUpdated should match the EmployeeCredentials instance lastUpdated.");
        assertFalse(result.isAccountLocked(), "Result accountLocked should be false.");
        assertEquals(result.isAccountLocked(), employeeCredentials.isAccountLocked(), "Result accountLocked should match the EmployeeCredentials instance accountLocked");
        assertFalse(result.isForceChangeAfterLogin(), "Result forceChangeAfterLogin should be set to false");
        assertEquals(result.isForceChangeAfterLogin(), employeeCredentials.isForceChangeAfterLogin(), "Result forceChangeAfterLogin should match the EmployeeCredentials instance forceChangeAfterLogin");
    }

    @Test
    public void handleRequest_validRequest_throwsInvalidInputFormatException() {
        // Given
        UpdateCredentialsRequest request = new UpdateCredentialsRequest();
        request.setUsername(employeeCredentials.getUsername());
        request.setEmployeeId(employeeCredentials.getEmployeeId());
        request.setPassword("Invalid Password");

        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenThrow(new InvalidInputFormatException("Invalid password format. "));

        // When
        UpdateCredentialsResult result = JsonUtil.fromJson(updatePasswordHandler.handleRequest(request, null), UpdateCredentialsResult.class);

        // Then
        assertFalse(result.isCredentialsUpdated());
        assertEquals(request.getEmployeeId(), result.getEmployeeId());
        assertEquals(request.getUsername(), result.getUsername());
        assertEquals("Invalid password format. ", result.getError());
    }

    @Test
    public void handleRequest_validRequest_throwsException() {
        // Given
        UpdateCredentialsRequest request = new UpdateCredentialsRequest();
        request.setUsername(employeeCredentials.getUsername());
        request.setEmployeeId(employeeCredentials.getEmployeeId());
        request.setPassword("NewPassword");

        when(credentialsDao.getEmployeeCredentials(request.getUsername())).thenThrow(new RuntimeException("An unexpected error occurred. "));

        // When
        UpdateCredentialsResult result = JsonUtil.fromJson(updatePasswordHandler.handleRequest(request, null), UpdateCredentialsResult.class);

        // Then
        assertFalse(result.isCredentialsUpdated());
        assertEquals(request.getEmployeeId(), result.getEmployeeId());
        assertEquals(request.getUsername(), result.getUsername());
        assertEquals("An unexpected error occurred. ", result.getError());
    }
}
