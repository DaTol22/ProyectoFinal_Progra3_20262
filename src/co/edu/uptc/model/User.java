package co.edu.uptc.model;

import co.edu.uptc.enums.Role;
import co.edu.uptc.structures.SimpleList;

public class User {
    private SimpleList<Reservation> reservations;
    private String name;
    private String password;
    private Role role;
    private boolean blocked;

    public String getName() {
        return name;
    }

    public String getPassword(){
        return password;
    }

    public Role getRole(){
        return role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void addReservation(Reservation r){
        reservations.add(r);
    }

    public Reservation removeReservation(String id){
        for(Reservation reservation : reservations){
            if(reservation.getId().equals(id)){
                reservations.remove(reservation);
                return  reservation;
            }
        }return null;
    }

    public SimpleList<Reservation> getReservations() {
        return reservations;
    }
}
