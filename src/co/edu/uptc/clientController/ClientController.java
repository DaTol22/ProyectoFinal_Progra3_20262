package co.edu.uptc.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.enums.Role;
import co.edu.uptc.model.BookableResource;
import co.edu.uptc.model.BookingManager;
import co.edu.uptc.model.OperatingHours;
import co.edu.uptc.model.ParkingSpot;
import co.edu.uptc.model.Reservation;
import co.edu.uptc.model.SportsCourt;
import co.edu.uptc.model.StudyRoom;
import co.edu.uptc.model.TimeRange;
import co.edu.uptc.model.User;
import co.edu.uptc.structures.SimpleList;

public class ClientController {
    private final BookingManager manager;
    private String currentUser;
    private Role currentRole;

    public ClientController(BookingManager manager) {
        this.manager = manager;
    }

    // ---------- sesión ----------

    public void login(String name, String password) {
        String trimmed = name == null ? "" : name.trim();
        currentRole = manager.login(trimmed, password);
        currentUser = trimmed;
    }

    public void logout() {
        currentUser = null;
        currentRole = null;
    }

    public void register(String name, String password) {
        String trimmed = name == null ? "" : name.trim();
        manager.register(new User(trimmed, password, false));
    }

    public boolean isAdmin() {
        return currentRole == Role.ADMIN;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    // ---------- usuario normal ----------

    /** Si type es null devuelve los recursos de todos los tipos. */
    public SimpleList<BookableResource> getResources(ResourceType type) {
        if (type != null) {
            return manager.findByType(type);
        }
        SimpleList<BookableResource> all = new SimpleList<>();
        for (ResourceType t : ResourceType.values()) {
            for (BookableResource resource : manager.findByType(t)) {
                all.add(resource);
            }
        }
        return all;
    }

    public Reservation reserve(String resourceId, LocalDateTime start, LocalDateTime end) {
        return manager.makeReservation(currentUser, resourceId, new TimeRange(start, end));
    }

    public SimpleList<Reservation> getMyReservations() {
        return manager.getActiveReservations(currentUser);
    }

    public int countMyReservations() {
        return getMyReservations().size();
    }

    /** El administrador también puede cancelar cualquier reserva. */
    public boolean cancelReservation(String reservationId) {
        return manager.cancelReservation(currentUser, reservationId);
    }

    // ---------- administrador: usuarios y reservas ----------

    public SimpleList<String> getUsersInfo() {
        return manager.getUsers(currentUser);
    }

    public void blockUser(String name) {
        manager.block(name);
    }

    public void unblockUser(String name) {
        manager.unblock(name);
    }

    public SimpleList<Reservation> getAllReservations() {
        return manager.getAllReservations(currentUser);
    }

    public SimpleList<Reservation> getReservationsOf(String userName) {
        SimpleList<Reservation> out = new SimpleList<>();
        for (Reservation reservation : manager.getAllReservations(currentUser)) {
            if (reservation.getUserName().equals(userName)) {
                out.add(reservation);
            }
        }
        return out;
    }

    // ---------- administrador: recursos ----------

    /**
     * extra: tarifa por hora (parqueadero) o duración de la franja en minutos (sala);
     * se ignora para las canchas.
     */
    public boolean addResource(ResourceType type, String name, LocalTime opens, LocalTime closes, int extra) {
        return manager.addResource(buildResource(type, null, name, opens, closes, extra));
    }

    public void editResource(String id, ResourceType type, String name, LocalTime opens, LocalTime closes, int extra) {
        manager.editResource(buildResource(type, id, name, opens, closes, extra));
    }

    public void removeResource(String id) {
        manager.removeResource(id);
    }

    private BookableResource buildResource(ResourceType type, String id, String name,
                                           LocalTime opens, LocalTime closes, int extra) {
        OperatingHours hours = new OperatingHours(opens, closes);
        if (type == ResourceType.PARKING) {
            return id == null ? new ParkingSpot(name, hours, extra)
                    : new ParkingSpot(name, id, hours, extra);
        }
        if (type == ResourceType.STUDY_ROOM) {
            Duration slot = Duration.ofMinutes(extra);
            return id == null ? new StudyRoom(name, hours, slot)
                    : new StudyRoom(name, id, hours, slot);
        }
        return id == null ? new SportsCourt(name, hours)
                : new SportsCourt(name, id, hours);
    }
}