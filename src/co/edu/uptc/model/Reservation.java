package co.edu.uptc.model;

public class Reservation {
    private String id;
    private String userName;
    private String resourceId;
    private int cost;
    private TimeRange range;

    public Reservation(String id, String userName, String resourceId, TimeRange range) {
        this(id, userName, resourceId, range, 0);
    }

    public Reservation(String id, String userName, String resourceId, TimeRange range, int cost) {
        this.id = id;
        this.userName = userName;
        this.resourceId = resourceId;
        this.range = range;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public String getId(){
        return id;
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
}
