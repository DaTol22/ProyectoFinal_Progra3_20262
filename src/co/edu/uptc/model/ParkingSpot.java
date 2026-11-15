package co.edu.uptc.model;

import co.edu.uptc.enums.ResourceType;

public class ParkingSpot extends BookableResource{
    private static final int MINUTES_PER_HOUR = 60;
    private int ratePerHour;

    public ParkingSpot(String name, String id, OperatingHours hours) {
        super(name, id, hours);
    }

    @Override
    public void validate(User user, TimeRange range) {
        

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
