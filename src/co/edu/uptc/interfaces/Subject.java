package co.edu.uptc.interfaces;

import co.edu.uptc.enums.BookingEventType;

public interface Subject {
    public void attach(Observer o);
    public void detach(Observer o);
    public void notifyObservers(BookingEventType type, String resourceId);
}
