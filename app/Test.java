public class Test {
    
    public static void main(String[] args) {

        // First user is sjz@it.rit.edu
        // His password is 5f47859188a602594556580532e814a3
        DBService dbService = new DBService();
        new GUI(dbService);
    }
    
}