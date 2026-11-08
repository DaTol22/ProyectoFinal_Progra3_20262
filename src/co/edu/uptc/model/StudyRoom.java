package co.edu.uptc.model;

import java.time.Duration;

public class StudyRoom extends BookableResource{
    private Duration slotDuration;

    @Override 
    public void validate(User user, TimeRange range){

    }
    
}
