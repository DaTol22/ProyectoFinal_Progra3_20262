package co.edu.uptc.model;

import java.time.Duration;

public class StudyRoom extends BookableResource{
    private Duration slotDuration;

    @Override 
    public void validate(User user, TimeRange range){
        if(range.getDuration().compareTo(slotDuration) > 0){ //TODO aqui es donde esta el tema de la excepcion
            return "cagaste pa no se pudo";
        }
    }

    @Override 
    public ResourceType getType(){
        return ResourceType.STUDY_ROOM;
    }
    
}
