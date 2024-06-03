package org.example.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "employee_time_entries")
public class TimeEntryModel {
    private String employeeId;
    private String entryId;
    private String timeIn;
    private String timeOut;
    private double duration;

    @DynamoDBHashKey(attributeName = "employee_id")
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @DynamoDBRangeKey(attributeName = "entry_id")
    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    @DynamoDBAttribute(attributeName = "time_in")
    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    @DynamoDBAttribute(attributeName = "time_out")
    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    @DynamoDBAttribute(attributeName = "duration")
    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeEntryModel)) return false;
        TimeEntryModel that = (TimeEntryModel) o;
        return Double.compare(getDuration(), that.getDuration()) == 0 &&
                Objects.equals(getEmployeeId(), that.getEmployeeId()) &&
                Objects.equals(getEntryId(), that.getEntryId()) &&
                Objects.equals(getTimeIn(), that.getTimeIn()) &&
                Objects.equals(getTimeOut(), that.getTimeOut());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getEntryId(), getTimeIn(), getTimeOut(), getDuration());
    }

    @Override
    public String toString() {
        return "TimeEntryModel{\n" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nentryId='" + entryId + '\'' +
                ", \ntimeIn='" + (timeIn != null ? timeIn : "") + '\'' +
                ", \ntimeOut='" + (timeOut != null ? timeOut : "") + '\'' +
                ", \nduration=" + duration +
                "\n}";
    }
}
