package co.edu.uptc.model;

import co.edu.uptc.structures.*;

public class UsersCollection {
    private SimpleList<User> users;

    public boolean register(User user){
        try{
            findByName(user.getName());
            return false;
        }catch(IllegalArgumentException e){
            return users.add(user);
        }
    }


    public User login(String name, String password) throws IllegalArgumentException{
        User user = findByName(name);
        if(!password.equals(user.getPassword())) throw new IllegalArgumentException("Contraseña incorrecta.");
        return user;
    }
    
    public User findByName(String name) throws IllegalArgumentException{
        for(User user : users){
            if(user.getName().equals(name)){
                return user;
            }
        }
        throw new IllegalArgumentException("Usuario no encontrado.");//TODO igualmente aqui, hay que mirar si esto es lo que se quiere
    }
    
}
