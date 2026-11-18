package co.edu.uptc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import co.edu.uptc.enums.Role;
import co.edu.uptc.exceptions.ReservationException;
import co.edu.uptc.persistence.FileManager;
import co.edu.uptc.structures.SimpleList;

public class BookingManager {
    private ResourcesCollection resources;
    private UsersCollection users;
    private int nextReservationNumber = 1;

    public BookingManager(){
        resources = new ResourcesCollection(FileManager.load());
    }
    
    public synchronized Reservation makeReservation(String userName, String resourceId, TimeRange range) {
        User user = users.findByName(userName);
        if(user == null){
            throw new ReservationException("usuario no encontrado");
        }
        if (user.isBlocked()) {
            throw new ReservationException("Tu cuenta está bloqueada; no puedes hacer reservas.");
        }
        BookableResource resource = resources.findById(resourceId);
        if (resource == null) {
            throw new ReservationException("El recurso no existe.");
        }
        if(range.getStartTime().isBefore(LocalDateTime.now())){
            throw new ReservationException("no puedes reservar en el pasado");
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

    public synchronized boolean cancelReservation(String userName, String reservationId) {
        if (userName == null || reservationId == null) {
            return false;
        }
        int separator = reservationId.lastIndexOf('#');
        if (separator <= 0) {
            return false;
        }
        BookableResource resource = resources.findById(reservationId.substring(0, separator));
        if (resource == null) {
            return false;
        }
        User requester = users.findByName(userName);
        if (requester == null) {
            return false;
        }
        Reservation reservation = findReservation(resource, reservationId);
        if (reservation == null) {
            return false;
        }
        boolean isOwner = userName.equals(reservation.getUserName());
        if (requester.getRole() != Role.ADMIN && !isOwner) {
            return false;
        }
        resource.removeReservation(reservationId);
        User owner = isOwner ? requester : users.findByName(reservation.getUserName());
        if (owner != null) {
            owner.removeReservation(reservationId);
        }
        return true;
    }

    public synchronized SimpleList<Reservation> getActiveReservations(String userName) {
        User user = users.findByName(userName);
        if (user == null) {
            throw new ReservationException("usuario no encontrado");
        }
        SimpleList<Reservation> active = new SimpleList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Reservation reservation : user.getReservations()) {
            if (reservation.getRange().getEndTime().isAfter(now)) {
                active.add(reservation);
            }
        }
        return active;
    }

    private Reservation findReservation(BookableResource resource, String reservationId) {
        SimpleList<Reservation> list = resource.getReservations();
        for (int i = 0; i < list.size(); i++) {
            Reservation r = list.get(i);
            if (r.getId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    public synchronized SimpleList<Reservation> getAllReservations(String requesterName) {
        User requester = users.findByName(requesterName);
        if (requester == null) {
            throw new ReservationException("usuario no encontrado");
        }
        if (requester.getRole() != Role.ADMIN) {
            throw new ReservationException("Solo un administrador puede ver todas las reservas.");
        }
        SimpleList<Reservation> all = new SimpleList<>();
        for (BookableResource resource : resources.findAll()) {
            for (Reservation reservation : resource.getReservations()) {
                all.add(reservation);
            }
        }
        return all;
    }

    public synchronized boolean isResourceAvailable(String resourceId, TimeRange range) {
        BookableResource resource = resources.findById(resourceId);
        if (resource == null) {
            throw new ReservationException("El recurso no existe.");
        }
        return resource.isAvailable(range);
    }

    public synchronized SimpleList<Reservation> getReservationsByDay(String resourceId, LocalDate date) {
        BookableResource resource = resources.findById(resourceId);
        if (resource == null) {
            throw new ReservationException("El recurso no existe.");
        }
        TimeRange day = new TimeRange(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        SimpleList<Reservation> occupied = new SimpleList<>();
        for (Reservation reservation : resource.getReservations()) {
            if (reservation.getRange().overlaps(day)) {
                occupied.add(reservation);
            }
        }
        return occupied;
    }
}
    