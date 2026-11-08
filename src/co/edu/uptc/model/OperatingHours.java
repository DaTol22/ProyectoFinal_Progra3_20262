package co.edu.uptc.model;

import java.time.LocalTime;

public class OperatingHours {
    private LocalTime opens;
    private LocalTime closes;
    private boolean openAllDay;

    public boolean allows(TimeRange range){
        return false;
    }
}
