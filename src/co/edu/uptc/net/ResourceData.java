package co.edu.uptc.net;

import java.time.LocalTime;
import co.edu.uptc.enums.ResourceType;

public class ResourceData {
    private final String id;
    private final String name;
    private final ResourceType type;
    private final LocalTime opens;
    private final LocalTime closes;
    private final int extra;

    public ResourceData(String id, String name, ResourceType type,
                        LocalTime opens, LocalTime closes, int extra) {
        this.id = id; this.name = name; this.type = type;
        this.opens = opens; this.closes = closes; this.extra = extra;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public ResourceType getType() { return type; }
    public LocalTime getOpens() { return opens; }
    public LocalTime getCloses() { return closes; }
    public int getExtra() { return extra; }
}