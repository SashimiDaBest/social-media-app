import java.util.*;
/**
 * Chat Interface
 * <p>
 * Defines the essential behaviors for a chat within a social media application, including methods
 * for managing chat data, members, and messages. Implementing classes should provide mechanisms for
 * data persistence and unique chat ID generation.
 * <p>
 * Status: Complete
 * </p>
 * @author Soleil Pham
 * @version 11/02/2024
 * @since 1.0
 */
public interface ChatInterface {
    /**
     * Writes the chat data to a persistent storage. Implementations should specify the format and
     * structure of the data.
     */
    public void writeData();

    /**
     * Retrieves the unique chat ID.
     *
     * @return the chat ID as a {@code String}
     */
    public String getChatID();

    /**
     * Generates a unique chat ID. The format and mechanism for ID generation are implementation-specific.
     *
     * @return a newly generated unique chat ID as a {@code String}
     */
    public String createChatID();

    /**
     * Retrieves the list of member IDs participating in the chat.
     *
     * @return an {@code ArrayList} of member IDs
     */
    public ArrayList<String> getMemberList();

    /**
     * Sets the list of member IDs for the chat.
     *
     * @param memberList the {@code ArrayList} of member IDs to be set
     */
    public void setMemberList(ArrayList<String> memberList);

    /**
     * Retrieves the list of messages in the chat.
     *
     * @return an {@code ArrayList} of {@code Message} objects representing the messages in the chat
     */
    public ArrayList<Message> getMessageList();

    /**
     * Adds a new message to the chat.
     *
     * @param message the {@code Message} object to be added to the chat
     */
    public void addMessage(Message message);

    /**
     * Edits the most recent message authored by the specified user. The message to be edited is identified
     * by the author's ID.
     *
     * @param messageText the new text for the message
     * @param authorID the ID of the author whose message will be edited
     */
    public void editMessage(String messageText, String authorID);

    /**
     * Deletes the most recent message authored by the specified user.
     *
     * @param authorID the ID of the author whose message will be deleted
     */
    public void deleteMessage(String authorID);

    /**
     * Retrieves the current counter value used for generating unique chat IDs.
     *
     * @return the counter value as an {@code int}
     */
    public int getCounter();
}
