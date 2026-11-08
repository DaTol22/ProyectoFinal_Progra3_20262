package co.edu.uptc.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeRange {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public boolean overlaps(TimeRange other){
        return false;
    }

    public boolean contains(LocalDateTime t){
        return false;
    }

    public Duration getDuration(){
        return null;
    }
}
