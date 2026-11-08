package co.edu.uptc.model;

public class ParkingSpot extends BookableResource{
    private double ratePerHour;

    @Override
    public void validate(User user, TimeRange range) {

    }

    private double calculateCost(TimeRange range){
        return 0;
    }
}
