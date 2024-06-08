package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.TimeEntryDao;
import org.example.dynamodb.model.TimeEntryModel;
import org.example.model.TimeEntry;
import org.example.model.requests.NewTimeEntryRequest;
import org.example.model.results.NewTimeEntryResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;

/**
 * Handler for creating a new TimeEntry in DynamoDB.
 * This class implements the AWS Lambda RequestHandler interface to handle new time entry requests.
 */
public class NewTimeEntryHandler implements RequestHandler<NewTimeEntryRequest, String> {
    private static final Logger log = LogManager.getLogger(NewTimeEntryHandler.class);

    @Inject
    TimeEntryDao timeEntryDao;

    /**
     * Default constructor that initializes the dependencies using Dagger.
     */
    public NewTimeEntryHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the incoming request to create a new time entry.
     *
     * @param request The request object containing the login details.
     * @param context The Lambda execution context.
     * @return A JSON string representing the result of the new time entry operation.
     */
    @Override
    public String handleRequest(NewTimeEntryRequest request, Context context) {
        try {
            TimeEntry newTimeEntry = TimeEntry.builder()
                    .withEmployeeId(request.getEmployeeId())
                    .build();

            TimeEntryModel result = timeEntryDao.saveTimeEntry(newTimeEntry);

            log.info(String.format("Successfully saved time entry. " +
                    "\nEmployee ID: %s \nEntry ID: %s ", result.getEmployeeId(), result.getEntryId()));

            return JsonUtil.createJsonResponse(
                    NewTimeEntryResult.builder()
                            .withNewTimeEntryCreated(true)
                            .withEmployeeId(result.getEmployeeId())
                            .withEntryId(result.getEntryId())
                            .withTimeIn(result.getTimeIn())
                            .build()
            );

        } catch (Exception e) {
            log.error("An unexpected error occurred while creating a new time entry. ", e);
            return JsonUtil.createJsonResponse(
                    NewTimeEntryResult.builder()
                            .withNewTimeEntryCreated(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withError(e.getMessage())
                            .build()
            );

        }
    }
}
