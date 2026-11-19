package co.edu.uptc.controller;

import co.edu.uptc.interfaces.Observer;
import co.edu.uptc.model.BookingEvent;
import co.edu.uptc.model.BookingManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BookingThread extends Thread implements Observer {
    private Socket socket;
    private DataInputStream in;
    private BookingManager manager;
    private String input;
    private DataOutputStream out;

    public BookingThread(Socket socket, BookingManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {

    }

    private void initConnection() throws IOException{
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        manager.attach(this);
    }

    @Override
    public void update(BookingEvent event) throws IOException {
        out.writeUTF("EVENT;" + event.getType() + ";" + event.getResourceId());
    }
}
