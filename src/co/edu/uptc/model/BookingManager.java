package co.edu.uptc.model;

import co.edu.uptc.exceptions.ReservationException;
import co.edu.uptc.persistence.FileManager;

public class BookingManager {
    private ResourcesCollection resources;
    private UsersCollection users;
    private int nextReservationNumber = 1;

    public BookingManager(){
        resources = new ResourcesCollection(FileManager.load());
        users = new UsersCollection();
    }
    
    public synchronized Reservation makeReservation(String userName, String resourceId, TimeRange range) {
        User user = users.findByName(userName);
        if (user.isBlocked()) {
            throw new ReservationException("Tu cuenta está bloqueada; no puedes hacer reservas.");
        }
        BookableResource resource = resources.findById(resourceId);
        if (resource == null) {
            throw new ReservationException("El recurso no existe.");
        }
        resource.validate(user, range);
        if (!resource.isAvailable(range)) {
            throw new ReservationException("El recurso no está disponible en esa franja (fuera de horario o ya reservado).");
        }
        Reservation reservation = new Reservation(resourceId + "#" + nextReservationNumber++, userName, resourceId, range);
        resource.addReservation(reservation);
        user.addReservation(reservation);
        return reservation;
    }

    public synchronized boolean cancelReservation(String reservationId) {
        int separator = reservationId.lastIndexOf('#');
        if (separator < 0) {
            return false;
        }
        BookableResource resource = resources.findById(reservationId.substring(0, separator));
        if (resource == null) {
            return false;
        }
        Reservation reservation = resource.removeReservation(reservationId);
        if (reservation == null) {
            return false;
        }
        users.findByName(reservation.getUserName()).removeReservation(reservationId);
        return true;
    }
}
