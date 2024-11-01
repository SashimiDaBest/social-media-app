import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;
import java.util.Random;

public class Chat implements ChatInterface {
    private String chatID;
    private ArrayList<Message> messages;
    private String recipientID;

    // Chat constructor for reading from file
    public Chat(String filename, boolean isRead) {
        if (isRead) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

                // Initialize constant data; chatID and recipientID are always the first 2 lines of a Chat data file.
                String line = reader.readLine();
                this.chatID = line;
                line = reader.readLine();
                this.recipientID = line;
                line = reader.readLine();
                this.messages = new ArrayList<>();

                // Add each message to this Chat's ArrayList of Messages.
                while (line != null) {
                    String[] messageComponents = line.split(":");
                    messages.add(new Message(messageComponents[1], messageComponents[0]));

                    line = reader.readLine();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Chat constructor for initially creating a new Chat.
    public Chat(String recipientID) {
        // Create this Chat with a unique ID and write it a data file.
        this.recipientID = recipientID;
        this.messages = new ArrayList<>();
        this.chatID = makeUniqueID();
        writeData();
    }

    // Add the new message and update the data file.
    public void addMessage(Message message) {
        messages.add(message);
        writeData();
    }

    // Delete the matching message and update the file.
    // In the future: should probably be fixed to know which specific message to delete in the case of duplicates
    public void deleteMessage(Message message) {
        for (Message m : messages) {
            if (m.equals(message)) {
                messages.remove(m);
                break;
            }
        }
        writeData();
    }

    public void writeData() {
        // Create or write to the data file matching this Chat.
        File chatData = new File(this.chatID + "_chat.txt");
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatData))) {

            // Add the chatID and recipientID as the first two lines in the data file.
            writer.println(this.chatID);
            writer.println(this.recipientID);

            // Add all Messages to the data file.
            for (Message message : messages) {
                writer.println(message.getAuthorID() + ":" + message.getMessage());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeUniqueID() {
        // Navigate to chat database directory (right now it is just the project directory)
        String directory = System.getProperty("user.dir");
        File src = new File(directory, "src");
        File projectDir = src.getParentFile();

        // Keep track of what IDs are in use
        ArrayList<String> usedIDs = new ArrayList<>();
        Random rand = new Random();

        // Add each chat's ID to the list of used IDs
        File[] files = projectDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".txt")) {
                    usedIDs.add(file.getName().split("_")[0]);
                }
            }
        }

        while (true) {
            // Attempt a 4-digit ID, padding it with enough 0s to make it 4 digits long
            int idNumber = rand.nextInt(10000);
            String idAttempt = "0000".substring(0, 4 - Integer.toString(idNumber).length()) + idNumber;

            // Return the new, unique ID
            if (!usedIDs.contains(idAttempt)) {
                return idAttempt;
            }
        }
    }

    public static void main(String[] args) {
        // For making a new chat that isn't already stored in data:
        String exampleUserID = "5245";  // Sender (owner of this Chat)
        String exampleUser2ID = "9582"; // Recipient

        Chat exampleChat = new Chat(exampleUser2ID); // In actual functionality, this Chat would be placed in
        // the User with ID exampleUserID's ArrayList of chats.
        // Automatically creates a data file with unique ID for this Chat.

        Message exampleMessage = new Message("Hey 9582!", exampleUserID);
        exampleChat.addMessage(exampleMessage); // Automatically updates the data file for this Chat.

        // For reading a chat from data:
        Chat exampleChat2 = new Chat("9999_chat.txt", true); // Existing Chat data file
        String exampleUser3ID = "5782"; // Owner of this Chat object
        String exampleUser4ID = "1258"; // Recipient of this Chat object


        Message exampleMessage2 = new Message("hey 5782!", exampleUser4ID);
        exampleChat2.deleteMessage(exampleMessage2); // Automatically updates the data file
    }
}