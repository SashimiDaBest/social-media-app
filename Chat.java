import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class Chat {
    private String chatID;
    private ArrayList<String> messages;
    private User user1;
    private User user2;
    private static ArrayList<Chat> activeChats = new ArrayList<>();

    public Chat(User user1, User user2) {
        File chatData = new File("chatData.txt");
        ArrayList<String> usedIDs = new ArrayList<>();
        Random rand = new Random();

        // Initialize new chat with an empty array list of messages
        this.messages = new ArrayList<String>();
        this.user1 = user1;
        this.user2 = user2;

        try (BufferedReader reader = new BufferedReader(new FileReader(chatData));
             PrintWriter writer = new PrintWriter(new FileOutputStream(chatData, true))) {

            // Read and store all the currently used IDs
            String line = reader.readLine();
            while (line != null) {
                String[] chatComponents = line.split(",");
                usedIDs.add(chatComponents[0]);
                line = reader.readLine();
            }

            // Assign a unique ID to this chat
            while (true) {
                int idNumber = rand.nextInt(10000);

                // Pad the ID with 0s if it is less than 4 digits long
                String idAttempt = "0000".substring(0, 4 - Integer.toString(idNumber).length()) + idNumber;

                // Add the new, unique ID. If it is not unique, try again
                if (!usedIDs.contains(idAttempt)) {
                    this.chatID = idAttempt;
                    break;
                }
            }

            // Add new chat to the data sheet and static list of chats
            writer.println(this);
            activeChats.add(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Chat(String chatID, User user1, User user2, ArrayList<String> messages) {
        // Make a new chat with known information for reading from data sheet
        this.chatID = chatID;
        this.user1 = user1;
        this.user2 = user2;
        this.messages = messages;
    }

    public ArrayList<String> getMessages() {
        return this.messages;
    }

    public String getChatID() {
        return this.chatID;
    }

    public ArrayList<Chat> getActiveChats() {
        return activeChats;
    }

    public String toString() {
        String output = this.chatID + "," + this.user1.getUsername() + "," + this.user2.getUsername();

        for (String message : this.messages) {
            output += "," + message;
        }

        return output;
    }

    public static void writeMessage(String chatID, User author, String message) {
        String newMessage = author.getUsername() + ":" + message;

        // Identify the chat that a message will be written to
        Chat chatToModify = null;
        for(Chat chat : activeChats) {
            if(chat.getChatID().equals(chatID)) {
                chatToModify = chat;
            }
        }

        // Add the new message to the chat's list of messages
        chatToModify.messages.add(newMessage);

        File chatData = new File("chatData.txt");
        ArrayList<String> chats = new ArrayList<>();

        // Keep track of which line in the chat data sheet needs to be updated
        String lineToModify = "";

        PrintWriter writer = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(chatData))) {

            String line = reader.readLine();
            while (line != null) {
                chats.add(line);
                String[] chatComponents = line.split(",");

                // Identify the line in the chat data sheet to be updated
                if (chatComponents[0].equals(chatID)) {
                    lineToModify = line;
                }

                line = reader.readLine();
            }

            // Update the line
            chats.set(chats.indexOf(lineToModify), lineToModify + "," + newMessage);

            writer = new PrintWriter(new FileOutputStream(chatData));

            // Re print the chat data sheet with the updated line
            for (String chat : chats) {
                writer.println(chat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /* readData() and the rest of this class currently uses an example User class that only has one field (username).
    In the future, once the User class has been completed, it will use methods from said class to access the actual
    users from the User database instead of making new ones with matching usernames.
     */
    public static void readData() {
        File chatData = new File("chatData.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(chatData))) {

            // Add a new chat to the static ArrayList of active chats by constructing it from the data sheet
            String line = reader.readLine();
            while (line != null) {
                String[] chatComponents = line.split(",");
                String chatID = chatComponents[0];
                User user1 = new User(chatComponents[1]);
                User user2 = new User(chatComponents[2]);
                ArrayList<String> messages = new ArrayList<>();
                for (int i = 3; i < chatComponents.length; i++) {
                    messages.add(chatComponents[i]);
                }

                activeChats.add(new Chat(chatID, user1, user2, messages));

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readData();
        User angela = new User("Angela");
        User michael = new User("Michael");
        Chat newChat = new Chat(angela, michael);
        writeMessage(newChat.getChatID(), angela, "Hello Michael!");

        // writeMessage can be used to add a new message to any existing chat in chatData. Example:
        writeMessage("1283", new User("angela"), "i am speaking with michael");
        writeMessage("3599", new User("dean"), "hru");
    }
}
