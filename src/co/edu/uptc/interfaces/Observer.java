package co.edu.uptc.interfaces;

import co.edu.uptc.model.BookingEvent;

import java.io.IOException;

public interface Observer {
    public void update(BookingEvent event) throws IOException;
}
