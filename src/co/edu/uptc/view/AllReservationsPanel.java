package co.edu.uptc.view;

import java.awt.BorderLayout;

import co.edu.uptc.clientController.ClientController;

class AllReservationsPanel extends SectionPanel {
    private final ClientController controller;
    private final ReservationListPanel list;

    AllReservationsPanel(ClientController controller) {
        this.controller = controller;
        list = new ReservationListPanel(controller, this::refresh);
        setLayout(new BorderLayout(0, 10));
        add(ViewUtils.createTitle("Todas las reservas"), BorderLayout.NORTH);
        add(list, BorderLayout.CENTER);
    }

    @Override
    void refresh() {
        try {
            list.showReservations(controller.getAllReservations(), true);
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
    }
}
