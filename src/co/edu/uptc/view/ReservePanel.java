package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import co.edu.uptc.clientController.ClientController;
import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.model.BookableResource;
import co.edu.uptc.model.Reservation;

class ReservePanel extends SectionPanel {
    private final ClientController controller;
    private final ResourceType[] types = ResourceType.values();
    private final JComboBox<String> typeCombo = new JComboBox<>();
    private final JPanel rows = new JPanel();

    ReservePanel(ClientController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 10));

        typeCombo.addItem("Todos");
        for (ResourceType type : types) {
            typeCombo.addItem(ViewUtils.typeLabel(type));
        }
        typeCombo.addActionListener(e -> refresh());

        JPanel top = new JPanel(new BorderLayout());
        top.add(ViewUtils.createTitle("Reservar"), BorderLayout.NORTH);
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        filter.add(new JLabel("Filtrar por tipo:  "));
        filter.add(typeCombo);
        top.add(filter, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        JPanel holder = new JPanel(new BorderLayout());
        holder.add(rows, BorderLayout.NORTH);
        add(new JScrollPane(holder), BorderLayout.CENTER);
    }

    @Override
    void refresh() {
        int index = typeCombo.getSelectedIndex();
        ResourceType type = index <= 0 ? null : types[index - 1];
        rows.removeAll();
        boolean empty = true;
        for (BookableResource resource : controller.getResources(type)) {
            rows.add(buildRow(resource));
            empty = false;
        }
        if (empty) {
            rows.add(new JLabel("No hay recursos de este tipo."));
        }
        rows.revalidate();
        rows.repaint();
    }

    private JPanel buildRow(BookableResource resource) {
        JSpinner dateSpinner = ViewUtils.createDateSpinner();
        JSpinner fromSpinner = ViewUtils.createTimeSpinner();
        JSpinner toSpinner = ViewUtils.createTimeSpinner();
        LocalTime start = LocalTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0);
        ViewUtils.setTime(fromSpinner, start);
        ViewUtils.setTime(toSpinner, start.plusHours(1));

        JButton reserve = new JButton("Reservar");
        reserve.addActionListener(e -> reserve(resource, dateSpinner, fromSpinner, toSpinner));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        controls.add(new JLabel("Fecha:"));
        controls.add(dateSpinner);
        controls.add(new JLabel("Franja:"));
        controls.add(fromSpinner);
        controls.add(new JLabel("a"));
        controls.add(toSpinner);
        controls.add(reserve);

        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 4, 8, 4)));
        row.add(new JLabel(ViewUtils.describe(resource)), BorderLayout.NORTH);
        row.add(controls, BorderLayout.CENTER);
        return row;
    }

    private void reserve(BookableResource resource, JSpinner dateSpinner, JSpinner fromSpinner, JSpinner toSpinner) {
        LocalDate date = ViewUtils.getDate(dateSpinner);
        try {
            Reservation reservation = controller.reserve(resource.getId(),
                    LocalDateTime.of(date, ViewUtils.getTime(fromSpinner)),
                    LocalDateTime.of(date, ViewUtils.getTime(toSpinner)));
            String message = "Reserva creada: " + reservation.getId();
            if (reservation.getCost() > 0) {
                message += "\nCosto: $" + reservation.getCost();
            }
            ViewUtils.showInfo(this, message);
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
    }
}
