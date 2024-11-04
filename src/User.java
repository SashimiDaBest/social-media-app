import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
/**
 * User Class
 * <p>
 * Represents a user within the social media application. Stores user information such as username,
 * user ID, profile picture, followers, following, blocked users, and associated chats.
 * Provides methods for managing user data, such as adding followers, creating chats, and updating profile info.
 * <p>
 * Status: Complete
 * </p>
 * @author Soleil Pham
 * @author Connor Pugliese
 * @author Venkat Mamidi
 * @author Derek McTume
 * @version 11/02/2024
 * @since 1.0
 */
public class User implements UserInterface {
    /** The username of the user. */
    private String userName;
    /** The unique user ID. */
    private String userID;
    /** The file pathway to the user's profile picture. */
    private String photoPathway;
    /** List of follower IDs. */
    private ArrayList<String> followerList;
    /** List of IDs that the user is following. */
    private ArrayList<String> followingList;
    /** List of blocked user IDs. */
    private ArrayList<String> blockedList;
    /** List of chat IDs associated with the user. */
    private ArrayList<String> chatIDList;
    /** The type of account (e.g., user type or permissions level). */
    private int accountType;
    /** The user's password. */
    private String password;
    /** Counter for generating unique user IDs. */
    private static AtomicInteger counter = new AtomicInteger(0);
    /** File pathway to the user data file. */
    private final String userIDinfo = this.userID + ".txt";
    /** Pathway to the file listing all user IDs. */
    private static final String userIDList = "UserIDList.txt";

