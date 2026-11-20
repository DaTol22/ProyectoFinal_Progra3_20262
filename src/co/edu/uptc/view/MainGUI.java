package co.edu.uptc.view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import co.edu.uptc.clientController.ClientController;

public class MainGUI extends JFrame {
    private static final String LOGIN = "login";
    private static final String MAIN = "main";

    private final ClientController controller;
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final LoginPanel loginPanel;
    private MainPanel mainPanel;

    public MainGUI(ClientController controller) {
        super("Sistema de Reservas de Recursos Compartidos");
        this.controller = controller;
        loginPanel = new LoginPanel(controller, this::openMain);
        root.add(loginPanel, LOGIN);
        setContentPane(root);
        setSize(960, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void openMain() {
        if (mainPanel != null) {
            root.remove(mainPanel);
        }
        mainPanel = new MainPanel(controller, this::logout);
        root.add(mainPanel, MAIN);
        cards.show(root, MAIN);
    }

    private void logout() {
        controller.logout();
        loginPanel.clear();
        cards.show(root, LOGIN);
    }

    //a
}