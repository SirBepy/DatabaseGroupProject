public class Authorship {
    private int facultyId; // Not null
    private int paperId; // Not null

    public Authorship() {
    }

    public Authorship(int facultyId, int paperId) {
        this.facultyId = facultyId;
        this.paperId = paperId;
    }

    public int getFacultyId() {
        return this.facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getPaperId() {
        return this.paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    @Override
    public String toString() {
        return "{" +
            " facultyId='" + getFacultyId() + "'" +
            ", paperId='" + getPaperId() + "'" +
            "}";
    }
}