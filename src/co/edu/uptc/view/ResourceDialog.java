package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import co.edu.uptc.clientController.ClientController;
import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.model.BookableResource;
import co.edu.uptc.model.ParkingSpot;
import co.edu.uptc.model.StudyRoom;

/** Diálogo para agregar (edit = false) o editar (edit = true) un recurso. */
class ResourceDialog extends JDialog {
    private final ClientController controller;
    private final boolean edit;
    private final ResourceType[] types = ResourceType.values();
    private final JTextField nameField = new JTextField(15);
    private final JSpinner opensSpinner = ViewUtils.createTimeSpinner();
    private final JSpinner closesSpinner = ViewUtils.createTimeSpinner();
    private final JLabel extraLabel = new JLabel();
    private final JSpinner extraSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000000, 1));
    private JComboBox<String> typeCombo;
    private JComboBox<ResourceItem> resourceCombo;
    private ResourceType currentType;
    private String currentId;

    ResourceDialog(Window owner, ClientController controller, boolean edit) {
        super(owner, edit ? "Editar recurso" : "Agregar recurso", Dialog.ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        this.edit = edit;

        JPanel form = new JPanel(new GridBagLayout());
        if (edit) {
            resourceCombo = new JComboBox<>();
            for (BookableResource resource : controller.getResources(null)) {
                resourceCombo.addItem(new ResourceItem(resource));
            }
            addRow(form, 0, new JLabel("Recurso:"), resourceCombo);
        } else {
            typeCombo = new JComboBox<>();
            for (ResourceType type : types) {
                typeCombo.addItem(ViewUtils.typeLabel(type));
            }
            addRow(form, 0, new JLabel("Tipo:"), typeCombo);
        }
        addRow(form, 1, new JLabel("Nombre:"), nameField);
        addRow(form, 2, new JLabel("Abre:"), opensSpinner);
        addRow(form, 3, new JLabel("Cierra:"), closesSpinner);
        addRow(form, 4, extraLabel, extraSpinner);
        addRow(form, 5, new JLabel(" "), new JLabel("Para 24 horas use 00:00 a 23:59"));

        ViewUtils.setTime(opensSpinner, LocalTime.of(8, 0));
        ViewUtils.setTime(closesSpinner, LocalTime.of(18, 0));

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        save.addActionListener(e -> save());
        cancel.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(save);
        buttons.add(cancel);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        if (edit) {
            resourceCombo.addActionListener(e -> loadSelectedResource());
            loadSelectedResource();
        } else {
            typeCombo.addActionListener(e -> selectType());
            selectType();
        }
        pack();
        setLocationRelativeTo(owner);
    }

    private void addRow(JPanel form, int row, Component label, Component field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        form.add(label, c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        form.add(field, c);
    }

    private void selectType() {
        currentType = types[typeCombo.getSelectedIndex()];
        currentId = null;
        updateExtra(-1);
    }

    private void loadSelectedResource() {
        BookableResource resource = ((ResourceItem) resourceCombo.getSelectedItem()).getResource();
        currentType = resource.getType();
        currentId = resource.getId();
        nameField.setText(resource.getName());
        ViewUtils.setTime(opensSpinner, resource.getHours().getOpens());
        ViewUtils.setTime(closesSpinner, resource.getHours().getCloses());
        int extra = -1;
        if (resource instanceof ParkingSpot) {
            extra = ((ParkingSpot) resource).getRatePerHour();
        } else if (resource instanceof StudyRoom) {
            extra = (int) ((StudyRoom) resource).getSlotDuration().toMinutes();
        }
        updateExtra(extra);
    }

    /** Muestra el campo extra según el tipo; value < 0 usa el valor por defecto. */
    private void updateExtra(int value) {
        boolean visible = true;
        if (currentType == ResourceType.PARKING) {
            extraLabel.setText("Tarifa por hora ($):");
            extraSpinner.setModel(new SpinnerNumberModel(value >= 0 ? value : 1000, 0, 1000000, 100));
        } else if (currentType == ResourceType.STUDY_ROOM) {
            extraLabel.setText("Duración de franja (min):");
            extraSpinner.setModel(new SpinnerNumberModel(value >= 0 ? value : 60, 5, 480, 5));
        } else {
            visible = false;
        }
        extraLabel.setVisible(visible);
        extraSpinner.setVisible(visible);
        pack();
    }

    private void save() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            ViewUtils.showError(this, "El nombre no puede estar vacío.");
            return;
        }
        LocalTime opens = ViewUtils.getTime(opensSpinner);
        LocalTime closes = ViewUtils.getTime(closesSpinner);
        if (!opens.isBefore(closes)) {
            ViewUtils.showError(this, "La hora de apertura debe ser anterior a la de cierre.");
            return;
        }
        int extra = extraSpinner.isVisible() ? (Integer) extraSpinner.getValue() : 0;
        try {
            if (edit) {
                controller.editResource(currentId, currentType, name, opens, closes, extra);
            } else {
                controller.addResource(currentType, name, opens, closes, extra);
            }
            dispose();
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
    }
}
