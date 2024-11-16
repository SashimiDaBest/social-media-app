import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * User Class
 * <p>
 * Represents a user within the social media application. It stores user
 * information such as username, user ID, profile picture, followers,
 * following, blocked users, and associated chats. Provides methods for
 * managing user data, such as adding followers, creating chats, and
 * updating profile information.
 *
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
    /** Path to the user ID list file */
    private static final String USERIDLIST = "UserIDList.txt";
    /** Lock object for thread-safe operations */
    private static final Object LOCK = new Object();
    /** Counter for generating unique user IDs */
    private static AtomicInteger counter = new AtomicInteger(0);
    /** The user's username */
    private String userName;
    /** The user's unique ID */
    private String userID;
    /** Path to the user's profile picture */
    private String photoPathway;
    /** List of followers' user IDs */
    private ArrayList<String> followerList;
    /** List of following users' IDs */
    private ArrayList<String> followingList;
    /** List of blocked users' IDs */
    private ArrayList<String> blockedList;
    /** List of chat IDs associated with the user */
    private ArrayList<String> chatIDList;
    /** The user's account type (e.g., regular or admin) */
    private int accountType;
    /** The user's password */
    private String password;

    /**
     * Constructs a User object by loading user data from a file.
     *
     * @param userIdinfo The path to the file containing user information
     */

    private final String SAMPLE_FOLDER = "Sample Test Folder/";

    public User(String userIdinfo) {
        synchronized (LOCK) {
            try (BufferedReader br = new BufferedReader(new FileReader(userIdinfo))) {
                String line1 = "";
                line1 = br.readLine();
                this.userID = line1.split(";")[0];
                this.password = line1.split(";")[1];
                this.userName = br.readLine();
                this.photoPathway = br.readLine();
                this.accountType = Integer.parseInt(br.readLine());
                this.followerList = new ArrayList<>();
                this.followingList = new ArrayList<>();
                this.blockedList = new ArrayList<>();
                this.chatIDList = new ArrayList<>();
                String followers = br.readLine();
                String[] followersArray = followers.split(";");
                for (String user : followersArray) {
                    followerList.add(user);
                }
                String following = br.readLine();
                String[] followingArray = following.split(";");
                for (String user : followingArray) {
                    followingList.add(user);
                }
                String blocking = br.readLine();
                String[] blockingArray = blocking.split(";");
                for (String user : blockingArray) {
                    blockedList.add(user);
                }
                String chatting = br.readLine();
                String[] chattingArray = chatting.split(";");
                for (String user : chattingArray) {
                    chatIDList.add(user);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Constructs a User object and initializes it with a username and password.
     *
     * @param userName The username of the user
     * @param password The password of the user
     * @throws InvalidCreateAccountException If the password is invalid
     */
    public User(String userName, String password) throws InvalidCreateAccountException {

        this.userName = userName;

        boolean haveLetter = false;
        boolean haveNumber = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
                haveLetter = true;
            }
            if (Character.isDigit(password.charAt(i))) {
                haveNumber = true;
            }
        }

        if (password == null || password.length() < 10 || (!haveLetter && !haveNumber)) {
            throw new InvalidCreateAccountException("Invalid password");
        }

        this.password = password;
        this.userID = createUserID();
        this.accountType = 0;
        this.photoPathway = null;
        followerList = new ArrayList<String>();
        followingList = new ArrayList<String>();
        blockedList = new ArrayList<String>();
        chatIDList = new ArrayList<String>();

        try (PrintWriter pw = new PrintWriter(new FileWriter(SAMPLE_FOLDER + this.userID + ".txt"))) {
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

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getUsername() {
        return this.userName;
    }

    public String getUserID() {
        return this.userID;
    }

    public synchronized void setUserID(String id) {
        this.userID = id;
    }

    public String createUserID() {
        String id = "U_";
        synchronized (LOCK) {
            try (BufferedReader reader = new BufferedReader(new FileReader(USERIDLIST))) {
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
    }

    public synchronized String getProfilePic() {
        return this.photoPathway;
    }

    public synchronized void setProfilePic(String newPhotoPathway) {
        this.photoPathway = newPhotoPathway;
        writeData();
    }

    public ArrayList<String> getFollowerList() {
        return followerList;
    }

    public synchronized boolean deleteFollower(String followerID) {
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

    public synchronized void writeData() {
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

    public synchronized boolean addFollower(String followerID) {
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

    public ArrayList<String> getFollowingList() {
        return followingList;
    }

    public synchronized boolean addFollowing(String followingID) {
        if (findUser(followingID) && !followingList.contains(followingID) && !blockedList.contains(followingID)) {
            followingList.add(followingID);
            writeData();
            return true;
        }
        return false;
    }

    public synchronized boolean deleteFollowing(String followingID) {
        if (followingList.contains(followingID)) {
            followingList.remove(followingID);
            writeData();
            return true;
        }
        return false;
    }

    public ArrayList<String> getBlockedList() {
        return blockedList;
    }

    public synchronized boolean addBlock(String blockedID) {
        if (findUser(blockedID) && !blockedList.contains(blockedID)) {
            blockedList.add(blockedID);
            writeData();
            return true;
        }
        return false;
    }

    public synchronized boolean deleteBlock(String blockedID) {
        if (blockedList.contains(blockedID)) {
            blockedList.remove(blockedID);
            writeData();
            return true;
        }
        return false;
    }

    public ArrayList<String> getChatIDList() {
        return chatIDList;
    }

    public synchronized boolean addChat(String chatID) {

        if (!chatIDList.contains(chatID)) {
            chatIDList.add(chatID);
            writeData();
            return true;
        }

        return false;

    }

    public synchronized void createChat(ArrayList<String> recipientID) {
        Chat newChat = new Chat(recipientID);
        chatIDList.add(newChat.getChatID());
        writeData();
    }

    public synchronized boolean deleteChat(String chatID) {
        chatIDList.remove(chatID);
        writeData();
        return false;
    }

    public synchronized int getAccountType() {
        return this.accountType;
    }

    public synchronized void setAccountType(int accountType) {
        this.accountType = accountType;
        writeData();
    }

    public synchronized String getPassWord() {
        return password;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
        writeData();
    }

    public synchronized boolean findUser(String userIDToSearch) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERIDLIST))) {
            String line = br.readLine();
            while (line != null) {
                if (line.substring(line.length() - 6).equals(userIDToSearch)) {
                    return true;
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static synchronized String findIDFromUsername(String usernameToSearch) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERIDLIST))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.split(";")[0].equals(usernameToSearch)) {
                    return line.split(";")[2];
                }

                line = reader.readLine();
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized String findUsernameFromID(String idToSearch) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERIDLIST))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.split(";")[2].equals(idToSearch)) {
                    return line.split(";")[0];
                }

                line = reader.readLine();
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean sendText(String chatID, String message, int type, String senderID, String username, int userType)
            throws NoChatFoundException {
        if (chatIDList.contains(chatID)) {
            Chat existingChat = null;
            try {
                existingChat = new Chat(chatID);
            } catch (InvalidFileFormatException e) {
                e.printStackTrace();
            }
            Message intendedMessage = new Message(senderID, type, message);
            existingChat.addMessage(intendedMessage);
            return true;
        }
        throw new NoChatFoundException("No chat found");
    }

    public static synchronized boolean hasLogin(String username, String passwordToCheck) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERIDLIST))) {
            String userIterator = "";
            while ((userIterator = br.readLine()) != null) {
                String userN = userIterator.split(";")[0];
                String passW = userIterator.split(";")[1];
                if (username.equals(userN) && passwordToCheck.equals(passW)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized String checkChatAbility(User userToChatWith) {
        if (this.getBlockedList().contains(userToChatWith.getUserID()) ||
                userToChatWith.getBlockedList().contains(this.userID)) {
            return "false";
        }
        if (this.userID.equals(userToChatWith.userID)) {
            return "self";
        }
        if (userToChatWith.getAccountType() == 0) {
            return "true";
        } else {
            return String.valueOf(userToChatWith.getFollowerList().contains(this.userID));
        }
    }

    public static synchronized boolean userNameValidation(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERIDLIST))) {
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

    public synchronized void createNewUser(String username, String newUserPassword, String userIDparameter) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERIDLIST, true))) {
            pw.println(username + ";" + newUserPassword + ";" + userIDparameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}