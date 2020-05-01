public class Faculty {
    private int id; // Not null
    private String fName;
    private String lName;
    private String password;
    private String email;

    public Faculty() {
    }

    public Faculty(int id, String fName, String lName, String password, String email) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFName() {
        return this.fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return this.lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", fName='" + getFName() + "'" +
            ", lName='" + getLName() + "'" +
            ", password='" + getPassword() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}