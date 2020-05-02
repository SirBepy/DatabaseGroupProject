import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Paper;

public class LoginGUI extends JFrame implements ActionListener {
    JButton loginButton, resetButton;
    JLabel usernameLabel, passwordLabel, papersLabel, paperKeywordsLabel, authorshipLabel, facultyLabel;
    JTextField usernameField, dataField;
    JPasswordField passwordField;
    JCheckBox showPassword;
    JTextArea papersArea, paperKeywordsArea, authorshipArea, facultyArea;
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

            Faculty faculty = dbService.login(userText, pwdText);
            // sjz@it.rit.edu
            // 5f47859188a602594556580532e814a3
            if (faculty != null) {
                System.out.println("Logged in as " + faculty);
                JOptionPane.showMessageDialog(this, "Login Successful.\nWelcome: " + faculty.getFName() + " " + faculty.getLName());
                dbWindow();
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

    public void dbWindow() {
        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setTitle("Viewing the Database");
        frame.setPreferredSize(new Dimension(700, 350));

        JPanel firstPanel = new JPanel();
        firstPanel.setLayout(new GridLayout(3, 2));

        papersLabel = new JLabel("Papers:");
        papersArea = new JTextArea();
        Papers papers = new Papers();
        papersArea.setText(papers.toString());

        paperKeywordsLabel = new JLabel("Paper Keywords:");
        paperKeywordsArea = new JTextArea();
        PaperKeywords paperKeywords = new PaperKeywords();
        paperKeywordsArea.append(paperKeywords.toString());

        authorshipLabel = new JLabel("Authorship:");
        authorshipArea = new JTextArea();
        Authorship authorship = new Authorship();
        authorshipArea.append(authorship.toString());

        facultyLabel = new JLabel("Faculty:");
        facultyArea = new JTextArea();
        Faculty faculty = new Faculty();
        facultyArea.append(faculty.toString());

        frame.setLayout(null);
        papersLabel.setBounds(70, 20, 100, 30);
        papersArea.setBounds(200, 20, 400, 50);
        papersArea.setEditable(false);

        authorshipLabel.setBounds(70, 90, 100, 30);
        authorshipArea.setBounds(200, 90, 400, 50);
        authorshipArea.setEditable(false);

        paperKeywordsLabel.setBounds(70, 160, 100, 30);
        paperKeywordsArea.setBounds(200, 160, 400, 50);
        paperKeywordsArea.setEditable(false);

        facultyLabel.setBounds(70, 240, 100, 30);
        facultyArea.setBounds(200, 240, 400, 50);
        facultyArea.setEditable(false);

        frame.add(papersLabel);
        frame.add(papersArea);
        frame.add(paperKeywordsLabel);
        frame.add(paperKeywordsArea);
        frame.add(facultyLabel);
        frame.add(facultyArea);
        frame.add(authorshipLabel);
        frame.add(authorshipArea);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
