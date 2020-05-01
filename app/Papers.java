public class Papers {
    private int id; // Not null
    private String title;
    private String text;
    private String citation;

    public Papers() {
    }

    public Papers(int id, String title, String text, String citation) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.citation = citation;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCitation() {
        return this.citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", text='" + getText() + "'" +
            ", citation='" + getCitation() + "'" +
            "}";
    }
}