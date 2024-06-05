package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.AdminResetPasswordRequest;
import org.example.model.results.AdminResetPasswordResult;

import javax.inject.Inject;

public class AdminResetPasswordHandler implements RequestHandler<AdminResetPasswordRequest, AdminResetPasswordResult> {
    private static final Logger log = LogManager.getLogger(AdminResetPasswordHandler.class);

    @Inject
    EmployeeCredentialsDao credentialsDao;

    public AdminResetPasswordHandler() {
        DaggerAppComponent.create().inject(this);
    }

    @Override
    public AdminResetPasswordResult handleRequest(AdminResetPasswordRequest request, Context context) {
        try {
            EmployeeCredentials credentials = credentialsDao.getEmployeeCredentials(request.getUsername());

            credentials.adminResetPassword(request.getPassword());

            credentialsDao.saveEmployeeCredentials(credentials);

            return AdminResetPasswordResult.builder()
                    .withEmployeeCredentialsReset(true)
                    .withEmployeeId(credentials.getEmployeeId())
                    .withUsername(credentials.getUsername())
                    .withLastUpdated(credentials.getLastUpdated())
                    .withAccountLocked(credentials.isAccountLocked())
                    .withForceChangeAfterLogin(credentials.isForceChangeAfterLogin())
                    .build();

        } catch (UsernameNotFoundException e) {
            log.error(String.format("Username \"%s\" not found after an admin user attempted to reset an existing employee's password. " +
                    "Employee's information should have been sent from the Manage Employee's page, which loads all relevant employee data. \n" +
                    "Information provided in request: %s" +
                    "\nError: \n", request.getUsername(), request), e);
            return AdminResetPasswordResult.builder()
                    .withEmployeeCredentialsReset(false)
                    .withEmployeeId(request.getEmployeeId())
                    .withUsername(request.getUsername())
                    .withError(e.getMessage())
                    .build();

        } catch (Exception e) {
            log.error("An unexpected error occurred. ", e);
            return AdminResetPasswordResult.builder()
                    .withEmployeeCredentialsReset(false)
                    .withEmployeeId(request.getEmployeeId())
                    .withUsername(request.getUsername())
                    .withError(e.getMessage())
                    .build();

        }


    }
}
