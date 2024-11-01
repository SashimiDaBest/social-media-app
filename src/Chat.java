import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * Social Media App - Message Class
 * <p>
 * Message class with accessors and mutators
 * <p>
 * Status: Complete
 *
 * @author connor pugliese, soleil pham
 * @version 11/01/2024
 */
public class Chat implements ChatInterface {
    private String chatID;
    private ArrayList<Message> messages;
    private ArrayList<String> recipientID;
    private static AtomicInteger counter = new AtomicInteger(0);

    // Chat constructor for reading from file. If any input does not match the expected format, throw an error.
    public Chat(String filename) throws InvalidFileFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            this.messages = new ArrayList<>();

            // Instantiate this chat's ID as the first line in the file.
            String line = reader.readLine();

            // Validate the chatID.
            if (line.length() != 6 || !line.startsWith("C_"))
                throw new InvalidFileFormatException("Invalid chatID Format!");
            try {
                Integer.parseInt(line.substring(2));
            } catch (NumberFormatException e) {
                throw new InvalidFileFormatException("Invalid chatID Format!");
            }

            this.chatID = line;

            while (line != null) {
                line = reader.readLine();

                // Instantiate the ArrayList of recipientIDs as the second line in the file;
                if (line.charAt(0) == 'U') {
                    String[] recipientIDs = line.split(";");

                    for (String recipientID : recipientIDs) {

                        if (recipientID.length() != 6 || !recipientID.startsWith("U_"))
                            throw new InvalidFileFormatException("Invalid recipientID Format!");
                        try {
                            Integer.parseInt(recipientID.substring(2));
                        } catch (NumberFormatException e) {
                            throw new InvalidFileFormatException("Invalid recipientID Format!");
                        }
                    }

                    this.recipientID = new ArrayList<>(Arrays.asList(recipientIDs));
                    continue;
                }

                // Instantiate each Message in the file to the Messages ArrayList.
                String senderID = line.substring(6);
                int messageType = Integer.parseInt(line.substring(5, 6));
                String messageContent = line.substring(7);
                messages.add(new Message(senderID, messageType, messageContent));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Chat constructor for initially creating a new Chat.
    public Chat(ArrayList<String> recipientID) {
        // Create this Chat with a unique ID and write it a data file.
        File chatIDList = new File("chatIDList.txt");
        this.recipientID = recipientID;
        this.messages = new ArrayList<>();
        this.chatID = makeUniqueID();
        writeData();

        // Add this chat's ID to the list of chat IDs.
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatIDList, true))) {
            writer.println(this.chatID);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        File chatData = new File(this.chatID + ".txt");
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatData, false))) {

            // Add the chatID and recipientID as the first two lines in the data file.
            writer.println(this.chatID);
            for (int i = 0; i < recipientID.size(); i++) {
                writer.print(recipientID.get(i) + ";");
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

        // Get the current status of the counter.
        File chatIDList = new File("chatIDList.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(chatIDList))) {
            String line = reader.readLine();
            while (line != null) {
                counter.incrementAndGet();

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pad the ID with 0s if its length is less than 4.
        String number = String.valueOf(counter.get());
        int length = number.length(); //4-1
        for (int i = 0; i < 4 - length; i++) {
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

    public int getCounter() {
        return counter.get();
    }

    public void setRecipientID(ArrayList<String> recipientID) {
        this.recipientID = recipientID;
    }

    public static void main(String[] args) {

    }
}