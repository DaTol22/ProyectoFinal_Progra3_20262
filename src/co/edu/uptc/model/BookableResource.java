package co.edu.uptc.model;

import java.util.List;

public abstract class BookableResource {
    private String name;
    private String id;
    private List<Reservation> reservations;
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
