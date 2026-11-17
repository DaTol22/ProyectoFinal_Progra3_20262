package co.edu.uptc.model;

import co.edu.uptc.structures.*;

public class UsersCollection {
    private SimpleList<User> users;

    public synchronized boolean register(User user){
        validateCredentials(user.getName(), user.getPassword());
        if(findByName(user.getName()) != null){
            throw new IllegalArgumentException("El usuario ya existe.");
        }
        return users.add(user);
    }

    public synchronized User login(String name, String password) throws IllegalArgumentException {
        User user = findByName(name);
        if (user == null || !password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
        }
        if (user.isBlocked()) {
            throw new IllegalArgumentException("El usuario está bloqueado.");
        }
        return user;
    }

    public synchronized User findByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    private void validateCredentials(String name, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }
    }

    public void blockUser(String name){
        User user = findByName(name);
        if(user == null){
            throw new IllegalArgumentException("usuario no encontrado");
        }else{
            user.setBlocked(true);
        }
    }

    public void unblockUser(String name){
        User user = findByName(name);
        if(user == null){
            throw new IllegalArgumentException("usuario no encontrado");
        }else{
            user.setBlocked(false);
        }
    }


}
