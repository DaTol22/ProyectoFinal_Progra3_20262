package co.edu.uptc.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

abstract class SectionPanel extends JPanel {

    SectionPanel() {
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    /** Se llama cada vez que la sección se muestra, para recargar sus datos. */
    abstract void refresh();
}