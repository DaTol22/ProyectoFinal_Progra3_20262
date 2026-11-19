package co.edu.uptc.view;

import java.awt.BorderLayout;

import co.edu.uptc.controller.ClientController;

class MyReservationsPanel extends SectionPanel {
    private final ClientController controller;
    private final ReservationListPanel list;

    MyReservationsPanel(ClientController controller) {
        this.controller = controller;
        list = new ReservationListPanel(controller, this::refresh);
        setLayout(new BorderLayout(0, 10));
        add(ViewUtils.createTitle("Reservas"), BorderLayout.NORTH);
        add(list, BorderLayout.CENTER);
    }

    @Override
    void refresh() {
        try {
            list.showReservations(controller.getMyReservations(), false);
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
    }
}
