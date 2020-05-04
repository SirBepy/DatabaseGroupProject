public class SpeakingRequest {
    private int id;
    private int receiverId;
    private int senderId;
    private String senderName;
    private String senderEmail;
    private String title;
    private String description;

    public SpeakingRequest() {
    }

    public SpeakingRequest(int id, int receiverId, int senderId, String senderName, String senderEmail, String title, String description) {
        this.id = id;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.title = title;
        this.description = description;
    }

    public SpeakingRequest(int senderId, String title, String description) {
        this(-1, -1, senderId, null, null, title, description);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return this.senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.senderName;
    }

    public void setName(String senderName) {
        this.senderName = senderName;
    }

    public String getEmail() {
        return this.senderEmail;
    }

    public void setEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", receiverId='" + getReceiverId() + "'" +
            ", senderId='" + getSenderId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}