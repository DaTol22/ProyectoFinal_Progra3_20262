package co.edu.uptc.model;

import co.edu.uptc.structures.SimpleList;

public class User {
    private String name;
    private String password;
    private Role role;
    private boolean blocked;
    private SimpleList<Reservation> reservations;

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
        }return null; //TODO hay que mirar si si tiene que ser la idea de este metodo
    }

    //TODO validar la password
}
