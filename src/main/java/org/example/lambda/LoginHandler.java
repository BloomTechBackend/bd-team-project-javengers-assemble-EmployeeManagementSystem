package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.exceptions.AccountLockedException;
import org.example.exceptions.InvalidEmployeeCredentialsException;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.EmployeeCredentials;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.requests.LoginRequest;
import org.example.model.results.LoginResult;

public class LoginHandler implements RequestHandler<LoginRequest, LoginResult> {
    private static final Logger log = LogManager.getLogger(LoginHandler.class);

    @Inject
    EmployeeCredentialsDao credentialsDao;

    public LoginHandler() {
        DaggerAppComponent.create().inject(this);
    }

    @Override
    public LoginResult handleRequest(final LoginRequest request, Context context) {
        try {
            String formattedUsername = request.getUsername().toLowerCase();

            // Retrieve Employee Credentials
            EmployeeCredentials employeeCredentials = credentialsDao.getEmployeeCredentials(formattedUsername);

            // Compare Credentials
            boolean credentialsComparisonResult = employeeCredentials.verifyCredentials(request.getPassword());

            if (!credentialsComparisonResult) {
                credentialsDao.saveEmployeeCredentials(employeeCredentials);
                if (employeeCredentials.getFailedAttempts() > 3) {
                    throw new AccountLockedException("Account is locked due to too many failed login attempts.");
                }
                throw new InvalidEmployeeCredentialsException("Invalid Password!");
            }

            log.info("User login successful. Username: " + employeeCredentials.getUsername());

            return LoginResult.build()
                    .withLoginSuccess(true)
                    .withUsername(employeeCredentials.getUsername())
                    .withEmployeeId(employeeCredentials.getEmployeeId())
                    .withForceChangeAfterLogin(employeeCredentials.isForceChangeAfterLogin())
                    .build();

        } catch (AccountLockedException e) {
            log.warn(e.getMessage());

            return LoginResult.build()
                    .withLoginSuccess(false)
                    .withUsername(request.getUsername())
                    .withAccountLocked(true)
                    .withError(e.getMessage())
                    .build();

        } catch (UsernameNotFoundException | InvalidEmployeeCredentialsException e) {
            log.warn(e.getMessage());

            return LoginResult.build()
                    .withLoginSuccess(false)
                    .withUsername(request.getUsername())
                    .withError(e.getMessage())
                    .build();

        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to log in.", e);

            return LoginResult.build()
                    .withLoginSuccess(false)
                    .withUsername(request.getUsername())
                    .withError(e.getMessage())
                    .build();
        }
    }
}
