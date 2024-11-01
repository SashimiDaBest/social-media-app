public class Message {
    private String messageID;
    private String message;
    private String authorID;
    private boolean isImage;

    public Message(String message, String authorID) {
        this.message = message;
        this.authorID = authorID;
        this.isImage = false;
    }

    public Message(String message, String authorID, boolean isImage) {
        this.message = message;
        this.authorID = authorID;
        this.isImage = isImage;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthorID() {
        return authorID;
    }

    public boolean equals(Message message) {
        return this.message.equals(message.message) && this.authorID.equals(message.authorID);
    }
}