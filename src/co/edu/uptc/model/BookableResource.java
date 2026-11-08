package co.edu.uptc.model;

import java.util.List;

public abstract class BookableResource {
    private String name;
    private String id;
    private List<Reservation> reservations;
    private OperatingHours hours;

    public boolean isAvailable(TimeRange range){
        return false;
    }

    public abstract void validate(User user, TimeRange range);

    public ResourceType getType(){
        return null;
    }

    private boolean isOpenDuring(TimeRange range){
        return false;
    }

    private boolean hasConflict(TimeRange range){
        return false;
    }
}
