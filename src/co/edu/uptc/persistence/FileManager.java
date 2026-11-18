package co.edu.uptc.persistence;

import co.edu.uptc.enums.Role;
import co.edu.uptc.model.*;
import co.edu.uptc.structures.SimpleList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FileManager {
    private static final Path USERS_PATH = Path.of("src/co/edu/uptc/data/users.csv");
    private static final Path RESERVATIONS_PATH = Path.of("src/co/edu/uptc/data/reservations.csv");
    private static final Path RESOURCES_PATH = Path.of("src/co/edu/uptc/data/resources.csv");


    public static SimpleList<BookableResource> load(){
        SimpleList<BookableResource> out = new SimpleList<>();
        String line;
        try(BufferedReader reader = Files.newBufferedReader(Path.of("src/co/edu/uptc/data/resources.csv"))){
            reader.readLine();
            while((line = reader.readLine()) != null){
                out.add(format(line));
            }
        }
        catch (IOException e){
            //TODO System.err.println(e.getMessage());
        }
        return out;
    }

    public static SimpleList<User> loadUsers() {
        SimpleList<User> out = new SimpleList<>();
        String line;
        try (BufferedReader reader = Files.newBufferedReader(USERS_PATH)) {
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.replace("\"", "").split(",", -1);
                out.add(new User(parts[0], parts[1], Role.valueOf(parts[2]), Boolean.parseBoolean(parts[3])));
            }
        } catch (IOException e) {
            System.err.println("Could not read users: " + e.getMessage());
        }
        return out;
    }

    public static SimpleList<Reservation> loadReservations() {
        SimpleList<Reservation> out = new SimpleList<>();
        String line;
        try (BufferedReader reader = Files.newBufferedReader(RESERVATIONS_PATH)) {
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.replace("\"", "").split(",", -1);
                TimeRange range = new TimeRange(LocalDateTime.parse(parts[3]), LocalDateTime.parse(parts[4]));
                int cost = parts.length > 5 && !parts[5].isEmpty() ? Integer.parseInt(parts[5]) : 0;
                out.add(new Reservation(parts[0], parts[1], parts[2], range, cost));
            }
        } catch (IOException e) {
            System.err.println("Could not read reservations: " + e.getMessage());
        }
        return out;
    }

    private static BookableResource format(String line){
        String[] lineArray = line.replace("\"", "").split(",", -1);
        String name = lineArray[0];
        String id = lineArray[1];
        LocalTime opens = LocalTime.parse(lineArray[2]);
        LocalTime closes = LocalTime.parse(lineArray[3]);
        if(lineArray[4].isEmpty() && lineArray[5].isEmpty()){
            return new SportsCourt(name, id, new OperatingHours(opens, closes));
        }else if(lineArray[5].isEmpty()){
            return new ParkingSpot(name, id, new OperatingHours(opens, closes), Integer.parseInt(lineArray[4]));
        }else{
            return new StudyRoom(name, id, new OperatingHours(opens, closes), Duration.ofMinutes(Long.parseLong(lineArray[5])));
        }
    }

    public static void writeResources(SimpleList<BookableResource> resources) {
        SimpleList<String> lines = new SimpleList<>();
        lines.add("\"NAME\",\"ID\",\"OPENS\",\"CLOSES\",\"RATE_PER_HOUR\",\"SLOT_DURATION\"");
        for (BookableResource resource : resources) {
            String rate = "";
            String slot = "";
            switch (resource.getType()) {
                case PARKING -> rate = String.valueOf(((ParkingSpot) resource).getRatePerHour());
                case STUDY_ROOM -> slot = String.valueOf(((StudyRoom) resource).getSlotDuration().toMinutes());
                case COURT -> { }
            }
            lines.add(field(resource.getName()) + "," + field(resource.getId()) + ","
                    + field(resource.getHours().getOpens().toString()) + ","
                    + field(resource.getHours().getCloses().toString()) + ","
                    + field(rate) + "," + field(slot));
        }
        write(RESOURCES_PATH, lines);
    }

    public static void writeUsers(SimpleList<User> users) {
        SimpleList<String> lines = new SimpleList<>();
        lines.add("\"NAME\",\"PASSWORD\",\"ROLE\",\"BLOCKED\"");
        for (User user : users) {
            lines.add(field(user.getName()) + "," + field(user.getPassword()) + ","
                    + field(user.getRole().name()) + "," + field(String.valueOf(user.isBlocked())));
        }
        write(USERS_PATH, lines);
    }

    public static void writeReservations(SimpleList<BookableResource> resources) {
        SimpleList<String> lines = new SimpleList<>();
        lines.add("\"ID\",\"USER_NAME\",\"RESOURCE_ID\",\"START\",\"END\",\"COST\"");
        for (BookableResource resource : resources) {
            for (Reservation reservation : resource.getReservations()) {
                lines.add(field(reservation.getId()) + "," + field(reservation.getUserName()) + ","
                        + field(reservation.getResourceId()) + ","
                        + field(reservation.getRange().getStartTime().toString()) + ","
                        + field(reservation.getRange().getEndTime().toString()) + ","
                        + field(String.valueOf(reservation.getCost())));
            }
        }
        write(RESERVATIONS_PATH, lines);
    }

    private static String field(String value) {
        return value.isEmpty() ? "" : "\"" + value + "\"";
    }

    private static void write(Path path, SimpleList<String> lines) {
        Path temp = Path.of(path.toString() + ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(temp)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Could not write " + path + ": " + e.getMessage());
            return;
        }
        try {
            Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Could not replace " + path + ": " + e.getMessage());
        }
    }
}
