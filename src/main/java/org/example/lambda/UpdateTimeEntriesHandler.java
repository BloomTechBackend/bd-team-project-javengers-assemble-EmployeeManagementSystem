package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.TimeEntryDao;
import org.example.dynamodb.model.TimeEntryModel;
import org.example.model.requests.UpdateTimeEntriesRequest;
import org.example.model.results.UpdateTimeEntriesResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;
import java.util.List;

/**
 * Handler for updating time entries in DynamoDB.
 * This class implements the AWS Lambda RequestHandler interface to handle requests
 * for updating time entries.
 */
public class UpdateTimeEntriesHandler implements RequestHandler<UpdateTimeEntriesRequest, String> {
    private static final Logger log = LogManager.getLogger(UpdateTimeEntriesHandler.class);
    @Inject
    TimeEntryDao timeEntryDao;

    /**
     * Default constructor that initializes the dependencies using Dagger.
     */
    public UpdateTimeEntriesHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the incoming request to update a list of time entries.
     *
     * @param request The request object containing the details of the time entries to update.
     * @param context The Lambda execution context.
     * @return A JSON string representing the result of the update operation.
     */
    @Override
    public String handleRequest(UpdateTimeEntriesRequest request, Context context) {
        try {
            List<TimeEntryModel> savedTimeEntries = timeEntryDao.saveTimeEntries(request.getTimeEntryList());

            log.info(String.format("Successfully updated list of time entries. " +
                    "\nEmployee ID: %s ", request.getEmployeeId()));

            return JsonUtil.createJsonResponse(
                    UpdateTimeEntriesResult.builder()
                            .withTimeEntriesUpdated(true)
                            .withEmployeeId(request.getEmployeeId())
                            .withTimeEntryList(savedTimeEntries)
                            .build()
            );

        } catch (Exception e) {
            log.error(String.format("An error occurred while updating time entries for employee ID \"%s\".. ", request.getEmployeeId()), e);

            return JsonUtil.createJsonResponse(
                    UpdateTimeEntriesResult.builder()
                            .withTimeEntriesUpdated(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withError(e.getMessage())
                            .build()
            );
        }
    }
}
