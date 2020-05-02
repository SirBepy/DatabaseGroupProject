import java.util.ArrayList;

public class PapersWithKeywords {
    private int id; // Not null
    private String title;
    private String text;
    private String citation;
    private ArrayList<String> keywords; // Not null

    public PapersWithKeywords(int id, String title, String text, String citation) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.citation = citation;
        keywords = new ArrayList<String>();
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

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    public String getKeywords() {
        String toReturn = "";

        for(String keyword : keywords) {
            toReturn += keyword + ", ";
        }

        return toReturn.substring(0, toReturn.length() - 2);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", title='" + getTitle() + "'" +
                ", text='" + getText() + "'" +
                ", citation='" + getCitation() + "'" +
                ", keywords='" + getKeywords() + "'" +
                "}";
    }
}