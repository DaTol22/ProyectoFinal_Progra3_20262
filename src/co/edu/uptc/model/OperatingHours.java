package co.edu.uptc.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class OperatingHours {
    private LocalTime opens;
    private LocalTime closes;
    private boolean openAllDay;

    public boolean allows(TimeRange range){
        if (openAllDay) {
            return true;
        }
        LocalDateTime start = range.getStartTime();
        LocalDateTime end = range.getEndTime();
        if (!start.toLocalDate().equals(end.toLocalDate())) {
            return false;                  
        }
        return !start.toLocalTime().isBefore(opens) && !end.toLocalTime().isAfter(closes);
    }
}
