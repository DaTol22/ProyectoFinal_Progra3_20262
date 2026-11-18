package co.edu.uptc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import co.edu.uptc.enums.ResourceType;
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
        users = new UsersCollection();
        for (User user : FileManager.loadUsers()) {
            users.register(user);
        }
        for (Reservation reservation : FileManager.loadReservations()) {
            BookableResource resource = resources.findById(reservation.getResourceId());
            User user = users.findByName(reservation.getUserName());
            if (resource != null && user != null) {
                resource.addReservation(reservation);
                user.addReservation(reservation);
                int number = Integer.parseInt(reservation.getId().substring(reservation.getId().lastIndexOf('#') + 1));
                nextReservationNumber = Math.max(nextReservationNumber, number + 1);
            }
        }
    }

    private void save() {
        FileManager.writeResources(resources.findAll());
        FileManager.writeUsers(users.getUsers());
        FileManager.writeReservations(resources.findAll());
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
        int cost = resource.calculateCost(range);
        Reservation reservation = new Reservation(resourceId + "#" + nextReservationNumber++, userName, resourceId, range, cost);
        resource.addReservation(reservation);
        user.addReservation(reservation);
        save();
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
        save();
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

    public synchronized void register(User user){
        users.register(user);
        save();
    }

    public synchronized void login(String username, String password){
        users.login(username, password);
    }

    public synchronized void block(String name){
        users.blockUser(name);
        save();
    }

    public synchronized void unblock(String name){
        users.unblockUser(name);
        save();
    }

    public synchronized SimpleList<String> getUsers(String userName){
        User requester = users.findByName(userName);
        if (requester == null) {
            throw new ReservationException("usuario no encontrado");
        }
        if (requester.getRole() != Role.ADMIN) {
            throw new ReservationException("No eres administrador.");
        }
        return users.getUsersInfo();
    }

    public synchronized boolean addResource(BookableResource resource){
        boolean success = resources.addResource(resource);
        if(success) {
            save();
            return true;
        }
        return false;
    }

    public synchronized BookableResource removeResource(String id){
        BookableResource removed = resources.removeResource(id);
        save();
        return removed;
    }

    public synchronized void editResource(BookableResource resource){
        resources.editResource(resource);
        save();
    }

    public synchronized SimpleList<BookableResource> findByType(ResourceType type){
        return resources.findByType(type);
    }

    public synchronized SimpleList<BookableResource> findAvailable(TimeRange range){
        return resources.findAvailable(range);
    }


}
    