import java.util.ArrayList;

public class ModifiedPapers {
    private int id; // Not null
    private String title;
    private String text;
    private String citation;
    private ArrayList<String> authorship;
    private ArrayList<String> keywords; // Not null

    public ModifiedPapers(int id, String title, String text, String citation) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.citation = citation;
        authorship = new ArrayList<String>();
        keywords = new ArrayList<String>();
    }

    public ModifiedPapers(String title, String text, String citation) {
        this(0, title, text, citation);
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
        return listToString(keywords);
    }

    public ArrayList<String> getKeywordsList() {
        return keywords;
    }

    public void addAuthor(String author) {
        authorship.add(author);
    }

    public String getAuthorship() {
        return listToString(authorship);
    }

    private String listToString(ArrayList<String> list) {
        String toReturn = "";

        for (String str : list) {
            toReturn += str + ", ";
        }

        try {
            return toReturn.substring(0, toReturn.length() - 2);
        } catch (Exception e) {
            return toReturn;
        }
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