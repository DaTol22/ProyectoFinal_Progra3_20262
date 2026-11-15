package co.edu.uptc.model;

public class Reservation {
    private String id;
    private String userName;
    private String resourceId;
    private TimeRange range;
    private ReservationStatus status;

    public Reservation(String id, String userName, String resourceId, TimeRange range) {
        this.id = id;
        this.userName = userName;
        this.resourceId = resourceId;
        this.range = range;
        status = ReservationStatus.ACTIVE;
    }

    public String getId(){
        return id;
    }

    public boolean isInRange(){
        return false;
    }

    public boolean isActive(){
        return status == ReservationStatus.ACTIVE;
    }

    public String getUserName() {
        return userName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public TimeRange getRange() {
        return range;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
