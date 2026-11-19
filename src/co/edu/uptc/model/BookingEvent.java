package co.edu.uptc.model;

import co.edu.uptc.enums.BookingEventType;

public class BookingEvent {
    private final BookingEventType type;
    private final String resourceId;

    public BookingEvent(BookingEventType type, String resourceId) {
        this.type = type;
        this.resourceId = resourceId;
    }

    public BookingEventType getType() { return type; }
    public String getResourceId() { return resourceId; }
}