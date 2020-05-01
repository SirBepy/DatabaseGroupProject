import java.sql.*;
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
     * @param sqlQuery String that contains the SQL query that will be made
     * @return All objects that that query returns
     */
    private ArrayList<String[]> getData(String sqlQuery) {
        ArrayList<String[]> toReturn = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);
            ResultSetMetaData data = rs.getMetaData();

            // Initializing dataNames so we can use it to get data later on
            int colLength = data.getColumnCount();
            String[] dataNames = new String[colLength];
            for (int x = 0; x < colLength;) {
                dataNames[x] = data.getColumnName(++x);
            }

            // Creating a bunch of String[] objects to add to the arraylist
            while (rs.next()) {
                String[] row = new String[colLength];
                for (int x = 0; x < colLength; x++) {
                    row[x] = rs.getString(dataNames[x]);
                }
                toReturn.add(row);
            }

            rs.close();
            statement.close();
        } catch (SQLException sqle) {
            System.out.println("An error occured when using method getData: " + sqle.getMessage());
            System.exit(1);
        }
        return toReturn;
    }

    /**
     * setData
     * 
     * @param sqlQuery String that contains the SQL query that will be made
     * @return true if the query was sucsessful, false if it wasn't
     */
    private boolean setData(String sqlQuery) {
        try {
            Statement statement = conn.createStatement();
            int numOfRows = statement.executeUpdate(sqlQuery);
            statement.close();
            if (numOfRows > 0)
                return true;
        } catch (SQLException sqle) {
            System.out.println("An error occured when using method setData: " + sqle.getMessage());
            System.exit(1);
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

    public int login(String username, String password) {
        ArrayList<String[]> list = getData("SELECT id FROM faculty WHERE email = " + username + " AND password = " + password);
        if (list.size() > 0) {
            try {
                return Integer.parseInt(list.get(0)[0]);
            } catch (Exception e) {
                System.out.println("An error occured: " + e.getMessage());
            }
        }

        return -1;
    }
}