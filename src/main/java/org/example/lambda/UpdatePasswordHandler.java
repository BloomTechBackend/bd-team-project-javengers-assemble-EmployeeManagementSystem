package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.dynamodb.model.EmployeeCredentialsModel;
import org.example.exceptions.InvalidInputFormatException;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.UpdateCredentialsRequest;
import org.example.model.results.UpdateCredentialsResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;

/**
 * Handler for processing password update requests in DynamoDB.
 * This class implements the AWS Lambda RequestHandler interface to handle password update requests.
 */
public class UpdatePasswordHandler implements RequestHandler<UpdateCredentialsRequest, String> {
    private static final Logger log = LogManager.getLogger(UpdatePasswordHandler.class);

    @Inject
    EmployeeCredentialsDao credentialsDao;


    /**
     * Default constructor that initializes the dependencies using Dagger.
     */
    public UpdatePasswordHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the incoming request to change an employee's password.
     *
     * @param request The request object containing the new password details.
     * @param context The Lambda execution context.
     * @return A JSON string representing the result of the password change operation.
     */
    @Override
    public String handleRequest(final UpdateCredentialsRequest request, Context context) {

        try {
            EmployeeCredentials credentials = credentialsDao.getEmployeeCredentials(request.getUsername());

            credentials.updatePassword(request.getPassword());

            EmployeeCredentialsModel savedCredentials = credentialsDao.saveEmployeeCredentials(credentials);

            log.info(String.format("Successfully updated credentials. " +
                    "\nEmployee ID: %s ", savedCredentials.getEmployeeId()));

            return JsonUtil.createJsonResponse(
                    UpdateCredentialsResult.builder()
                            .withCredentialsUpdated(true)
                            .withEmployeeId(savedCredentials.getEmployeeId())
                            .withUsername(savedCredentials.getUsername())
                            .withLastUpdated(savedCredentials.getLastUpdated())
                            .withAccountLocked(savedCredentials.isAccountLocked())
                            .withForceChangeAfterLogin(savedCredentials.isForceChangeAfterLogin())
                            .withFailedAttempts(savedCredentials.getFailedAttempts())
                            .build()
            );

        } catch (InvalidInputFormatException e) {
            log.warn("Invalid password format. ", e);
            return JsonUtil.createJsonResponse(
                    UpdateCredentialsResult.builder()
                            .withCredentialsUpdated(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withUsername(request.getUsername())
                            .withError(e.getMessage())
                            .build()
            );

        } catch (Exception e) {
            log.error("An unexpected error occurred while creating credentials. ", e);
            return JsonUtil.createJsonResponse(
                    UpdateCredentialsResult.builder()
                            .withCredentialsUpdated(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withUsername(request.getUsername())
                            .withError(e.getMessage())
                            .build()
            );

        }
    }

}