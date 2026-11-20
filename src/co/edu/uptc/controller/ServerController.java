package co.edu.uptc.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import co.edu.uptc.model.BookingManager;

public class ServerController {
    private static final int PORT = 1670;
    private ServerSocket server;
    private BookingManager manager;

    public ServerController() throws IOException {
        server = new ServerSocket(PORT);
        manager = new BookingManager();
    }

    public void init() throws IOException {
        while (true) {
            Socket socket = server.accept();
            BookingThread thread = new BookingThread(socket, manager);
            thread.start();
        }
    }
}