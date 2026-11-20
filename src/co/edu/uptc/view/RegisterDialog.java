package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import co.edu.uptc.clientController.ClientController;

class RegisterDialog extends JDialog {
    private final ClientController controller;
    private final JTextField nameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);

    RegisterDialog(Window owner, ClientController controller) {
        super(owner, "Registro", Dialog.ModalityType.APPLICATION_MODAL);
        this.controller = controller;

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        form.add(new JLabel("Nombre:"), c);
        c.gridx = 1;
        form.add(nameField, c);
        c.gridx = 0;
        c.gridy = 1;
        form.add(new JLabel("Password:"), c);
        c.gridx = 1;
        form.add(passwordField, c);

        JButton register = new JButton("Registrarse");
        JButton close = new JButton("Cerrar");
        register.addActionListener(e -> register());
        close.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(register);
        buttons.add(close);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void register() {
        try {
            controller.register(nameField.getText(), new String(passwordField.getPassword()));
            ViewUtils.showInfo(this, "Usuario registrado. Ya puedes iniciar sesión.");
            dispose();
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
    }
}