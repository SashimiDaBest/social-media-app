import java.util.*;
/**
 * Message Interface
 * <p>
 * Defines the essential behaviors for a message within a social media application.
 * Provides methods for accessing and modifying message properties such as the author ID,
 * message type, and content.
 * <p>
 * Status: Complete
 * </p>
 * @author Soleil Pham
 * @version 11/02/2024
 * @since 1.0
 */
public interface MessageInterface {
    /**
     * Retrieves the ID of the message's author.
     *
     * @return the author ID as a {@code String}
     */
    public String getAuthorID();

    /**
     * Retrieves the type of the message.
     *
     * @return an integer representing the message type (0 for text, other values for different types)
     */
    public int getMessageType();

    /**
     * Retrieves the content of the message.
     *
     * @return the message content as a {@code String}
     */
    public String getMessage();

    /**
     * Sets a new text content for the message if the message type allows it.
     *
     * @param messageText the new content for the message
     * @return {@code true} if the message was successfully updated, {@code false} if updating is not allowed
     */
    public boolean setMessage(String messageText);

}
