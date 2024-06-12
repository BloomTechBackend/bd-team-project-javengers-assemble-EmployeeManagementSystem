package org.example.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

public class TimeEntry {
    private static final LocalDateTime NULL_DATE_TIME = LocalDateTime.of(9999, 1, 1, 00,00,00);

    private final String employeeId;
    private final String entryId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private double duration;

    private TimeEntry(TimeEntryBuilder timeEntryBuilder) {
        this.employeeId = timeEntryBuilder.employeeId;
        this.entryId = timeEntryBuilder.entryId;
        this.timeIn = timeEntryBuilder.timeIn;
        this.timeOut = timeEntryBuilder.timeOut;
        this.duration = timeEntryBuilder.duration;
        calculateDuration();
    }

    public static TimeEntryBuilder builder() {
        return new TimeEntryBuilder();
    }

    /**
     * Calculates the duration between the time an employee clocked in and the time they clocked out.
     * If the employee has not clocked out, the duration is set to zero.
     * If the employee has clocked out, the duration is calculated from the clock-in time to the clock-out time.
     * The duration is returned in hours as a double, including fractional hours.
     *
     * @return the duration between timeIn and timeOut (or zero if timeOut is null) in hours.
     */
    public double calculateDuration() {
        if (timeOut == null) {
            duration = 0.0;
        } else {
            duration = Duration.between(timeIn, timeOut).toMinutes() / 60.0;
        }
        return duration;
    }

    public void recordTimeOut() {
        this.timeOut = getCurrentTimeStamp();
        calculateDuration();
    }

    public static class TimeEntryBuilder {
        private String employeeId;
        private String entryId;
        private LocalDateTime timeIn;
        private LocalDateTime timeOut;
        private double duration;

        public TimeEntryBuilder() {
            this.entryId = UUID.randomUUID().toString();
            this.timeIn = getCurrentTimeStamp();
        }

        public TimeEntryBuilder withEmployeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public TimeEntryBuilder withEntryId(String entryId) {
            this.entryId = entryId;
            return this;
        }

        public TimeEntryBuilder withTimeIn(LocalDateTime timeIn) {
            this.timeIn = timeIn;
            return this;
        }

        public TimeEntryBuilder withTimeOut(LocalDateTime timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public TimeEntryBuilder withDuration(double duration) {
            this.duration = duration;
            return this;
        }

        public TimeEntry build() {
            return new TimeEntry(this);
        }
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEntryId() {
        return entryId;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
        calculateDuration();
    }

    public double getDuration() {
        calculateDuration();
        return duration;
    }

    private static LocalDateTime getCurrentTimeStamp() {
        return LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeEntry)) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return Double.compare(getDuration(), timeEntry.getDuration()) == 0 &&
                Objects.equals(getEmployeeId(), timeEntry.getEmployeeId()) &&
                Objects.equals(getEntryId(), timeEntry.getEntryId()) &&
                Objects.equals(getTimeIn(), timeEntry.getTimeIn()) &&
                Objects.equals(getTimeOut(), timeEntry.getTimeOut());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getEntryId(), getTimeIn(), getTimeOut(), getDuration());
    }

    @Override
    public String toString() {
        return "TimeEntry{\n" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nentryId='" + entryId + '\'' +
                ", \ntimeIn=" + (timeIn != null ? timeIn : NULL_DATE_TIME) +
                ", \ntimeOut=" + (timeOut != null ? timeOut : NULL_DATE_TIME) +
                ", \nduration=" + duration +
                "\n}";
    }
}
