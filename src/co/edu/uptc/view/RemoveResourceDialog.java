package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import co.edu.uptc.controller.ClientController;
import co.edu.uptc.model.BookableResource;

class RemoveResourceDialog extends JDialog {
    private final ClientController controller;
    private final JComboBox<ResourceItem> resourceCombo = new JComboBox<>();

    RemoveResourceDialog(Window owner, ClientController controller) {
        super(owner, "Remove", Dialog.ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        for (BookableResource resource : controller.getResources(null)) {
            resourceCombo.addItem(new ResourceItem(resource));
        }

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        form.add(new JLabel("Recurso:"));
        form.add(resourceCombo);

        JButton remove = new JButton("Remove");
        JButton cancel = new JButton("Cancel");
        remove.addActionListener(e -> remove());
        cancel.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(remove);
        buttons.add(cancel);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void remove() {
        ResourceItem item = (ResourceItem) resourceCombo.getSelectedItem();
        if (item == null) {
            return;
        }
        try {
            controller.removeResource(item.getResource().getId());
            ViewUtils.showInfo(this, "Recurso eliminado.");
            dispose();
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
    }
}
