import java.util.ArrayList;
/**
 * User Interface
 * <p>
 * Defines the essential behaviors for a user within a social media application, including methods for managing
 * user attributes such as username, profile picture, followers, following, blocked users, chats, and authentication.
 * This interface provides a foundation for user-related functionality in the application.
 * <p>
 * Status: Complete
 * </p>
 * @author Soleil Pham
 * @author Connor Pugliese
 * @version 11/01/2024
 * @since 1.0
 */
public interface UserInterface {


    /**
     * Sets the username of the user.
     *
     * @param username the new username
     */
    public void setUsername(String username);

    /**
     * Retrieves the username of the user.
     *
     * @return the username as a {@code String}
     */
    public String getUsername();

    /**
     * Retrieves the unique user ID.
     *
     * @return the user ID as a {@code String}
     */
    public String getUserID();

    /**
     * Generates a unique user ID.
     *
     * @return the generated user ID as a {@code String}
     */
    public String createUserID();

    /**
     * Sets the profile picture of the user.
     *
     * @param photoPathway the file pathway to the profile picture
     */
    public void setProfilePic(String photoPathway);

    /**
     * Retrieves the profile picture pathway of the user.
     *
     * @return the profile picture pathway as a {@code String}
     */
    public String getProfilePic();

    /**
     * Retrieves the list of follower IDs.
     *
     * @return an {@code ArrayList} of follower IDs
     */
    public ArrayList<String> getFollowerList();

    /**
     * Removes a follower by their ID.
     *
     * @param followerId the ID of the follower to be removed
     * @return {@code true} if the follower was successfully removed, {@code false} otherwise
     */
    public boolean deleteFollower(String followerId);

    /**
     * Adds a new follower by their ID.
     *
     * @param followerID the ID of the follower to be added
     * @return {@code true} if the follower was successfully added, {@code false} otherwise
     */
    public boolean addFollower(String followerID);

    /**
     * Retrieves the list of IDs the user is following.
     *
     * @return an {@code ArrayList} of following IDs
     */
    public ArrayList<String> getFollowingList();

    /**
     * Removes a following user by their ID.
     *
     * @param followingId the ID of the user to stop following
     * @return {@code true} if the user was successfully removed from following, {@code false} otherwise
     */
    public boolean deleteFollowing(String followingId);

    /**
     * Adds a new user to the following list by their ID.
     *
     * @param followingID the ID of the user to follow
     * @return {@code true} if the user was successfully added to following, {@code false} otherwise
     */
    public boolean addFollowing(String followingID);

    /**
     * Retrieves the list of blocked user IDs.
     *
     * @return an {@code ArrayList} of blocked user IDs
     */
    public ArrayList<String> getBlockedList();

    /**
     * Unblocks a user by their ID.
     *
     * @param blockedID the ID of the user to unblock
     * @return {@code true} if the user was successfully unblocked, {@code false} otherwise
     */
    public boolean deleteBlock(String blockedID);

    /**
     * Blocks a user by their ID.
     *
     * @param blockedID the ID of the user to block
     * @return {@code true} if the user was successfully blocked, {@code false} otherwise
     */
    public boolean addBlock(String blockedID);

    /**
     * Retrieves the list of chat IDs associated with the user.
     *
     * @return an {@code ArrayList} of chat IDs
     */
    public ArrayList<String> getChatIDList();

    /**
     * Adds a chat by its ID.
     *
     * @param chatId the ID of the chat to add
     * @return {@code true} if the chat was successfully added, {@code false} otherwise
     */
    public boolean addChat(String chatId);

    /**
     * Creates a new chat with the specified recipient IDs.
     *
     * @param recipientId the list of recipient IDs for the new chat
     */
    public void createChat(ArrayList<String> recipientId);

    /**
     * Deletes a chat by its ID.
     *
     * @param chatId the ID of the chat to delete
     * @return {@code true} if the chat was successfully deleted, {@code false} otherwise
     */
    public boolean deleteChat(String chatId);

    /**
     * Retrieves the account type of the user.
     *
     * @return the account type as an integer
     */
    public int getAccountType();

    /**
     * Sets the account type of the user.
     *
     * @param accountType the new account type
     */
    public void setAccountType(int accountType);

    /**
     * Retrieves the password of the user.
     *
     * @return the password as a {@code String}
     */
    public String getPassWord();

    /**
     * Sets the password of the user.
     *
     * @param password the new password
     */
    public void setPassword(String password);

    /**
     * Finds a user by their ID.
     *
     * @param userID the ID of the user to find
     * @return {@code true} if the user is found, {@code false} otherwise
     */
    public boolean findUser(String userID);

    /**
     * Sends a message in a chat.
     *
     * @param chatID the ID of the chat to send the message to
     * @param message the message content
     * @param type the message type (0 for text)
     * @param userID the ID of the user sending the message
     * @param username the username of the user sending the message
     * @param userType the type of the user sending the message
     * @return {@code true} if the message was successfully sent, {@code false} otherwise
     * @throws NoChatFoundException if the specified chat ID is not found
     */
    public boolean sendText(String chatID, String message, int type,
                            String userID, String username, int userType) throws NoChatFoundException;

    /**
     * Verifies if the provided username and password match the user's credentials.
     *
     * @param username the username to verify
     * @param password the password to verify
     * @return {@code true} if login is successful, {@code false} otherwise
     */
    public static boolean hasLogin(String username, String password) {
        return false;
    };

    /**
     * Creates a new user with the specified username, password, and user ID.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param userIDparameter the user ID for the new user
     */
    public void createNewUser(String username, String password, String userIDparameter);

    /**
     * Validates the specified username.
     *
     * @param username the username to validate
     * @return {@code true} if the username is valid, {@code false} otherwise
     */
    public static boolean userNameValidation(String username) {
        return false;
    };

}