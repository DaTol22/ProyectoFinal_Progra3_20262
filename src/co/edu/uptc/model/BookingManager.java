package co.edu.uptc.model;

public class BookingManager {
    private ResourcesCollection resources;
    private UsersCollection users;
    
    public Reservation makeReservation(String userName, String resourceId, TimeRange range){
        //no valida nada dxxxxxx
        Reservation reservation = new Reservation("l4", userName, resourceId, range); //TODO no se como se pone el id de la reserva dxpuse ese l4 provisional
        users.findByName(userName).addReservation(reservation); 
        return reservation;
    }

    public boolean cancelReservation(String reservationId){
        return false;
    }
}
