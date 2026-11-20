package co.edu.uptc.controller;

import co.edu.uptc.net.CommandType;
import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.enums.Role;
import co.edu.uptc.exceptions.ReservationException;
import co.edu.uptc.interfaces.Observer;
import co.edu.uptc.model.*;
import co.edu.uptc.structures.SimpleList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingThread extends Thread implements Observer {
    private Socket socket;
    private DataInputStream in;
    private BookingManager manager;
    private String input;
    private DataOutputStream out;
    private String currentUser;
    private Role currentRole;

    public BookingThread(Socket socket, BookingManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            initConnection();
            while (true) {
                String command = in.readUTF();
                int n = Integer.parseInt(in.readUTF());
                String[] p = new String[n];
                for (int i = 0; i < n; i++) {
                    p[i] = in.readUTF();
                }
                process(command, p);
            }
        } catch (IOException e) {
            System.err.println("Cliente desconectado.");
        } finally {
            manager.detach(this);
            try { socket.close(); } catch (IOException e) { }
        }
    }

    private void process(String command, String[] p) throws IOException {
        try {
            CommandType type = CommandType.valueOf(command);
            if (type != CommandType.LOGIN && type != CommandType.REGISTER) {
                requireLogin();
            }
            switch (type) {
                case LOGIN -> {
                    Role role = manager.login(p[0].trim(), p[1]);
                    currentUser = p[0].trim();
                    currentRole = role;
                    send("OK", role.name());
                }
                case REGISTER -> {
                    manager.register(new User(p[0].trim(), p[1], false));
                    send("OK");
                }
                case LOGOUT -> {
                    currentUser = null;
                    currentRole = null;
                    send("OK");
                }
                case GET_RESOURCES -> {
                    SimpleList<String> data = new SimpleList<>();
                    if (p[0].equals("ALL")) {
                        for (ResourceType t : ResourceType.values()) {
                            addResources(data, manager.findByType(t));
                        }
                    } else {
                        addResources(data, manager.findByType(ResourceType.valueOf(p[0])));
                    }
                    send("OK", data.toArray(new String[0]));
                }
                case RESERVE -> {
                    Reservation r = manager.makeReservation(currentUser, p[0],
                            new TimeRange(LocalDateTime.parse(p[1]), LocalDateTime.parse(p[2])));
                    send("OK", r.getId(), String.valueOf(r.getCost()));
                }
                case CANCEL -> {
                    if (!manager.cancelReservation(currentUser, p[0])) {
                        throw new ReservationException("No se pudo cancelar la reserva.");
                    }
                    send("OK");
                }
                case GET_MY_RESERVATIONS -> {
                    SimpleList<String> data = new SimpleList<>();
                    addReservations(data, manager.getActiveReservations(currentUser));
                    send("OK", data.toArray(new String[0]));
                }
                case GET_ALL_RESERVATIONS -> {
                    SimpleList<String> data = new SimpleList<>();
                    addReservations(data, manager.getAllReservations(currentUser));
                    send("OK", data.toArray(new String[0]));
                }
                case GET_USERS -> {
                    SimpleList<String> users = manager.getUsers(currentUser);
                    send("OK", users.toArray(new String[0]));
                }
                case BLOCK -> {
                    requireAdmin();
                    manager.block(p[0]);
                    send("OK");
                }
                case UNBLOCK -> {
                    requireAdmin();
                    manager.unblock(p[0]);
                    send("OK");
                }
                case ADD_RESOURCE -> {
                    requireAdmin();
                    manager.addResource(buildResource(ResourceType.valueOf(p[0]), null, p[1],
                            LocalTime.parse(p[2]), LocalTime.parse(p[3]), Integer.parseInt(p[4])));
                    send("OK");
                }
                case EDIT_RESOURCE -> {
                    requireAdmin();
                    manager.editResource(buildResource(ResourceType.valueOf(p[1]), p[0], p[2],
                            LocalTime.parse(p[3]), LocalTime.parse(p[4]), Integer.parseInt(p[5])));
                    send("OK");
                }
                case REMOVE_RESOURCE -> {
                    requireAdmin();
                    manager.removeResource(p[0]);
                    send("OK");
                }
            }
        } catch (RuntimeException e) {
            send("ERROR", e.getMessage() == null ? "Error del servidor." : e.getMessage());
        }
    }

    private void send(String type, String... parts) throws IOException {
        synchronized (out) {
            out.writeUTF(type);
            out.writeUTF(String.valueOf(parts.length));
            for (String part : parts) {
                out.writeUTF(part);
            }
        }
    }

    private void initConnection() throws IOException{
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        manager.attach(this);
    }

    @Override
    public void update(BookingEvent event) throws IOException {
        send("EVENT", event.getType().name(), event.getResourceId());
    }
    private void requireLogin() {
        if (currentUser == null) {
            throw new ReservationException("Debes iniciar sesión.");
        }
    }

    private void requireAdmin() {
        if (currentRole != Role.ADMIN) {
            throw new ReservationException("No eres administrador.");
        }
    }

    private void addResources(SimpleList<String> data, SimpleList<BookableResource> resources) {
        for (BookableResource resource : resources) {
            int extra = 0;
            switch (resource.getType()) {
                case PARKING -> extra = ((ParkingSpot) resource).getRatePerHour();
                case STUDY_ROOM -> extra = (int) ((StudyRoom) resource).getSlotDuration().toMinutes();
                case COURT -> { }
            }
            data.add(resource.getId());
            data.add(resource.getName());
            data.add(resource.getType().name());
            data.add(resource.getHours().getOpens().toString());
            data.add(resource.getHours().getCloses().toString());
            data.add(String.valueOf(extra));
        }
    }

    private void addReservations(SimpleList<String> data, SimpleList<Reservation> reservations) {
        for (Reservation r : reservations) {
            data.add(r.getId());
            data.add(r.getUserName());
            data.add(r.getResourceId());
            data.add(r.getRange().getStartTime().toString());
            data.add(r.getRange().getEndTime().toString());
            data.add(String.valueOf(r.getCost()));
        }
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
