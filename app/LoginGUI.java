import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame implements ActionListener {
    JButton loginButton, resetButton;
    JLabel usernameLabel, passwordLabel;
    JTextField usernameField;
    JPasswordField passwordField;
    JCheckBox showPassword;
    DBService dbService;

    public LoginGUI(DBService dbService) {
        this.dbService = dbService;
        loginWindow();
    }

    public void loginWindow() {
        setLocationRelativeTo(null);
        setTitle("Login Page");
        setPreferredSize(new Dimension(400, 250));

        JPanel firstPanel = new JPanel();
        firstPanel.setLayout(new GridLayout(3, 2));

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        resetButton = new JButton("Reset");

        showPassword = new JCheckBox("Show Password");

        setLayout(null);
        usernameLabel.setBounds(70, 20, 100, 30);
        passwordLabel.setBounds(70, 60, 100, 30);
        usernameField.setBounds(150, 20, 150, 30);
        passwordField.setBounds(150, 60, 150, 30);
        showPassword.setBounds(185, 100, 150, 30);
        loginButton.setBounds(70, 150, 100, 30);
        resetButton.setBounds(200, 150, 100, 30);

        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);

        add(usernameLabel);
        add(passwordLabel);
        add(usernameField);
        add(passwordField);
        add(showPassword);
        add(loginButton);
        add(resetButton);

        pack();
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == loginButton) {
            String userText;
            String pwdText;
            userText = usernameField.getText();
            pwdText = passwordField.getText();

            if (userText.equalsIgnoreCase("sjz@it.rit.edu") && pwdText.equalsIgnoreCase("5f47859188a602594556580532e814a3")) {
                JOptionPane.showMessageDialog(this, "Login Successful.\nWelcome: Steve Zilora");
            } else if (userText.equalsIgnoreCase("dsb@it.rit.edu") && pwdText.equalsIgnoreCase("f4f6172eb26581952a70d7199bfd2ddb")) {
                JOptionPane.showMessageDialog(this, "Login Successful.\nWelcome: Dan Bogaard");
            } else if (userText.equalsIgnoreCase("kdgvks@rit.edu") && pwdText.equalsIgnoreCase("084387d79f1cae0cecd9a8eaccbd23b3")) {
                JOptionPane.showMessageDialog(this, "Login Successful.\nWelcome: Karen Griffith");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        }
        if (actionEvent.getSource() == resetButton) {
            usernameField.setText("");
            passwordField.setText("");
        }
        if (actionEvent.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        }
    }

}
