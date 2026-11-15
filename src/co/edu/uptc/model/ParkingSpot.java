package co.edu.uptc.model;

public class ParkingSpot extends BookableResource{
    private static final int MINUTES_PER_HOUR = 60;
    private int ratePerHour;

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
