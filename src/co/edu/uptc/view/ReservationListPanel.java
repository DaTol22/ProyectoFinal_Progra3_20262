package co.edu.uptc.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import co.edu.uptc.controller.ClientController;
import co.edu.uptc.model.Reservation;
import co.edu.uptc.structures.SimpleList;

class ReservationListPanel extends JPanel {
    private final ClientController controller;
    private final Runnable onChange;
    private final JPanel rows = new JPanel();

    /** onChange se ejecuta después de cancelar una reserva, para que el panel dueño recargue. */
    ReservationListPanel(ClientController controller, Runnable onChange) {
        this.controller = controller;
        this.onChange = onChange;
        setLayout(new BorderLayout());
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        JPanel holder = new JPanel(new BorderLayout());
        holder.add(rows, BorderLayout.NORTH);
        add(new JScrollPane(holder), BorderLayout.CENTER);
    }

    void showReservations(SimpleList<Reservation> reservations, boolean showOwner) {
        rows.removeAll();
        boolean empty = true;
        for (Reservation reservation : reservations) {
            rows.add(buildRow(reservation, showOwner));
            empty = false;
        }
        if (empty) {
            rows.add(new JLabel("No hay reservas."));
        }
        rows.revalidate();
        rows.repaint();
    }

    void clear() {
        rows.removeAll();
        rows.revalidate();
        rows.repaint();
    }

    private JPanel buildRow(Reservation reservation, boolean showOwner) {
        String text = "Reserva " + reservation.getId() + " · " + ViewUtils.formatRange(reservation.getRange());
        if (reservation.getCost() > 0) {
            text += " · $" + reservation.getCost();
        }
        if (showOwner) {
            text += " · Usuario: " + reservation.getUserName();
        }
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> cancel(reservation.getId()));

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 4, 8, 4)));
        row.add(new JLabel(text), BorderLayout.CENTER);
        row.add(cancel, BorderLayout.EAST);
        return row;
    }

    private void cancel(String reservationId) {
        int answer = JOptionPane.showConfirmDialog(this, "¿Cancelar la reserva " + reservationId + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (!controller.cancelReservation(reservationId)) {
                ViewUtils.showError(this, "No se pudo cancelar la reserva.");
            }
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
        onChange.run();
    }
}
