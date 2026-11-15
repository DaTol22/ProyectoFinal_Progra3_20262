package co.edu.uptc.model;

import co.edu.uptc.structures.SimpleList;

public abstract class BookableResource {
    private String name;
    private String id;
    private SimpleList<Reservation> reservations;
    private OperatingHours hours;
    
    public String getId() {
        return id;
    }

    public abstract ResourceType getType();
    
    public abstract void validate(User user, TimeRange range);
    
    public boolean isAvailable(TimeRange range){
        return isOpenDuring(range) && !hasConflict(range);
    }

   private boolean isOpenDuring(TimeRange range){
        return hours.allows(range);
    }

    private boolean hasConflict(TimeRange range){
        for (Reservation reservation : reservations) {
            if (reservation.isActive() && reservation.getRange().overlaps(range)) {
                return true;
            }
        }
        return false;
    }
}
