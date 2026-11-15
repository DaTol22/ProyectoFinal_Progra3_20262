package co.edu.uptc.model;

import co.edu.uptc.enums.ResourceType;

public class SportsCourt extends BookableResource{
    private int maxNumberOfPlayers;

    public SportsCourt(String name, String id, OperatingHours hours) {
        super(name, id, hours);
    }

    @Override 
    public void validate(User user, TimeRange range){
        user.
    }
    
    @Override 
    public ResourceType getType(){
        return ResourceType.COURT;
    }
}
