import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Paper;
import java.util.ArrayList;

public class GUI implements ActionListener {
    // Global variables necessary for logging in
    JFrame loginFrame;
    JButton loginButton, resetButton;
    JTextField usernameField;
    JPasswordField passwordField;
    JCheckBox showPassword;

    DBService dbService;

    Faculty user;

    public GUI(DBService dbService) {
        this.dbService = dbService;
        loginWindow();
    }

    public void loginWindow() {
        JLabel usernameLabel, passwordLabel;
        loginFrame = new JFrame("Login Page");
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setPreferredSize(new Dimension(400, 250));

        JPanel firstPanel = new JPanel();
        firstPanel.setLayout(new GridLayout(3, 2));

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        resetButton = new JButton("Reset");

        showPassword = new JCheckBox("Show Password");

        loginFrame.setLayout(null);
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

        loginFrame.add(usernameLabel);
        loginFrame.add(passwordLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordField);
        loginFrame.add(showPassword);
        loginFrame.add(loginButton);
        loginFrame.add(resetButton);

        loginFrame.pack();
        loginFrame.setVisible(true);
        loginFrame.setResizable(false);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == loginButton) {
            String userText;
            String pwdText;
            userText = usernameField.getText();
            pwdText = new String(passwordField.getPassword());

            user = dbService.login(userText, pwdText);
            // sjz@it.rit.edu
            // 5f47859188a602594556580532e814a3
            if (user != null) {
                System.out.println("Logged in as " + user);
                JOptionPane.showMessageDialog(loginFrame,
                        "Login Successful.\nWelcome: " + user.getFName() + " " + user.getLName());
                dbWindow();
                loginFrame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password");
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
        frame.setPreferredSize(new Dimension(1500, 350));

        ArrayList<PapersWithKeywords> papers = dbService.getPapers();

        Object[][] data = new Object[papers.size()][4];

        for (int x = 0; x < data.length; x++) {
            PapersWithKeywords paper = papers.get(x);
            Object[] row = {paper.getTitle(), paper.getText(), paper.getCitation(), paper.getKeywords()};
            data[x] = row;
        }


        String[] columnNames = {"Title", "Abstract", "Citation", "Keywords"};

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(40);
        table.setRowHeight(0, 30);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(370);
        columnModel.getColumn(1).setPreferredWidth(400);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(210);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.setLayout(new BorderLayout());
        frame.add(table.getTableHeader(), BorderLayout.PAGE_START);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
