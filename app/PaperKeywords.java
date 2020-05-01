public class PaperKeywords {
    private int id; // Not null
    private String keyword; // Not null

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        if (keyword != null)
            this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", keyword='" + getKeyword() + "'" + "}";
    }
}
