package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import co.edu.uptc.clientController.ClientController;

class UsersPanel extends SectionPanel {
    private final ClientController controller;
    private final JComboBox<UserItem> userCombo = new JComboBox<>();
    private final JLabel statusLabel = new JLabel(" ");
    private final ReservationListPanel reservationList;

    UsersPanel(ClientController controller) {
        this.controller = controller;
        reservationList = new ReservationListPanel(controller, this::afterCancel);
        setLayout(new BorderLayout(0, 10));

        JButton block = new JButton("Block");
        JButton unblock = new JButton("Unblock");
        JButton showReservations = new JButton("Ver reservas");
        block.addActionListener(e -> changeBlock(true));
        unblock.addActionListener(e -> changeBlock(false));
        showReservations.addActionListener(e -> showReservations());
        userCombo.addActionListener(e -> updateStatus());

        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        selector.add(new JLabel("Seleccionar:"));
        selector.add(userCombo);
        selector.add(block);
        selector.add(unblock);
        selector.add(showReservations);

        JPanel top = new JPanel(new BorderLayout());
        top.add(ViewUtils.createTitle("Users"), BorderLayout.NORTH);
        top.add(selector, BorderLayout.CENTER);
        top.add(statusLabel, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(reservationList, BorderLayout.CENTER);
    }

    @Override
    void refresh() {
        loadUsers(null);
        reservationList.clear();
    }

    private void loadUsers(String keepSelected) {
        try {
            userCombo.removeAllItems();
            for (String info : controller.getUsersInfo()) {
                String[] parts = info.split(";");
                UserItem item = new UserItem(parts[0], Integer.parseInt(parts[1]), Boolean.parseBoolean(parts[2]));
                userCombo.addItem(item);
                if (item.name.equals(keepSelected)) {
                    userCombo.setSelectedItem(item);
                }
            }
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
        updateStatus();
    }

    private void updateStatus() {
        UserItem item = (UserItem) userCombo.getSelectedItem();
        if (item == null) {
            statusLabel.setText(" ");
            return;
        }
        statusLabel.setText("Reservas: " + item.reservations + " · Estado: "
                + (item.blocked ? "bloqueado" : "activo"));
    }

    private void changeBlock(boolean block) {
        UserItem item = (UserItem) userCombo.getSelectedItem();
        if (item == null) {
            return;
        }
        try {
            if (block) {
                controller.blockUser(item.name);
            } else {
                controller.unblockUser(item.name);
            }
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
        loadUsers(item.name);
    }

    private void showReservations() {
        UserItem item = (UserItem) userCombo.getSelectedItem();
        if (item == null) {
            return;
        }
        try {
            reservationList.showReservations(controller.getReservationsOf(item.name), false);
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
    }

    private void afterCancel() {
        UserItem item = (UserItem) userCombo.getSelectedItem();
        loadUsers(item == null ? null : item.name);
        showReservations();
    }

    private static class UserItem {
        private final String name;
        private final int reservations;
        private final boolean blocked;

        private UserItem(String name, int reservations, boolean blocked) {
            this.name = name;
            this.reservations = reservations;
            this.blocked = blocked;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
