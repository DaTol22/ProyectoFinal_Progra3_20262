package co.edu.uptc.net;

import java.time.LocalDateTime;

public class ReservationData {
    private final String id;
    private final String userName;
    private final String resourceId;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final int cost;

    public ReservationData(String id, String userName, String resourceId,
       LocalDateTime start, LocalDateTime end, int cost) {
        this.id = id; this.userName = userName; this.resourceId = resourceId;
        this.start = start; this.end = end; this.cost = cost;
    }

    public String getId() { return id; }
    public String getUserName() { return userName; }
    public String getResourceId() { return resourceId; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public int getCost() { return cost; }
}