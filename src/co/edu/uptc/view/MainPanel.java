package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import co.edu.uptc.clientController.ClientController;

class MainPanel extends JPanel {
    private final JPanel menu = new JPanel();
    private final JPanel content = new JPanel(new CardLayout());
    private Runnable firstSection;

    MainPanel(ClientController controller, Runnable onLogout) {
        setLayout(new BorderLayout());
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        menu.setPreferredSize(new Dimension(170, 0));

        if (controller.isAdmin()) {
            addSection("Usuarios", new UsersPanel(controller));
            addSection("Recursos", new ResourcesPanel(controller));
            addSection("Todas las reservas", new AllReservationsPanel(controller));
        } else {
            addSection("Perfil", new ProfilePanel(controller));
            addSection("Reservar", new ReservePanel(controller));
            addSection("Ver reservas", new MyReservationsPanel(controller));
        }

        menu.add(Box.createVerticalGlue());
        JButton logout = createMenuButton("Cerrar sesión");
        logout.addActionListener(e -> onLogout.run());
        menu.add(logout);

        add(menu, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        firstSection.run();
    }

    private void addSection(String title, SectionPanel panel) {
        content.add(panel, title);
        Runnable show = () -> {
            panel.refresh();
            ((CardLayout) content.getLayout()).show(content, title);
        };
        JButton button = createMenuButton(title);
        button.addActionListener(e -> show.run());
        menu.add(button);
        menu.add(Box.createVerticalStrut(6));
        if (firstSection == null) {
            firstSection = show;
        }
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height + 6));
        return button;
    }
}