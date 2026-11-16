package co.edu.uptc.model;

import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.exceptions.ReservationException;

import java.time.Duration;

public class StudyRoom extends BookableResource{
    private static final int MAX_SLOTS = 2;
    private Duration slotDuration;

    public StudyRoom(String name, OperatingHours hours) {
        super(name, "03", hours);
    }

    @Override 
    public void validate(User user, TimeRange range){
        long slotMin = slotDuration.toMinutes();
        long durMin = range.getDuration().toMinutes();
        long startMin = range.getStartTime().getHour() * 60L + range.getStartTime().getMinute();

        if (startMin % slotMin != 0) {
            throw new ReservationException("Las salas se reservan por franjas de "
                    + slotMin + " minutos; la hora de inicio debe coincidir con una franja.");
        }
        if (durMin <= 0 || durMin % slotMin != 0) {
            throw new ReservationException("La duración debe ser un múltiplo de "
                    + slotMin + " minutos.");
        }
        if (durMin > slotMin * MAX_SLOTS) {
            throw new ReservationException("Máximo " + MAX_SLOTS + " franjas ("
                    + slotMin * MAX_SLOTS + " min) por reserva.");
        }
    }

    @Override 
    public ResourceType getType(){
        return ResourceType.STUDY_ROOM;
    }
    
}
