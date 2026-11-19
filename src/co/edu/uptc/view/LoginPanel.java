package co.edu.uptc.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import co.edu.uptc.controller.ClientController;

class LoginPanel extends JPanel {
    private final ClientController controller;
    private final Runnable onLogin;
    private final JTextField nameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);

    LoginPanel(ClientController controller, Runnable onLogin) {
        this.controller = controller;
        this.onLogin = onLogin;
        setLayout(new GridLayout(1, 2));
        add(buildBanner());
        add(buildForm());
    }

    private JPanel buildBanner() {
        JPanel banner = new JPanel(new GridBagLayout());
        banner.setBackground(new Color(40, 70, 120));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(4, 4, 4, 4);

        JLabel title = ViewUtils.createTitle("Reservas");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(40f));
        c.gridy = 0;
        banner.add(title, c);

        JLabel subtitle = new JLabel("Salas · Canchas · Parqueadero");
        subtitle.setForeground(Color.WHITE);
        c.gridy = 1;
        banner.add(subtitle, c);
        return banner;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        c.gridy = 0;
        form.add(ViewUtils.createTitle("Log in"), c);
        c.gridy = 1;
        form.add(new JLabel("Usuario:"), c);
        c.gridy = 2;
        form.add(nameField, c);
        c.gridy = 3;
        form.add(new JLabel("Contraseña:"), c);
        c.gridy = 4;
        form.add(passwordField, c);

        JButton enter = new JButton("Entrar");
        JButton register = new JButton("Registrarse");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.add(enter);
        buttons.add(new JLabel("  "));
        buttons.add(register);
        c.gridy = 5;
        c.insets = new Insets(12, 4, 4, 4);
        form.add(buttons, c);

        enter.addActionListener(e -> login());
        nameField.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
        register.addActionListener(e -> new RegisterDialog(SwingUtilities.getWindowAncestor(this), controller)
                .setVisible(true));
        return form;
    }

    private void login() {
        try {
            controller.login(nameField.getText(), new String(passwordField.getPassword()));
            onLogin.run();
        } catch (RuntimeException ex) {
            ViewUtils.showError(this, ex.getMessage());
        }
    }

    void clear() {
        nameField.setText("");
        passwordField.setText("");
    }
}