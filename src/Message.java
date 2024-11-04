/**
 * Message Class
 * <p>
 * Represents a message within a chat, with information about the author, type, and content.
 * Supports both text messages and image pathways, with accessors and mutators to manage
 * message details. Allows editing of messages if the message type is text.
 * <p>
 * Status: Complete
 * </p>
 *
 * @author Connor Pugliese
 * @author Soleil Pham
 * @version 11/02/2024
 * @since 1.0
 */
public class Message {
    /**
     * The ID of the message's author.
     */
    private String authorID;
    /**
     * The type of message (0 for text, other values for image pathways).
     */
    private int messageType;
    /**
     * The content of the message, which may be text or an image pathway.
     */
    private String message;

    /**
     * Constructs a new {@code Message} with the specified author ID, message type, and content.
     *
     * @param authorID    the ID of the author of the message
     * @param messageType the type of the message (0 for text, non-zero for image pathway)
     * @param message     the content of the message
     */
    public Message(String authorID, int messageType, String message) {
        this.authorID = authorID;
        this.messageType = messageType;
        this.message = message;
    }

    /**
     * Retrieves the ID of the message's author.
     *
     * @return the author ID as a {@code String}
     */
    public String getAuthorID() {
        return authorID;
    }

    /**
     * Retrieves the type of the message.
     *
     * @return an integer representing the message type (0 for text)
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * Retrieves the content of the message.
     *
     * @return the message content as a {@code String}
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets a new text content for the message if the message type is text (0).
     *
     * @param messageText the new content for the message
     * @return {@code true} if the message was successfully updated, {@code false} if the message type is not text
     */
    public boolean setMessage(String messageText) {
        if (this.messageType == 0) {
            this.message = messageText;
            return true;
        }
        return false;
    }

    /**
     * Compares this {@code Message} to another object for equality based on author ID, message type, and content.
     *
     * @param obj the object to compare to this message
     * @return {@code true} if the object is a {@code Message} with the same author ID, message type, and content;
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Message messageToCompare = (Message) obj;
        return this.message.equals(messageToCompare.message) && this.authorID.equals(messageToCompare.authorID) &&
            this.messageType == messageToCompare.messageType;
    }
}