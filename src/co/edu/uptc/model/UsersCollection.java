package co.edu.uptc.model;

import co.edu.uptc.structures.*;

public class UsersCollection {
    private SimpleList<User> users;

    public boolean register(User user){
        return users.add(user);
    }

    public User login(String name, String password){
        for(User user : users){
            if(user.getName().equals(name) && user.getPassword().equals(password)){
                return user;
            }
        }return null;
    }
    
    public User findByName(String name){
        for(User user : users){
            if(user.getName().equals(name)){
                return user;
            }
        }return null; //TODO igualmente aqui, hay que mirar si esto es lo que se quiere
    }
    
}
