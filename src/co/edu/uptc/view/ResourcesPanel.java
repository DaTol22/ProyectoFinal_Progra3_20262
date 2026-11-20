package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import co.edu.uptc.clientController.ClientController;

class ResourcesPanel extends SectionPanel {
    private final ClientController controller;

    ResourcesPanel(ClientController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 10));
        add(ViewUtils.createTitle("Gestionar recursos"), BorderLayout.NORTH);

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton remove = new JButton("Remove");
        for (JButton button : new JButton[] { add, edit, remove }) {
            button.setPreferredSize(new Dimension(110, 36));
        }
        add.addActionListener(e -> new ResourceDialog(SwingUtilities.getWindowAncestor(this), controller, false)
                .setVisible(true));
        edit.addActionListener(e -> openIfThereAreResources(true));
        remove.addActionListener(e -> openIfThereAreResources(false));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        buttons.add(add);
        buttons.add(edit);
        buttons.add(remove);
        add(buttons, BorderLayout.CENTER);
    }

    private void openIfThereAreResources(boolean edit) {
        if (controller.getResources(null).size() == 0) {
            ViewUtils.showInfo(this, "No hay recursos registrados.");
            return;
        }
        if (edit) {
            new ResourceDialog(SwingUtilities.getWindowAncestor(this), controller, true).setVisible(true);
        } else {
            new RemoveResourceDialog(SwingUtilities.getWindowAncestor(this), controller).setVisible(true);
        }
    }

    @Override
    void refresh() {
        // no hay datos que recargar
    }
}
