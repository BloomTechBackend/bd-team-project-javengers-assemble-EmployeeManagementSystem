package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.TimeEntryDao;
import org.example.dynamodb.model.TimeEntryModel;
import org.example.model.TimeEntry;
import org.example.model.requests.UpdateTimeEntryRequest;
import org.example.model.results.UpdateTimeEntryResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;

/**
 * Handler for updating a single time entry in DynamoDB.
 * This class implements the AWS Lambda RequestHandler interface to handle requests
 * for updating time entries.
 */
public class UpdateTimeEntryHandler implements RequestHandler<UpdateTimeEntryRequest, String> {
    private static final Logger log = LogManager.getLogger(UpdateTimeEntryHandler.class);

    @Inject
    TimeEntryDao timeEntryDao;

    /**
     * Default constructor that initializes the dependencies using Dagger.
     */
    public UpdateTimeEntryHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the incoming request to update a time entry.
     *
     * @param request The request object containing the details of the time entry to update.
     * @param context The Lambda execution context.
     * @return A JSON string representing the result of the update operation.
     */
    @Override
    public String handleRequest(UpdateTimeEntryRequest request, Context context) {
        try {
            TimeEntry updatedTimeEntry = TimeEntry.builder()
                    .withEmployeeId(request.getEmployeeId())
                    .withEntryId(request.getEntryId())
                    .withTimeIn(request.getTimeIn())
                    .withTimeOut(request.getTimeOut())
                    .withDuration(request.getDuration())
                    .build();

            if (request.isEmployeeClockOut()) {
                updatedTimeEntry.recordTimeOut();
            }

            TimeEntryModel savedTimeEntry = timeEntryDao.saveTimeEntry(updatedTimeEntry);

            log.info(String.format("Successfully updated time entry. " +
                    "\nEmployee ID: %s \nEntry ID: %s ", savedTimeEntry.getEmployeeId(), savedTimeEntry.getEntryId()));

            return JsonUtil.createJsonResponse(
                    UpdateTimeEntryResult.builder()
                            .withTimeUpdated(true)
                            .withEmployeeId(savedTimeEntry.getEmployeeId())
                            .withEntryId(savedTimeEntry.getEntryId())
                            .withTimeIn(savedTimeEntry.getTimeIn())
                            .withTimeOut(savedTimeEntry.getTimeOut())
                            .withDuration(savedTimeEntry.getDuration())
                            .build()
            );

        } catch (Exception e) {
            log.error("There was an error while updating the time entry. ", e);

            return JsonUtil.createJsonResponse(
                    UpdateTimeEntryResult.builder()
                            .withTimeUpdated(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withEntryId(request.getEntryId())
                            .withTimeIn(request.getTimeIn().toString())
                            .withTimeOut(request.getTimeOut().toString())
                            .withDuration(request.getDuration())
                            .withError(e.getMessage())
                            .build()
            );
        }
    }
}
