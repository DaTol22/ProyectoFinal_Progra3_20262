package co.edu.uptc.model;

public class Reservation {
    private String id;
    private String userName;
    private String resourceId;
    private TimeRange range;

    public Reservation(String id, String userName, String resourceId, TimeRange range) {
        this.id = id;
        this.userName = userName;
        this.resourceId = resourceId;
        this.range = range;
    }

    public String getId(){
        return id;
    }

    public boolean isInRange(){
        return false;
    }
}
