package co.edu.uptc.model;

import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.structures.SimpleList;

public abstract class BookableResource {
    private SimpleList<Reservation> reservations;
    private String name;
    private String id;
    private OperatingHours hours;

    public BookableResource(String name, String id, OperatingHours hours){
        reservations = new SimpleList<>();
        this.name = name;
        this.id = id;
        this.hours = hours;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
            if (reservation.getRange().overlaps(range)) {
                return true;
            }
        }
        return false;
    }

    public SimpleList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(SimpleList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation r){
        reservations.add(r);
    }

    public Reservation removeReservation(String id){
        for(Reservation reservation : reservations){
            if(reservation.getId().equals(id)){
                reservations.remove(reservation);
                return  reservation;
            }
        }return null;
    }
}
