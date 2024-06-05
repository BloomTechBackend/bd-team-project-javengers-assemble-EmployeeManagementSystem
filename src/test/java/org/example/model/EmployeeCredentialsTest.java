package org.example.model;

import org.example.exceptions.AccountLockedException;
import org.example.utils.CredentialsUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class EmployeeCredentialsTest {
    private EmployeeCredentials employeeCredentials;
    private final String employeeId = "Emp123";
    private final String username = "testUser";
    private final String password = "password123";
    private final String salt = "randomSalt";
    private final String hashedPassword = "hashedPassword";
    private final LocalDateTime lastUpdated = LocalDateTime.of(2024,05,01,13,21,52);

    @BeforeEach
    public void setUp() {
        try (MockedStatic<CredentialsUtility> credentialsUtilityMock = mockStatic(CredentialsUtility.class)) {
            credentialsUtilityMock.when(CredentialsUtility::generateSalt).thenReturn(salt);
            credentialsUtilityMock.when(() -> CredentialsUtility.hashPassword(password, salt)).thenReturn(hashedPassword);
            employeeCredentials = new EmployeeCredentials(employeeId, username, password);
        }
    }

    @Test
    public void testConstructor_NewEmployee() {
        assertEquals(employeeId, employeeCredentials.getEmployeeId());
        assertEquals(username, employeeCredentials.getUsername());
        assertEquals(salt, employeeCredentials.getSalt());
        assertEquals(hashedPassword, employeeCredentials.getPassword());
        assertNotNull(employeeCredentials.getLastUpdated());
        assertFalse(employeeCredentials.isAccountLocked());
        assertTrue(employeeCredentials.isForceChangeAfterLogin());
    }

    @Test
    public void testConstructor_ExistingEmployee() {
        EmployeeCredentials existingEmployeeCredentials = new EmployeeCredentials(employeeId, username, salt,
                hashedPassword, lastUpdated, false, false, 1);

        assertEquals(employeeId, existingEmployeeCredentials.getEmployeeId());
        assertEquals(username, existingEmployeeCredentials.getUsername());
        assertEquals(salt, existingEmployeeCredentials.getSalt());
        assertEquals(hashedPassword, existingEmployeeCredentials.getPassword());
        assertEquals(lastUpdated, existingEmployeeCredentials.getLastUpdated());
        assertFalse(existingEmployeeCredentials.isAccountLocked());
        assertFalse(existingEmployeeCredentials.isForceChangeAfterLogin());
        assertEquals(1, existingEmployeeCredentials.getFailedAttempts());
    }

    @Test
    public void testUpdatePassword() {
        String newPassword = "newPassword123!";
        String newSalt = "newRandomSalt";
        String newHashedPassword = "newHashedPassword";

        try (MockedStatic<CredentialsUtility> credentialsUtilityMock = mockStatic(CredentialsUtility.class)) {
            credentialsUtilityMock.when(CredentialsUtility::generateSalt).thenReturn(newSalt);
            credentialsUtilityMock.when(() -> CredentialsUtility.hashPassword(newPassword, newSalt)).thenReturn(newHashedPassword);

            employeeCredentials.updatePassword(newPassword);

            assertEquals(newSalt, employeeCredentials.getSalt());
            assertEquals(newHashedPassword, employeeCredentials.getPassword());
            assertNotNull(employeeCredentials.getLastUpdated());
        }
    }

    @Test
    public void testVerifyCredentials_Success() {
        try (MockedStatic<CredentialsUtility> credentialsUtilityMock = mockStatic(CredentialsUtility.class)) {
            credentialsUtilityMock.when(() -> CredentialsUtility.verifyPassword(password, salt, hashedPassword)).thenReturn(true);

            boolean result = employeeCredentials.verifyCredentials(password);

            assertTrue(result);
            assertEquals(0, employeeCredentials.getFailedAttempts());
        }
    }

    @Test
    public void testVerifyCredentials_Failure() {
        try (MockedStatic<CredentialsUtility> credentialsUtilityMock = mockStatic(CredentialsUtility.class)) {
            credentialsUtilityMock.when(() -> CredentialsUtility.verifyPassword(password, salt, hashedPassword)).thenReturn(false);

            boolean result = employeeCredentials.verifyCredentials(password);

            assertFalse(result);
            assertEquals(1, employeeCredentials.getFailedAttempts());
        }
    }

    @Test
    public void testVerifyCredentials_AccountLocked() {
        employeeCredentials = new EmployeeCredentials(employeeId, username, salt, hashedPassword, lastUpdated,
                false, false, 4);

        AccountLockedException exception = assertThrows(AccountLockedException.class, () -> {
            employeeCredentials.verifyCredentials(password);
        });

        assertTrue(employeeCredentials.isAccountLocked());
        assertEquals("Account is locked due to too many failed login attempts.", exception.getMessage());
    }

    @Test
    public void testAdminResetPassword() {
        String newPassword = "newPassword123";
        String newSalt = "newRandomSalt";
        String newHashedPassword = "newHashedPassword";

        try (MockedStatic<CredentialsUtility> credentialsUtilityMock = mockStatic(CredentialsUtility.class)) {
            credentialsUtilityMock.when(CredentialsUtility::generateSalt).thenReturn(newSalt);
            credentialsUtilityMock.when(() -> CredentialsUtility.hashPassword(newPassword, newSalt)).thenReturn(newHashedPassword);

            employeeCredentials.adminResetPassword(newPassword);

            assertEquals(newSalt, employeeCredentials.getSalt());
            assertEquals(newHashedPassword, employeeCredentials.getPassword());
            assertNotNull(employeeCredentials.getLastUpdated());
            assertFalse(employeeCredentials.isAccountLocked());
            assertTrue(employeeCredentials.isForceChangeAfterLogin());
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        EmployeeCredentials credentials = new EmployeeCredentials(employeeId, username, salt, hashedPassword,
                lastUpdated, false, false, 0);
        EmployeeCredentials sameCredentials = new EmployeeCredentials(employeeId, username, salt, hashedPassword,
                lastUpdated, false, false, 0);

        assertEquals(credentials, sameCredentials);
        assertEquals(credentials.hashCode(), sameCredentials.hashCode());

        EmployeeCredentials differentCredentials = new EmployeeCredentials("DifferentId", username, salt,
                hashedPassword, lastUpdated, false, false, 0);

        assertNotEquals(credentials, differentCredentials);
        assertNotEquals(credentials.hashCode(), differentCredentials.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "EmployeeCredentials{" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nusername='" + username + '\'' +
                ", \nlastUpdated=" + employeeCredentials.getLastUpdated() +
                ", \naccountLocked=" + employeeCredentials.isAccountLocked() +
                ", \nforceChangeAfterLogin=" + employeeCredentials.isForceChangeAfterLogin() +
                ", \nfailedAttempts=" + employeeCredentials.getFailedAttempts() +
                "\n}";

        assertEquals(expectedString, employeeCredentials.toString());
    }
}
