package co.edu.uptc.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class OperatingHours {
    private LocalTime opens;
    private LocalTime closes;
    private boolean openAllDay;

    public OperatingHours(LocalTime opens, LocalTime closes){
        this.opens = opens;
        this.closes = closes;
        if(opens.equals(LocalTime.of(0, 0)) && closes.equals(LocalTime.of(23, 59))){
            openAllDay = true;
        }
    }

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

    public LocalTime getCloses() {
        return closes;
    }

    public LocalTime getOpens() {
        return opens;
    }
}
