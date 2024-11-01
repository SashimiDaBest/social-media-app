/**
 * Social Media App - Message Class
 *
 * Message class with accessors and mutators
 *
 * Status: Complete
 *
 * @author connor pugliese, soleil pham
 *
 * @version 11/01/2024
 *
 */
public class Message {

    private String messageID;
    private String message;
    private String authorID;
    private int type;

    public Message(String authorID, int type, String message) {
        this.message = message;
        this.authorID = authorID;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthorID() {
        return authorID;
    }

    public int getType() {
        return type;
    }
}