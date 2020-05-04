import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;

public class DBService {
    private Connection conn = null;

    public DBService() {
        String driver = "com.mysql.jdbc.Driver";
        String serverName = "localhost";
        String mydatabase = "FacResearchDB";
        String username = "root";
        String password = "SumanSQL31334!";

        String url = "jdbc:mysql://" + serverName + "/" + mydatabase + "?useSSL=false";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Driver not found");
            System.exit(1);
        }

        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException sqle) {
            System.out.println("There is something wrong with the connection: " + sqle.getMessage());
            System.exit(1);
        }
    }

    /**
     * getData
     *
     * @param stmt String that contains the SQL query that will be made
     * @return All objects that that query returns
     */
    private ArrayList<HashMap<String, String>> getData(PreparedStatement stmt) {
        ArrayList<HashMap<String, String>> toReturn = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData data = rs.getMetaData();

            // Initializing dataNames so we can use it to get data later on
            int colLength = data.getColumnCount();
            String[] dataNames = new String[colLength];
            for (int x = 0; x < colLength; ) {
                dataNames[x] = data.getColumnName(++x);
            }

            // Creating a bunch of String[] objects to add to the arraylist
            while (rs.next()) {
                HashMap<String, String> map = new HashMap<>();

                for (int x = 0; x < colLength; x++) {
                    map.put(dataNames[x], rs.getString(dataNames[x]));
                }
                toReturn.add(map);
            }

            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            System.out.println("An error occurred when using method getData: " + sqle.getMessage());
        }
        return toReturn;
    }

    /**
     * setData
     *
     * @param stmt String that contains the SQL query that will be made
     * @return true if the query was successful, false if it wasn't
     */
    private boolean setData(PreparedStatement stmt) {
        try {
            int numOfRows = stmt.executeUpdate();
            stmt.close();
            if (numOfRows > 0)
                return true;
        } catch (SQLException sqle) {
            System.out.println("An error occurred when using method setData: " + sqle.getMessage());
        }
        return false;
    }

    /**
     * Closes the Connection object
     */
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User login(String username, String password) {
        ArrayList<HashMap<String, String>> list = null;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            list = getData(stmt);
        } catch (SQLException e) {
            System.out.println("An error occurred when sending prepared statement in login method: " + e.getMessage());
            return null;
        }

        if (list.size() > 0) {
            try {
                HashMap<String, String> map = list.get(0);
                return new User(Integer.parseInt(map.get("id")), map.get("fName"), map.get("lName"), map.get("email"),
                        map.get("role"));
            } catch (Exception e) {
                System.out.println("An error occurred when trying to fetch data in login method: " + e.getMessage());
            }
        }

        return null;
    }

    public ArrayList<ModifiedPapers> fetchPapers() {
        ArrayList<HashMap<String, String>> papersList = null;
        ArrayList<HashMap<String, String>> keywordsList = null;
        ArrayList<HashMap<String, String>> authorshipList = null;

        ArrayList<ModifiedPapers> toReturn = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM papers");
            papersList = getData(stmt);
        } catch (SQLException e) {
            System.out.println("An error occurred when sending prepared statement in login method: " + e.getMessage());
            return null;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM paper_keywords");
            keywordsList = getData(stmt);
        } catch (SQLException e) {
            System.out.println("An error occurred when sending prepared statement in login method: " + e.getMessage());
            return null;
        }

        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT paperId, id, fName, lName FROM authorship LEFT JOIN user "
                            + "ON authorship.facultyid = user.id ORDER BY paperId;");
            authorshipList = getData(stmt);
        } catch (SQLException e) {
            System.out.println("An error occurred when sending prepared statement in login method: " + e.getMessage());
            return null;
        }

        if (papersList.size() > 0) {
            try {
                for (HashMap<String, String> map : papersList) {
                    ModifiedPapers paper = new ModifiedPapers(Integer.parseInt(map.get("id")), map.get("title"),
                            map.get("abstract"), map.get("citation"));
                    for (int x = 0; x < keywordsList.size(); ) {
                        HashMap<String, String> keywordsMap = keywordsList.get(x);
                        if (keywordsMap.get("id").equals(map.get("id"))) {
                            paper.addKeyword(keywordsMap.get("keyword"));
                            keywordsList.remove(x);
                        } else {
                            x++;
                        }
                    }

                    for (int x = 0; x < authorshipList.size(); ) {
                        HashMap<String, String> authorshipMap = authorshipList.get(x);
                        if (authorshipMap.get("paperId").equals(map.get("id"))) {
                            paper.addAuthor(authorshipMap.get("fName") + " " + authorshipMap.get("lName"));
                            authorshipList.remove(x);
                        } else {
                            x++;
                        }
                    }
                    toReturn.add(paper);
                }
            } catch (Exception e) {
                System.out.println("An error occurred when trying to fetch data in login method: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return toReturn;
    }

    public boolean postPapersAndKeywords(ModifiedPapers modifiedPapers, User user) {
        try {
            // These first three lines exist because there is no autoincrement in the table
            PreparedStatement idStmt = conn.prepareStatement("SELECT id FROM papers ORDER BY id");
            ArrayList<HashMap<String, String>> list = getData(idStmt);
            int newId = Integer.parseInt(list.get(list.size() - 1).get("id"));
            modifiedPapers.setId(++newId);

            // Paper insert
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO papers VALUES(?, ?, ?, ?)");
            stmt1.setInt(1, modifiedPapers.getId());
            stmt1.setString(2, modifiedPapers.getTitle());
            stmt1.setString(3, modifiedPapers.getText());
            stmt1.setString(4, modifiedPapers.getCitation());
            boolean insert1 = setData(stmt1);

            // Keyword insert
            for (String keyword : modifiedPapers.getKeywordsList()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO paper_keywords(id, keyword) VALUES(?, ?)");
                stmt.setInt(1, modifiedPapers.getId());
                stmt.setString(2, keyword);
                if (!setData(stmt)) {
                    System.out.println("Not everything was written into the database, abort.");
                    return false;
                }
            }

            // Keyword insert
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO authorship(facultyid, paperid) VALUES(?, ?)");
            stmt2.setInt(1, user.getId());
            stmt2.setInt(2, modifiedPapers.getId());
            boolean insert2 = setData(stmt2);

            if (insert1 && insert2) {
                System.out.println("It all worked");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Didn't really all work...");
        return false;
    }

    public ArrayList<SpeakingRequest> fetchSpeakingRequestsById(User user) {
        // SELECT * FROM speaking_request WHERE receiverid = 1;
        ArrayList<HashMap<String, String>> list = null;
        ArrayList<SpeakingRequest> toReturn = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT speaking_request.id, receiverid, senderid, title, description, CONCAT(lName, ', ',fName) AS 'name', email "
                            + "FROM speaking_request left join user on speaking_request.senderid = user.id WHERE receiverid = ?");
            stmt.setInt(1, user.getId());
            list = getData(stmt);
        } catch (SQLException e) {
            System.out.println("An error occurred when sending prepared statement in fetchSpeakingRequestsById method: " + e.getMessage());
            return null;
        }

        if (list.size() > 0) {
            try {
                for (HashMap<String, String> map : list) {
                    int id = Integer.parseInt(map.get("id"));
                    int receiverid = Integer.parseInt(map.get("receiverid"));
                    int senderid = Integer.parseInt(map.get("senderid"));
                    toReturn.add(new SpeakingRequest(id, receiverid, senderid, map.get("name"), map.get("email"), map.get("title"),
                            map.get("description")));
                }
            } catch (Exception e) {
                System.out.println("An error occurred when trying to fetch data in fetchSpeakingRequestsById method: " + e.getMessage());
            }
        }

        return toReturn;
    }

    public boolean postRequest(String email, SpeakingRequest request) {
        int receiverid = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT id FROM user WHERE role = 'FACULTY' AND email = ? ");
            stmt.setString(1, email);
            ArrayList<HashMap<String, String>> list = getData(stmt);
            if (list.size() != 0) {
                receiverid = Integer.parseInt(list.get(0).get("id"));
            } else {
                System.out.println("No faculty members found with that email");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("No faculty members found with that email");
            return false;
        }
        // Request insert 
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO speaking_request(receiverid, senderid, title, description) VALUES(?, ?, ?, ?)");
            stmt.setInt(1, receiverid);
            stmt.setInt(2, request.getSenderId());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());
            return setData(stmt);
        } catch (Exception e) {
            System.out.println("An error has occured when inserting request " + e.getMessage());
            return false;
        }
    }

}