    /**
     * Constructs a User by reading user data from a file.
     *
     * @param userIdinfo the file pathway containing user data
     */
    public User(String userIdinfo) {
        try (BufferedReader br = new BufferedReader(new FileReader(userIdinfo))) {
            String line1 = "";
            line1 = br.readLine();
            this.userID = line1.split(";")[0];
            this.password = line1.split(";")[1];
            this.userName = br.readLine();
            this.photoPathway = br.readLine();
            this.accountType = Integer.parseInt(br.readLine());
            String followers = br.readLine();
            String followersArray[] = followers.split(";");
            for (String user : followersArray) {
                followerList.add(user);
            }
            String following = br.readLine();
            String followingArray[] = following.split(";");
            for (String user : followingArray) {
                followingList.add(user);
            }
            String blocking = br.readLine();
            String blockingArray[] = blocking.split(";");
            for (String user : blockingArray) {
                blockedList.add(user);
            }
            String chatting = br.readLine();
            String chattingArray[] = chatting.split(";");
            for (String user : chattingArray) {
                chatIDList.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a new User with the specified username and password.
     * Initializes a new unique user ID and creates a new data file for the user.
     *
     * @param userName the username of the new user
     * @param password the password of the new user
     */
    public User(String userName, String password) {

        this.userName = userName;
        this.password = password;
        this.userID = createUserID();
        this.accountType = 0;
        this.photoPathway = null;
        followerList = new ArrayList<String>();
        followingList = new ArrayList<String>();
        blockedList = new ArrayList<String>();
        chatIDList = new ArrayList<String>();

        try (PrintWriter pw = new PrintWriter(new FileWriter(this.userID + ".txt"))) {
            pw.println(this.userID + ";" + this.userName);
            pw.println(this.userName);
            pw.println(this.photoPathway);
            pw.println(this.accountType);
            pw.println("");
            pw.println("");
            pw.println("");
            pw.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        counter.set(0);
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.userName = username;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username as a {@code String}
     */
    public String getUsername() {
        return this.userName;
    }

    /**
     * Retrieves the unique user ID.
     *
     * @return the user ID as a {@code String}
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * Generates a new unique user ID.
     *
     * @return the generated user ID as a {@code String}
     */
    public String createUserID() {
        String id = "U_";

        try (BufferedReader reader = new BufferedReader(new FileReader(userIDList))) {
            String line = reader.readLine();
            while (line != null) {
                counter.incrementAndGet();

                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String number = String.valueOf(counter.get());
        int length = number.length();
        for (int i = 0; i < 4 - length; i++) {
            id += "0";
        }
        return id + number;
    }

    /**
     * Sets the file pathway to the user's profile picture and updates data storage.
     *
     * @param photoPathway the file pathway to the profile picture
     */
    public void setProfilePic(String photoPathway) {
        this.photoPathway = photoPathway;
        writeData();
    }

    /**
     * Retrieves the profile picture pathway.
     *
     * @return the profile picture pathway as a {@code String}
     */
    public String getProfilePic() {
        return this.photoPathway;
    }

    /**
     * Retrieves the list of follower IDs.
     *
     * @return an {@code ArrayList} of follower IDs
     */
    public ArrayList<String> getFollowerList() {
        return followerList;
    }

    /**
     * Removes a follower by their ID and updates data storage.
     *
     * @param followerID the ID of the follower to be removed
     * @return {@code true} if the follower was successfully removed, {@code false} otherwise
     */
    public boolean deleteFollower(String followerID) {
        if (findUser(followerID)) {
            for (int i = 0; i < followerList.size(); i++) {
                if (followerList.get(i).equals(followerID)) {
                    followerList.remove(i);
                    writeData();
                    return true;

                }
            }
        }
        return false;
    }


    /**
     * Saves the user's data to a file named with the user ID.
     * <p>
     * Writes the user's ID, password, username, profile picture pathway, account type,
     * follower list, following list, blocked list, and chat ID list to the file. Each section
     * is written in a specific format, with lists separated by semicolons.
     * <p>
     * This method ensures that any changes to the user's information are persisted in the file system.
     * </p>
     */
    public void writeData() {
        try (PrintWriter pr = new PrintWriter(new FileWriter(this.getUserID() + ".txt"))) {
            pr.println(this.userID + ";" + this.password);
            pr.println(this.userName);
            pr.println(this.photoPathway);
            pr.println(this.accountType);
            for (int i = 0; i < followerList.size(); i++) {
                if (i != followerList.size() - 1) {
                    pr.print(followerList.get(i) + ";");
                } else {
                    pr.println(followerList.get(i));
                }
            }
            for (int i = 0; i < followingList.size(); i++) {
                if (i != followingList.size() - 1) {
                    pr.print(followingList.get(i) + ";");
                } else {
                    pr.println(followingList.get(i));
                }
            }
            for (int i = 0; i < blockedList.size(); i++) {
                if (i != blockedList.size() - 1) {
                    pr.print(blockedList.get(i) + ";");
                } else {
                    pr.println(blockedList.get(i));
                }
            }
            for (int i = 0; i < chatIDList.size(); i++) {
                if (i != chatIDList.size() - 1) {
                    pr.print(chatIDList.get(i) + ";");
                } else {
                    pr.println(chatIDList.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new follower by their ID and updates data storage.
     *
     * @param followerID the ID of the follower to be added
     * @return {@code true} if the follower was successfully added, {@code false} otherwise
     */
    public boolean addFollower(String followerID) {
        if (findUser(followerID)) {
            for (int i = 0; i < followerList.size(); i++) {
                if (followerList.get(i).equals(followerID)) {
                    writeData();
                    return true;
                }
            }
        } else {
            return false;
        }
        followerList.add(followerID);
        writeData();
        return true;
    }

    /**
     * Retrieves the list of IDs that the user is following.
     *
     * @return an {@code ArrayList} of following IDs
     */
    public ArrayList<String> getFollowingList() {
        return followingList;
    }

    /**
     * Adds a new following user by their ID and updates data storage.
     *
     * @param followingID the ID of the user to follow
     * @return {@code true} if the user was successfully followed, {@code false} otherwise
     */
    public boolean addFollowing(String followingID) {
        if (findUser(followingID) && !followingList.contains(followingID) && !blockedList.contains(followingID)) {
            followingList.add(followingID);
            writeData();
            return true;
        }
        return false;
    }

    /**
     * Removes a following user by their ID and updates data storage.
     *
     * @param followingID the ID of the user to unfollow
     * @return {@code true} if the user was successfully removed from following, {@code false} otherwise
     */
    public boolean deleteFollowing(String followingID) {
        if (followingList.contains(followingID)) {
            followingList.remove(followingID);
            writeData();
            return true;
        }
        return false;
    }

    /**
     * Retrieves the list of blocked user IDs.
     *
     * @return an {@code ArrayList} of blocked user IDs
     */
    public ArrayList<String> getBlockedList() {
        return blockedList;
    }

    /**
     * Blocks a user by their ID and updates data storage.
     *
     * @param blockedID the ID of the user to block
     * @return {@code true} if the user was successfully blocked, {@code false} otherwise
     */
    public boolean addBlock(String blockedID) {
        if (findUser(blockedID) && !blockedList.contains(blockedID)) {
            blockedList.add(blockedID);
            writeData();
            return true;
        }
        return false;
    }

    /**
     * Unblocks a user by their ID and updates data storage.
     *
     * @param blockedID the ID of the user to unblock
     * @return {@code true} if the user was successfully unblocked, {@code false} otherwise
     */
    public boolean deleteBlock(String blockedID) {
        if (blockedList.contains(blockedID)) {
            blockedList.remove(blockedID);
            writeData();
            return true;
        }
        return false;
    }

    /**
     * Retrieves the list of chat IDs associated with the user.
     *
     * @return an {@code ArrayList} of chat IDs
     */
    public ArrayList<String> getChatIDList() {
        return chatIDList;
    }

    /**
     * Adds a chat by its ID to the user's chat list and updates data storage.
     *
     * @param chat_id the ID of the chat to add
     * @return {@code true} if the chat was successfully added, {@code false} otherwise
     */
    public boolean addChat(String chat_id) {
        chatIDList.add(chat_id);
        writeData();
        return true;
    }

    /**
     * Creates a new chat with the specified recipient IDs and updates data storage.
     *
     * @param recipient_id the list of recipient IDs for the new chat
     */
    public void createChat(ArrayList<String> recipient_id) {
        Chat newChat = new Chat(recipient_id);
        chatIDList.add(newChat.getChatID());
        writeData();
    }

    /**
     * Deletes a chat by its ID from the user's chat list and updates data storage.
     *
     * @param chat_id the ID of the chat to delete
     * @return {@code true} if the chat was successfully deleted, {@code false} otherwise
     */
    public boolean deleteChat(String chat_id) {
        chatIDList.remove(chat_id);
        writeData();
        return false;
    }

    /**
     * Retrieves the user's account type.
     *
     * @return the account type as an integer
     */
    public int getAccountType() {
        return this.accountType;
    }

    /**
     * Sets the user's account type and updates data storage.
     *
     * @param accountType the new account type
     */
    public void setAccountType(int accountType) {
        this.accountType = accountType;
        writeData();
    }

    /**
     * Retrieves the user's password.
     *
     * @return the password as a {@code String}
     */
    public String getPassWord() {
        return password;
    }

    /**
     * Sets the user's password and updates data storage.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
        writeData();
    }

    /**
     * Searches for a user by their ID within the application data.
     *
     * @param userID the ID of the user to find
     * @return {@code true} if the user is found, {@code false} otherwise
     */
    public boolean findUser(String userID) {
        try (BufferedReader br = new BufferedReader(new FileReader(userIDList))) {
            String line = br.readLine();
            while (line != null) {
                if (line.substring(line.length() - 6).equals(userID)) {
                    return true;
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Sends a message in a specified chat.
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
    public boolean sendText(String chatID, String message, int type, String userID, String username, int userType) throws NoChatFoundException {
        if (chatIDList.contains(chatID)) {
            Chat existingChat = null;
            try {
                existingChat = new Chat(chatID);
            } catch (InvalidFileFormatException e) {
                e.printStackTrace();
            }
            Message intendedMessage = new Message(userID, type, message);
            existingChat.addMessage(intendedMessage);
            return true;
        }
        throw new NoChatFoundException("No chat found");
    }

    /**
     * Verifies if the provided username and password match any user entry in the system.
     *
     * @param username the username to verify
     * @param password the password to verify
     * @return {@code true} if the username and password match an existing user, {@code false} otherwise
     */
    public boolean hasLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(userIDList))) {
            String userIterator = "";
            while ((userIterator = br.readLine()) != null) {
                String userN = userIterator.split(";")[0];
                String passW = userIterator.split(";")[1];
                if (username.equals(userN) && password.equals(passW)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if the specified username is available for a new user.
     *
     * @param username the username to validate
     * @return {@code true} if the username is unique and available, {@code false} if it is already taken
     */
    public boolean userNameValidation(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(userIDList))) {
            String userIterator = "";
            while ((userIterator = br.readLine()) != null) {
                String userN = userIterator.split(";")[0];
                if (userN.equals(username)) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Creates a new user entry by saving their username, password, and user ID to the user ID list.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param userIDparameter the user ID for the new user
     */
    public void createNewUser(String username, String password, String userIDparameter) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(userIDList, true))) {
            pw.println(username + ";" + password + ";" + userIDparameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the user ID for the user.
     *
     * @param id the new user ID to be set
     */
    public void setUserID(String id) {
        this.userID = id;
    }
}
