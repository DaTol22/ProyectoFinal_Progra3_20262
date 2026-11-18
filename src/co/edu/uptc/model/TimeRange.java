package co.edu.uptc.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeRange {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public TimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if(startTime.isBefore(endTime)){
            this.startTime = startTime;
            this.endTime = endTime;
        }else{
            throw new IllegalArgumentException("el tiempo de inicio debe ser antes que el de fin");
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean overlaps(TimeRange other){
        return startTime.isBefore(other.endTime) && other.startTime.isBefore(endTime);
    }

    public boolean contains(LocalDateTime t){
    return !t.isBefore(startTime) && t.isBefore(endTime);   
}

    public Duration getDuration(){
        return Duration.between(startTime, endTime);
    }
}
