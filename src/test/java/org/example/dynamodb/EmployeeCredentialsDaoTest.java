package org.example.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.example.dynamodb.model.EmployeeCredentialsModel;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.EmployeeCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class EmployeeCredentialsDaoTest {
    AutoCloseable mocks;
    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private EmployeeCredentialsDao employeeCredentialsDao;

    private EmployeeCredentialsModel employeeCredentialsModel;
    private EmployeeCredentials employeeCredentials;

    private final String username = "testUser";
    private final String employeeId = "Emp123";
    private final String salt = "randomSalt";
    private final String password = "hashedPassword";
    private final LocalDateTime lastUpdated = LocalDateTime.now();
    private final boolean accountLocked = false;
    private final boolean forceChangeAfterLogin = true;
    private final int failedAttempts = 0;

    @BeforeEach
    public void setUp() {
        mocks = openMocks(this);
        employeeCredentialsModel = new EmployeeCredentialsModel();
        employeeCredentialsModel.setUsername(username);
        employeeCredentialsModel.setEmployeeId(employeeId);
        employeeCredentialsModel.setSalt(salt);
        employeeCredentialsModel.setPassword(password);
        employeeCredentialsModel.setLastUpdated(lastUpdated.toString());
        employeeCredentialsModel.setAccountLocked(accountLocked);
        employeeCredentialsModel.setForceChangeAfterLogin(forceChangeAfterLogin);
        employeeCredentialsModel.setFailedAttempts(failedAttempts);

        employeeCredentials = new EmployeeCredentials(employeeId, username, salt, password, lastUpdated, accountLocked, forceChangeAfterLogin, failedAttempts);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void getEmployeeCredentials_returnsEmployeeCredentials() {
        when(dynamoDBMapper.load(EmployeeCredentialsModel.class, username)).thenReturn(employeeCredentialsModel);

        EmployeeCredentials result = employeeCredentialsDao.getEmployeeCredentials(username);

        assertNotNull(result);
        assertEquals(employeeCredentials, result);
        verify(dynamoDBMapper, times(1)).load(EmployeeCredentialsModel.class, username);
    }

    @Test
    public void getEmployeeCredentials_throwsUsernameNotFoundException() {
        when(dynamoDBMapper.load(EmployeeCredentialsModel.class, username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> employeeCredentialsDao.getEmployeeCredentials(username));
        verify(dynamoDBMapper, times(1)).load(EmployeeCredentialsModel.class, username);
    }

    @Test
    public void saveEmployeeCredentials_savesEmployeeCredentials() {
        doNothing().when(dynamoDBMapper).save(any(EmployeeCredentialsModel.class));

        EmployeeCredentialsModel result = employeeCredentialsDao.saveEmployeeCredentials(employeeCredentials);

        assertNotNull(result);
        assertEquals(employeeCredentialsModel.getUsername(), result.getUsername());
        verify(dynamoDBMapper, times(1)).save(any(EmployeeCredentialsModel.class));
    }

    @Test
    public void saveEmployeeCredentials_throwsException() {
        doThrow(new RuntimeException("Error saving")).when(dynamoDBMapper).save(any(EmployeeCredentialsModel.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> employeeCredentialsDao.saveEmployeeCredentials(employeeCredentials));
        assertEquals("Error saving", exception.getMessage());
        verify(dynamoDBMapper, times(1)).save(any(EmployeeCredentialsModel.class));
    }
}
