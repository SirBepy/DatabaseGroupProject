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
    JFrame dbFrame;
    JFrame tableFrame;
    JButton loginButton, resetButton, insertButton, saveButton;
    JTextField usernameField;
    JPasswordField passwordField;
    JCheckBox showPassword;
    JLabel titleLabel, textLabel, citationLabel, keywordsLabel;
    JTextField titleTextField, citationTextField, keywordsTextField;
    JTextArea textField;
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
        if (actionEvent.getSource() == insertButton) {
            insertTable();
        }
        if (actionEvent.getSource() == saveButton) {
            saveToTable();
            dbWindow();
            tableFrame.setVisible(false);
        }
    }

    public void dbWindow() {
        dbFrame = new JFrame();
//        dbFrame.setLocationRelativeTo(null);
        dbFrame.setTitle("Viewing the Database");
        dbFrame.setPreferredSize(new Dimension(1500, 350));

        insertButton = new JButton("Insert New");
        insertButton.setPreferredSize(new Dimension(100, 20));
        insertButton.addActionListener(this);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(insertButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

        ArrayList<PapersWithKeywords> papers = dbService.getPapers();

        Object[][] data = new Object[papers.size()][4];

        for (int x = 0; x < data.length; x++) {
            PapersWithKeywords paper = papers.get(x);
            Object[] row = {paper.getTitle(), paper.getText(), paper.getCitation(), paper.getKeywords()};
            data[x] = row;
        }

        String[] columnNames = {"Title", "Abstract", "Citation", "Keywords"};

        JTable table = new JTable(data, columnNames);
        table.setBounds(10, 50, 1400, 200);
        table.setRowHeight(40);
        table.setRowHeight(0, 30);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(370);
        columnModel.getColumn(1).setPreferredWidth(400);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(210);

        JScrollPane scrollPane = new JScrollPane(table);
        dbFrame.setLayout(new BorderLayout());
        dbFrame.add(buttonPane, BorderLayout.NORTH);
        dbFrame.add(table.getTableHeader(), BorderLayout.PAGE_START);
        dbFrame.add(scrollPane, BorderLayout.CENTER);

        dbFrame.pack();
        dbFrame.setVisible(true);
        dbFrame.setResizable(false);
        dbFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void insertTable() {
        tableFrame = new JFrame();
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setTitle("Insert into Database");
        tableFrame.setPreferredSize(new Dimension(500, 350));

        saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(70, 20));
        saveButton.addActionListener(this);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(saveButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

        titleLabel = new JLabel("Title: ");
        titleTextField = new JTextField();
        textLabel = new JLabel("Abstract: ");
        textField = new JTextArea();
        textField.setLineWrap(true);
        citationLabel = new JLabel("Citation: ");
        citationTextField = new JTextField();
        keywordsLabel = new JLabel("Keywords: ");
        keywordsTextField = new JTextField();

        titleLabel.setBounds(10, 30, 100, 30);
        titleTextField.setBounds(100, 40, 250, 20);
        textLabel.setBounds(10, 60, 100, 30);
        textField.setBounds(100, 70, 250, 60);
        citationLabel.setBounds(10, 150, 100, 30);
        citationTextField.setBounds(100, 160, 250, 20);
        keywordsLabel.setBounds(10, 180, 100, 30);
        keywordsTextField.setBounds(100, 190, 250, 20);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(null);
        fieldsPanel.add(titleLabel);
        fieldsPanel.add(titleTextField);
        fieldsPanel.add(textLabel);
        fieldsPanel.add(textField);
        fieldsPanel.add(citationLabel);
        fieldsPanel.add(citationTextField);
        fieldsPanel.add(keywordsLabel);
        fieldsPanel.add(keywordsTextField);

        tableFrame.setLayout(new BorderLayout());
        tableFrame.add(buttonPane, BorderLayout.SOUTH);
        tableFrame.add(fieldsPanel, BorderLayout.CENTER);

        tableFrame.pack();
        tableFrame.setVisible(true);
        tableFrame.setResizable(false);
        tableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void saveToTable() {
        String title = titleTextField.getText();
        String text = textField.getText();
        String citation = citationTextField.getText();
        String[] keywords = keywordsTextField.getText().split(",");

        PapersWithKeywords paper = new PapersWithKeywords(title, text, citation);

        for(String keyword : keywords) {
            System.out.println(keyword);
            if(keyword.charAt(0) == ' ') {
                keyword.substring(1, keyword.length());
            }
            if(keyword.charAt(keyword.length() - 1) == ' ') {
                keyword.substring(0, keyword.length() - 1);
            }
            paper.addKeyword(keyword);
        }

        dbService.insertPapersAndKeywords(paper);
    }

}
