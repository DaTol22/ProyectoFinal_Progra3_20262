package co.edu.uptc.model;

import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.exceptions.ReservationException;

public class ParkingSpot extends BookableResource{
    private static final int MINUTES_PER_HOUR = 60;
    private static final long MAX_HOURS = 24;
    private int ratePerHour;

    public ParkingSpot(String name, String id, OperatingHours hours) {
        super(name, id, hours);
    }

    @Override
    public void validate(User user, TimeRange range) {
        long minutes = range.getDuration().toMinutes();
        if (minutes <= 0) {
            throw new ReservationException("La duración debe ser mayor a cero.");
        }
        if (minutes > MAX_HOURS * 60) {
            throw new ReservationException("La estadía máxima es de " + MAX_HOURS + " horas.");
        }
    }

    @Override 
    public ResourceType getType(){
        return ResourceType.PARKING;
    }

    public int calculateCost(TimeRange range){
        int totalMinutes = (int) range.getDuration().toMinutes();
        return ((totalMinutes + (MINUTES_PER_HOUR - 1)) / MINUTES_PER_HOUR) * ratePerHour;
    }
}
