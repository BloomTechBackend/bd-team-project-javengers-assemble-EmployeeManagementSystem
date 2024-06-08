package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.dynamodb.model.EmployeeCredentialsModel;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.AdminResetPasswordRequest;
import org.example.model.results.AdminResetPasswordResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;

/**
 * Handler for resetting employee passwords by an admin in DynamoDB.
 * This class implements the AWS Lambda RequestHandler interface to handle requests
 * for admin password resets.
 */
public class AdminResetPasswordHandler implements RequestHandler<AdminResetPasswordRequest, String> {
    private static final Logger log = LogManager.getLogger(AdminResetPasswordHandler.class);

    @Inject
    EmployeeCredentialsDao credentialsDao;

    /**
     * Default constructor that initializes the dependencies using Dagger.
     */
    public AdminResetPasswordHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the incoming request to reset an employee's password by an admin.
     *
     * @param request The request object containing the details of the password reset.
     * @param context The Lambda execution context.
     * @return A JSON string representing the result of the password reset operation.
     */
    @Override
    public String handleRequest(AdminResetPasswordRequest request, Context context) {
        try {
            EmployeeCredentials credentials = credentialsDao.getEmployeeCredentials(request.getUsername());

            credentials.adminResetPassword(request.getPassword());

            EmployeeCredentialsModel savedCredentials = credentialsDao.saveEmployeeCredentials(credentials);

            log.info(String.format("Successfully updated credentials. " +
                    "\nEmployee ID: %s ", savedCredentials.getEmployeeId()));

            return JsonUtil.createJsonResponse(
                    AdminResetPasswordResult.builder()
                            .withEmployeeCredentialsReset(true)
                            .withEmployeeId(savedCredentials.getEmployeeId())
                            .withUsername(savedCredentials.getUsername())
                            .withLastUpdated(savedCredentials.getLastUpdated())
                            .withAccountLocked(savedCredentials.isAccountLocked())
                            .withForceChangeAfterLogin(savedCredentials.isForceChangeAfterLogin())
                            .build()
            );

        } catch (UsernameNotFoundException e) {
            log.error(String.format("Username \"%s\" not found after an admin user attempted to reset an existing employee's password. " +
                    "Employee's information should have been sent from the Manage Employee's page, which loads all relevant employee data. \n" +
                    "Information provided in request: %s" +
                    "\nError: \n", request.getUsername(), request), e);
            return JsonUtil.createJsonResponse(
                    AdminResetPasswordResult.builder()
                            .withEmployeeCredentialsReset(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withUsername(request.getUsername())
                            .withError(e.getMessage())
                            .build()
            );

        } catch (Exception e) {
            log.error("An unexpected error occurred. ", e);
            return JsonUtil.createJsonResponse(
                    AdminResetPasswordResult.builder()
                            .withEmployeeCredentialsReset(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withUsername(request.getUsername())
                            .withError(e.getMessage())
                            .build()
            );

        }
    }
}
