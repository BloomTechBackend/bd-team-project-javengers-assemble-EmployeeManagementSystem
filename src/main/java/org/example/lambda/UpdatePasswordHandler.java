package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.exceptions.InvalidInputFormatException;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.UpdateCredentialsRequest;
import org.example.model.results.UpdateCredentialsResult;

import javax.inject.Inject;

public class UpdatePasswordHandler implements RequestHandler<UpdateCredentialsRequest, UpdateCredentialsResult> {
    private static final Logger log = LogManager.getLogger(UpdatePasswordHandler.class);

    @Inject
    EmployeeCredentialsDao credentialsDao;

    public UpdatePasswordHandler() {
        DaggerAppComponent.create().inject(this);
    }

    @Override
    public UpdateCredentialsResult handleRequest(final UpdateCredentialsRequest request, Context context) {

        try {
            EmployeeCredentials credentials = credentialsDao.getEmployeeCredentials(request.getUsername());

            credentials.updatePassword(request.getPassword());

            credentialsDao.saveEmployeeCredentials(credentials);


            return UpdateCredentialsResult.builder()
                    .withCredentialsUpdated(true)
                    .withEmployeeId(credentials.getEmployeeId())
                    .withUsername(credentials.getUsername())
                    .withLastUpdated(credentials.getLastUpdated())
                    .withAccountLocked(credentials.isAccountLocked())
                    .withForceChangeAfterLogin(credentials.isForceChangeAfterLogin())
                    .withFailedAttempts(credentials.getFailedAttempts())
                    .build();

        } catch (InvalidInputFormatException e) {
            log.warn("Invalid password format. ", e);
            return UpdateCredentialsResult.builder()
                    .withCredentialsUpdated(false)
                    .withEmployeeId(request.getEmployeeId())
                    .withUsername(request.getUsername())
                    .withError(e.getMessage())
                    .build();

        } catch (Exception e) {
            log.error("An unexpected error occurred while creating credentials. ", e);
            return UpdateCredentialsResult.builder()
                    .withCredentialsUpdated(false)
                    .withEmployeeId(request.getEmployeeId())
                    .withUsername(request.getUsername())
                    .withError(e.getMessage())
                    .build();

        }
    }

}