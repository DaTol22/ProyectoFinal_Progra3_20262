package co.edu.uptc.model;

import co.edu.uptc.structures.SimpleList;

public class User {
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

    private SimpleList<Reservation> reservations;

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
}
