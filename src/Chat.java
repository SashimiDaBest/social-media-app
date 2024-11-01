import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
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
public class Chat implements ChatInterface {
    private String chatID;
    private ArrayList<Message> messages;
    private ArrayList<String> recipientID;
    private static AtomicInteger counter = new AtomicInteger(0);;

    // Chat constructor for reading from file
    public Chat(String filename, boolean alrCreated) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            this.messages = new ArrayList<>();

            String line = reader.readLine();
            this.chatID = line;

            while (line != null) {
                line = reader.readLine();
                if (line.charAt(0) == 'U') {
                    //get an array list of user ids
                    continue;
                }
                String senderID = line.substring(6);
                int messageType = Integer.parseInt(line.substring(5,6));
                String messageContent = line.substring(7);
                messages.add(new Message(senderID, messageType, messageContent));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Chat constructor for initially creating a new Chat.
    public Chat(ArrayList<String> recipientID) {
        // Create this Chat with a unique ID and write it a data file.
        this.recipientID = recipientID;
        this.messages = new ArrayList<>();
        this.chatID = makeUniqueID();
        writeData();
        counter.set(counter.get() + 1);
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
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(this.chatID + ".txt", false))) {

            // Add the chatID and recipientID as the first two lines in the data file.
            writer.println(this.chatID);
            for (int i = 0; i < recipientID.size(); i++) {
                writer.print(recipientID.get(i));
            }
            writer.println();

            // Add all Messages to the data file.
            for (Message message : messages) {
                writer.println(message.getAuthorID() + ";" + message.getType() + message.getMessage());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String makeUniqueID() {
        String id = "C_";
        String number = String.valueOf(counter.get());
        int length = number.length(); //4-1
        for (int i = 0; i < 4 - counter.get(); i++) {
            id += "0";
        }
        return id + number;
    }


    public String getChatID() {
        return chatID;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<String> getRecipientID() {
        return recipientID;
    }
    public int getCounter(){
        return counter.get();
    }
    public ArrayList<String> setRecipientID(ArrayList<String> recipientID) {
        this.recipientID = recipientID;
    }
}