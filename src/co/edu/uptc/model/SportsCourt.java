package co.edu.uptc.model;

public class SportsCourt extends BookableResource{
    private int maxNumberOfPlayers;

    @Override 
    public void validate(User user, TimeRange range){
        user.
    }
    
    @Override 
    public ResourceType getType(){
        return ResourceType.COURT;
    }
}
