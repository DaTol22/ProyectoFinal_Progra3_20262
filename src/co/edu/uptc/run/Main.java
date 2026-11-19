package co.edu.uptc.run;

import javax.swing.SwingUtilities;

import co.edu.uptc.controller.ClientController;
import co.edu.uptc.model.BookingManager;
import co.edu.uptc.view.MainGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientController controller = new ClientController(new BookingManager());
            new MainGUI(controller).setVisible(true);
        });
    }
}