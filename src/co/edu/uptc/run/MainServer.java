package co.edu.uptc.run;

import java.io.IOException;

import co.edu.uptc.controller.ServerController;

public class MainServer {
    public static void main(String[] args) {
        try {
            new ServerController().init();
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor: " + e.getMessage());
        }
    }
}