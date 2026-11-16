package co.edu.uptc.model;

import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.exceptions.ReservationException;

import java.time.Duration;
import java.time.LocalDate;

public class SportsCourt extends BookableResource{
    private static final long MAX_DAILY_HOURS = 2;
    private int maxNumberOfPlayers;

    public SportsCourt(String name, OperatingHours hours) {
        super(name, "02", hours);
    }

    @Override
    public void validate(User user, TimeRange range) {
        LocalDate day = range.getStartTime().toLocalDate();
        if (!day.equals(range.getEndTime().toLocalDate())) {
            throw new ReservationException("La reserva debe empezar y terminar el mismo día.");
        }
        Duration used = Duration.ZERO;
        for (Reservation r : getReservations()) {
            if (r.getUserName().equals(user.getName())
                    && r.getRange().getStartTime().toLocalDate().equals(day)) {
                used = used.plus(r.getRange().getDuration());
            }
        }
        if (used.plus(range.getDuration()).compareTo(Duration.ofHours(MAX_DAILY_HOURS)) > 0) {
            throw new ReservationException("El límite es de " + MAX_DAILY_HOURS
                    + " horas diarias por cancha; hoy ya tienes " + used.toMinutes() + " min reservados.");
        }
    }
    
    @Override 
    public ResourceType getType(){
        return ResourceType.COURT;
    }
}
