package co.edu.uptc.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import co.edu.uptc.clientController.ClientController;

class ProfilePanel extends SectionPanel {
    private final ClientController controller;
    private final JLabel nameLabel = new JLabel();
    private final JLabel reservationsLabel = new JLabel();

    ProfilePanel(ClientController controller) {
        this.controller = controller;
        setLayout(new java.awt.BorderLayout());
        add(ViewUtils.createTitle("Perfil"), java.awt.BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel avatar = new JLabel(new AvatarIcon());
        avatar.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        reservationsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(javax.swing.Box.createVerticalStrut(20));
        info.add(avatar);
        info.add(javax.swing.Box.createVerticalStrut(16));
        info.add(nameLabel);
        info.add(javax.swing.Box.createVerticalStrut(8));
        info.add(reservationsLabel);
        add(info, java.awt.BorderLayout.CENTER);
    }

    @Override
    void refresh() {
        nameLabel.setText("Nombre: " + controller.getCurrentUser());
        reservationsLabel.setText("Reservas activas: " + controller.countMyReservations());
    }

    private static class AvatarIcon implements Icon {
        private static final int SIZE = 110;

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new Ellipse2D.Float(x, y, SIZE, SIZE));
            g2.setColor(new Color(220, 220, 220));
            g2.fillRect(x, y, SIZE, SIZE);
            g2.setColor(new Color(150, 150, 150));
            g2.fillOval(x + 37, y + 20, 36, 36);
            g2.fillOval(x + 22, y + 64, 66, 66);
            g2.dispose();
        }
    }
}