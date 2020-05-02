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
        String password = "1234";

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
            System.out.println("An error occured when using method getData: " + sqle.getMessage());
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

    public Faculty login(String username, String password) {
        ArrayList<HashMap<String, String>> list = null;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM faculty WHERE email = ? AND password = ?");
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
                return new Faculty(Integer.parseInt(map.get("id")), map.get("fName"), map.get("lName"), map.get("password"), map.get("email"));
            } catch (Exception e) {
                System.out.println("An error occurred when trying to fetch data in login method: " + e.getMessage());
            }
        }

        return null;
    }

    public ArrayList<PapersWithKeywords> getPapers() {
        ArrayList<HashMap<String, String>> papersList = null;
        ArrayList<HashMap<String, String>> keywordsList = null;
        ArrayList<PapersWithKeywords> toReturn = new ArrayList<>();

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

        if (papersList.size() > 0) {
            try {
                //int id, String title, String text, String citation
                for(HashMap<String, String> map : papersList) {
                    PapersWithKeywords paper = new PapersWithKeywords(Integer.parseInt(map.get("id")), map.get("title"), map.get("abstract"), map.get("citation"));
                    for(int x = 0; x < keywordsList.size(); x++) {
                        HashMap<String, String> keywordsMap = keywordsList.get(x);
                        if(keywordsMap.get("id").equals(map.get("id"))) {
                            paper.addKeyword(keywordsMap.get("keyword"));
                            keywordsList.remove(x);
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
}

