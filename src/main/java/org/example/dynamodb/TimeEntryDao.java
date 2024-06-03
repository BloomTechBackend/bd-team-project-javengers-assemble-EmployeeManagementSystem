package org.example.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dynamodb.model.TimeEntryModel;
import org.example.exceptions.TimeEntriesNotFoundException;
import org.example.model.TimeEntry;
import org.example.utils.ModelConverter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeEntryDao {
    private static final Logger log = LogManager.getLogger(TimeEntryDao.class);
    private final DynamoDBMapper dynamoDBMapper;

    public TimeEntryDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Retrieves a single time entry for an employee using the specified employee ID and entry ID.
     *
     * @param employeeId the ID of the employee.
     * @param entryId the ID of the time entry.
     * @return the TimeEntry object corresponding to the specified employee ID and entry ID.
     * @throws TimeEntriesNotFoundException if no time entry is found for the specified employee ID and entry ID.
     */
    public TimeEntry getTimeEntry(String employeeId, String entryId) {
        try {
            TimeEntryModel timeEntryModel = this.dynamoDBMapper.load(TimeEntryModel.class, employeeId, entryId);

            if (timeEntryModel == null) {
                throw new TimeEntriesNotFoundException("Could not find time entry with Employee ID: " + employeeId + " and Entry ID: " + entryId);
            }

            TimeEntry timeEntry = ModelConverter.fromTimeEntryModel(timeEntryModel);
            log.info("Successfully retrieved TimeEntry with Entry ID \"{}\" for Employee ID \"{}\".", entryId, employeeId);
            return timeEntry;
        } catch (TimeEntriesNotFoundException e) {
            log.warn("TimeEntry for Entry ID \"{}\" for Employee ID \"{}\" not found in database.", entryId, employeeId);
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving TimeEntry Entry ID \"{}\"} for Employee ID \"{}\". ", entryId, employeeId, e);
            throw e;
        }
    }

    /**
     * Retrieves the last five time entries for an employee based on the time_in timestamp.
     *
     * @param employeeId the ID of the employee.
     * @return a list of TimeEntry objects representing the last five time entries for the specified employee ID.
     * @throws TimeEntriesNotFoundException if no time entries are found for the specified employee ID.
     */
    public List<TimeEntry> getLastFiveTimeEntries(String employeeId) {
        try {
            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":employeeId", new AttributeValue().withS(employeeId));

            DynamoDBQueryExpression<TimeEntryModel> queryExpression = new DynamoDBQueryExpression<TimeEntryModel>()
                    .withKeyConditionExpression("employeeId = :employeeId")
                    .withExpressionAttributeValues(eav)
                    .withLimit(5);

            List<TimeEntryModel> timeEntryModels = dynamoDBMapper.query(TimeEntryModel.class, queryExpression);

            if (timeEntryModels.isEmpty()) {
                throw new TimeEntriesNotFoundException("Could not find time entries with Employee ID: " + employeeId);
            }

            List<TimeEntry> timeEntryList = ModelConverter.fromTimeEntryModelList(timeEntryModels);

            // Sort time entries based on time_in timestamp in descending order
            timeEntryList.sort(Comparator.comparing(TimeEntry::getTimeIn).reversed());

            log.info("Successfully retrieved last 5 Time Entries for Employee ID \"{}\".", employeeId);
            return timeEntryList;
        } catch (TimeEntriesNotFoundException e) {
            log.warn("Time Entries for Employee ID \"{}\" not found.", employeeId);
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving time entries for Employee ID \"{}\". ", employeeId, e);
            throw e;
        }
    }


    /**
     * Retrieves all time entries for an employee using the specified employee ID.
     *
     * @param employeeId the ID of the employee.
     * @return a list of TimeEntry objects corresponding to the specified employee ID.
     * @throws TimeEntriesNotFoundException if no time entries are found for the specified employee ID.
     */
    public List<TimeEntry> getTimeEntries(String employeeId) {
        try {
            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":employeeId", new AttributeValue().withS(employeeId));

            DynamoDBQueryExpression<TimeEntryModel> queryExpression = new DynamoDBQueryExpression<TimeEntryModel>()
                    .withKeyConditionExpression("employeeId = :employeeId")
                    .withExpressionAttributeValues(eav);

            List<TimeEntryModel> timeEntryModels = dynamoDBMapper.query(TimeEntryModel.class, queryExpression);

            if (timeEntryModels.isEmpty()) {
                throw new TimeEntriesNotFoundException("Could not find time entries with Employee ID: " + employeeId);
            }

            List<TimeEntry> timeEntries = ModelConverter.fromTimeEntryModelList(timeEntryModels);

            log.info("Successfully retrieved time entries for Employee ID \"{}\". ", employeeId);
            return timeEntries;
        } catch (TimeEntriesNotFoundException e) {
            log.warn("Time Entries for Employee ID \"{}\" not found.", employeeId);
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving time entries for Employee ID \"{}\". ", employeeId, e);
            throw e;
        }
    }

    /**
     * Saves a single time entry to the DynamoDB table.
     *
     * @param timeEntry the TimeEntry object to be saved.
     * @return the saved TimeEntryModel object.
     */
    public TimeEntryModel saveTimeEntry(TimeEntry timeEntry) {
        TimeEntryModel timeEntryModel;
        try {
            timeEntryModel = ModelConverter.fromTimeEntry(timeEntry);
            dynamoDBMapper.save(timeEntryModel);

            log.info("Successfully saved time entry to table. " + timeEntry);
            return timeEntryModel;
        } catch (Exception e) {
            log.error("There was an error while saving: ", e);
            throw e;
        }
    }

    /**
     * Saves multiple time entries to the DynamoDB table in a single batch operation.
     *
     * @param timeEntries a list of TimeEntry objects to be saved.
     */
    public List<TimeEntryModel> saveTimeEntries(List<TimeEntry> timeEntries) {
        try {
            List<TimeEntryModel> timeEntryModelList = ModelConverter.fromTimeEntryList(timeEntries);
            dynamoDBMapper.batchSave(timeEntryModelList);

            return timeEntryModelList;
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}
