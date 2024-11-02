/**
 * Social Media App - Message Class
 *
 * Message class with accessors and mutators
 * 1. constructor
 * a. message can be image pathway or text
 * 2. getAuthorID(), getMessageType(), getMessage()
 * 3. setMessage(String messageText)
 * a. if message is a string, then message can be edited
 * 4. equals(Object obj)
 *
 * Status: Complete
 *
 * @author connor pugliese, soleil pham
 *
 * @version 11/02/2024
 *
 */
public class Message {

    private String authorID;
    private int messageType;
    private String message;

    public Message(String authorID, int messageType, String message) {
        this.authorID = authorID;
        this.messageType = messageType;
        this.message = message;
    }

    public String getAuthorID() {
        return authorID;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public boolean setMessage(String messageText) {
        if (this.messageType == 0) {
            this.message = messageText;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Message message = (Message) obj;
        return this.message.equals(message.message) && this.authorID.equals(message.authorID) && this.messageType == message.messageType;
    }
}