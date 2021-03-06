import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI implements ActionListener {
    // Global UI variables
    private JFrame loginFrame, tableFrame, dbFrame, requestFrame = new JFrame();
    private JButton loginButton, resetButton, insertButton, saveButton, guestButton, notificationButton, okButton,
            requestButton, sendButton;
    private JCheckBox showPassword;
    private JLabel titleLabel, textLabel, citationLabel, keywordsLabel, requestLabel, detailsLabel,
            reqProfLabel;
    private JPasswordField passwordField;
    private JTextField titleTextField, citationTextField, keywordsTextField, requestTextField,
            reqProfTextField, usernameField;
    private JTextArea textField, detailsTextField;

    // Other necessary variables
    private DBService dbService;
    private User user;
    private ArrayList<SpeakingRequest> requests = new ArrayList<>();

    public GUI(DBService dbService) {
        this.dbService = dbService;
        loginWindow();
    }

    public void loginWindow() {
        JLabel usernameLabel, passwordLabel;
        loginFrame = new JFrame("Login Page");
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setPreferredSize(new Dimension(400, 300));

        JPanel firstPanel = new JPanel();
        firstPanel.setLayout(new GridLayout(3, 2));

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        resetButton = new JButton("Reset");
        guestButton = new JButton("Login as Guest");

        showPassword = new JCheckBox("Show Password");
        loginFrame.getRootPane().setDefaultButton(loginButton);

        loginFrame.setLayout(null);
        usernameLabel.setBounds(70, 20, 100, 30);
        passwordLabel.setBounds(70, 60, 100, 30);
        usernameField.setBounds(150, 20, 150, 30);
        passwordField.setBounds(150, 60, 150, 30);
        showPassword.setBounds(185, 100, 150, 30);
        loginButton.setBounds(70, 150, 100, 30);
        resetButton.setBounds(200, 150, 100, 30);
        guestButton.setBounds(115, 190, 150, 25);

        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        guestButton.addActionListener(this);
        showPassword.addActionListener(this);

        loginFrame.add(usernameLabel);
        loginFrame.add(passwordLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordField);
        loginFrame.add(showPassword);
        loginFrame.add(loginButton);
        loginFrame.add(resetButton);
        loginFrame.add(guestButton);

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
        } else if (actionEvent.getSource() == resetButton) {
            usernameField.setText("");
            passwordField.setText("");
        } else if (actionEvent.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        } else if (actionEvent.getSource() == insertButton) {
            insertTable();
        } else if (actionEvent.getSource() == saveButton) {
            saveToTable();
            dbFrame.setVisible(false);
            dbWindow();
            tableFrame.setVisible(false);
        } else if (actionEvent.getSource() == guestButton) {
            // int id, String fName, String lName, String password, String email
            user = new User(-1, "Guest", "", "guest", "GUEST");
            System.out.println("Demo logged in as guest");
            JOptionPane.showMessageDialog(loginFrame, "Welcome guest!");
            dbWindow();
            loginFrame.setVisible(false);
        } else if (actionEvent.getSource() == notificationButton) {
            requestFrame.setVisible(false);
            viewRequests();
        } else if (actionEvent.getSource() == requestButton) {
            requestFrame.setVisible(false);
            requestSpeaker();
        } else if (actionEvent.getSource() == okButton) {
            requestFrame.setVisible(false);
        } else if (actionEvent.getSource() == sendButton) {
            if (reqProfTextField.getText().equals(user.getEmail())) {
                JOptionPane.showMessageDialog(loginFrame, "Cannot send request to self!");
            } else {

                boolean bool = dbService.postRequest(reqProfTextField.getText(), new SpeakingRequest(user.getId(), requestTextField.getText(), detailsTextField.getText()));
                if (!bool) {
                    JOptionPane.showMessageDialog(null, "No faculty member with that email!");
                } else {
                    JOptionPane.showMessageDialog(null, "Request sent!");
                    requestFrame.setVisible(false);
                }
            }
        }
    }

    public void dbWindow() {
        dbFrame = new JFrame();
        startJFrameSettings(dbFrame, "Viewing the Database", 1500, 1000);

        insertButton = new JButton("Insert New");
        insertButton.setPreferredSize(new Dimension(130, 20));
        insertButton.addActionListener(this);

        requestButton = new JButton("Request Speaker");
        requestButton.setPreferredSize(new Dimension(160, 20));
        requestButton.addActionListener(this);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());

        ArrayList<ModifiedPapers> papers = dbService.fetchPapers();
        
        String[] columnNames = {"Author", "Title", "Text", "Citation", "Keywords"};


        Object[][] data = new Object[papers.size()][columnNames.length];

        for (int x = 0; x < data.length; x++) {
            ModifiedPapers paper = papers.get(x);
            Object[] row = {paper.getAuthorship(), paper.getTitle(), paper.getText(), paper.getCitation(),
                    paper.getKeywords()};
            data[x] = row;
        }


        JTable table = new JTable(data, columnNames);
        table.setRowHeight(300);
        resizeRows(table);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(400);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(210);
        columnModel.getColumn(0).setCellRenderer(new TextTableRenderer()); 
        columnModel.getColumn(1).setCellRenderer(new TextTableRenderer()); 
        columnModel.getColumn(2).setCellRenderer(new TextTableRenderer()); 
        columnModel.getColumn(3).setCellRenderer(new TextTableRenderer()); 
        columnModel.getColumn(4).setCellRenderer(new TextTableRenderer()); 


        JScrollPane scrollPane = new JScrollPane(table);

        dbFrame.setLayout(new BorderLayout());
        if (user.getRole().equals("STUDENT")) {
            buttonPane.add(requestButton);
            buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        } else if (user.getRole().equals("FACULTY")) {
            buttonPane.add(requestButton);
            buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
            buttonPane.add(insertButton);
            buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

            requests = dbService.fetchSpeakingRequestsById(user);
            if (requests.size() != 0) {
                notificationButton = new JButton("View " + requests.size() + " Requests");
                notificationButton.setPreferredSize(new Dimension(200, 20));
                notificationButton.addActionListener(this);
                buttonPane.add(notificationButton);
                buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
            }
        }
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
        startJFrameSettings(tableFrame, "Insert into Database", 500, 350);

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
        textLabel = new JLabel("Text: ");
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

        endJFrameSettings(tableFrame, fieldsPanel);
        tableFrame.add(buttonPane, BorderLayout.SOUTH);
    }

    public void saveToTable() {
        String title = titleTextField.getText();
        String text = textField.getText();
        String citation = citationTextField.getText();
        String[] keywords = keywordsTextField.getText().split(",");

        ModifiedPapers paper = new ModifiedPapers(title, text, citation);

        for (String keyword : keywords) {
            if (Character.isWhitespace(keyword.charAt(0))) {
                keyword = keyword.substring(1, keyword.length());
            }
            if (Character.isWhitespace(keyword.charAt(keyword.length() - 1))) {
                keyword = keyword.substring(0, keyword.length() - 1);
            }
            paper.addKeyword(keyword);
        }

        dbService.postPapersAndKeywords(paper, user);
    }

    public void requestSpeaker() {
        requestFrame = new JFrame();

        startJFrameSettings(requestFrame, "Requests", 480, 300);

        reqProfLabel = new JLabel("Professors email: ");
        reqProfTextField = new JTextField();
        requestLabel = new JLabel("Title: ");
        requestTextField = new JTextField();
        detailsLabel = new JLabel("Details: ");
        detailsTextField = new JTextArea();
        detailsTextField.setLineWrap(true);

        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        reqProfLabel.setBounds(10, 10, 150, 30);
        reqProfTextField.setBounds(160, 10, 250, 20);

        requestLabel.setBounds(10, 50, 100, 30);
        requestTextField.setBounds(160, 50, 250, 20);

        detailsLabel.setBounds(10, 90, 100, 30);
        detailsTextField.setBounds(160, 90, 250, 60);

        sendButton.setBounds(190, 210, 70, 20);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(null);
        fieldsPanel.add(reqProfLabel);
        fieldsPanel.add(reqProfTextField);
        fieldsPanel.add(requestLabel);
        fieldsPanel.add(requestTextField);
        fieldsPanel.add(detailsLabel);
        fieldsPanel.add(detailsTextField);
        fieldsPanel.add(sendButton);

        endJFrameSettings(requestFrame, fieldsPanel);
    }

    public void viewRequests() {
        requestFrame = new JFrame("View Requests");

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(null);
        String[] columnNames = {"Name", "Title", "Description", "Email"};

        Object[][] data = new Object[requests.size()][columnNames.length];

        for (int x = 0; x < data.length; x++) {
            SpeakingRequest request = requests.get(x);
            Object[] row = {request.getName(), request.getTitle(), request.getDescription(), request.getEmail()};
            data[x] = row;
        }

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(300);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(400);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(210);
        columnModel.getColumn(0).setCellRenderer(new TextTableRenderer()); 
        columnModel.getColumn(1).setCellRenderer(new TextTableRenderer()); 
        columnModel.getColumn(2).setCellRenderer(new TextTableRenderer()); 
        columnModel.getColumn(3).setCellRenderer(new TextTableRenderer()); 

        JScrollPane scrollPane = new JScrollPane(table);

        endJFrameSettings(requestFrame, scrollPane);
    }

    private void endJFrameSettings(JFrame frame, JPanel panel) {
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private void endJFrameSettings(JFrame frame, JScrollPane panel) {
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private void startJFrameSettings(JFrame frame, String title, int height, int width) {
        frame.setTitle(title);
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(height, width));
    }

    private void resizeRows(JTable table) {
        for (int row = 0; row < table.getRowCount(); row++)
        {
            int rowHeight = table.getRowHeight();
            
            for (int column = 0; column < table.getColumnCount(); column++)
            {
                int newHeight = ("" + table.getValueAt(row, column)).length()/4;
                rowHeight = Math.max(rowHeight, newHeight);
            }
            table.setRowHeight(row, rowHeight);
        }
    }

}
