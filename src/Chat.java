import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
/**
 * Chat Class
 * <p>
 * Represents a chat within a social media application, managing members, messages, and
 * file-based persistence. Provides methods for creating a new chat, reading from a file,
 * adding, editing, and deleting messages, and generating a unique chat ID.
 * <p>
 * Status: Complete
 * </p>
 * @author Connor Pugliese
 * @author Soleil Pham
 * @version 11/02/2024
 * @since 1.0
 */
public class Chat implements ChatInterface {
    /** The unique identifier for the chat. */
    private String chatID;
    /** The list of member IDs participating in this chat. */
    private ArrayList<String> memberList;
    /** The list of messages in this chat. */
    private ArrayList<Message> messageList;
    /** Counter to generate unique chat IDs. */
    private static AtomicInteger counter = new AtomicInteger(0);
    /** File to store a list of all chat IDs. */
    private static String chatIDListDoc = "chatIDList.txt";

    /**
     * Constructs a Chat object by reading data from an existing chat file.
     * <p>
     * Validates the format of the chatID and member IDs and populates the message list.
     * </p>
     *
     * @param chatID the ID of the chat to be reconstructed
     * @throws InvalidFileFormatException if the chat file format is invalid
     */
    public Chat(String chatID) throws InvalidFileFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(chatID + ".txt"))) {

            String line = reader.readLine();

            try {
                Integer.parseInt(chatID.substring(2));
                if (line.length() != 6 || !line.startsWith("C_"))
                    throw new InvalidFileFormatException("Invalid chatID Format!");
            } catch (NumberFormatException e) {
                throw new InvalidFileFormatException("Invalid chatID Format!");
            }

            this.chatID = line;

            if (line != null) {
                line = reader.readLine();

                int count = 0;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ';') {
                        count++;
                    }
                }

                String[] members = line.split(";", count + 1);

                for (String member : members) {
                    try {
                        Integer.parseInt(member.substring(2));
                        if (member.length() != 6 || !member.startsWith("U_"))
                            throw new InvalidFileFormatException("Invalid memberID Format!");
                    } catch (NumberFormatException e) {
                        throw new InvalidFileFormatException("Invalid memberID Format!");
                    }
                }

                this.memberList = new ArrayList<>(Arrays.asList(members));
            } else {
                throw new InvalidFileFormatException("No Users Found in Chat");
            }

            this.messageList = new ArrayList<>();

            line = reader.readLine();

            while (line != null) {
                String[] messageParts = line.split(";", 2);
                try {
                    this.messageList.add(new Message(messageParts[0], Integer.parseInt(messageParts[1].substring(0, 1)),
                            messageParts[1].substring(1)));
                } catch (Exception e) {
                    throw new InvalidFileFormatException("Invalid User Message in Chat");
                }

                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a new Chat object with a generated unique chat ID and the specified list of members.
     * Initializes an empty message list and writes data to a new file.
     *
     * @param memberList the list of member IDs for the new chat
     */
    public Chat(ArrayList<String> memberList) {
        this.chatID = createChatID();
        this.memberList = memberList;
        this.messageList = new ArrayList<>();
        writeData();

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatIDListDoc, true))) {
            writer.println(this.chatID);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        counter.set(0);
    }

    /**
     * Writes the chat data to a file, including chat ID, member list, and all messages.
     * <p>
     * The chat ID and member list are written as the first two lines, followed by each message.
     * </p>
     */
    public void writeData() {
        File chatData = new File(this.chatID + ".txt");
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatData, false))) {

            writer.println(this.chatID);
            for (int i = 0; i < memberList.size(); i++) {
                writer.print(memberList.get(i));
                if (i != memberList.size() - 1)
                    writer.print(";");
            }
            writer.println();

            for (Message message : messageList) {
                writer.println(message.getAuthorID() + ";" + message.getMessageType() + message.getMessage());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the unique chat ID of this chat.
     *
     * @return the chat ID
     */
    public String getChatID() {
        return chatID;
    }

    /**
     * Generates a unique chat ID based on the counter. Pads the ID with zeros if its length is less than four digits.
     *
     * @return a unique chat ID in the format "C_XXXX"
     */
    public String createChatID() {
        String id = "C_";

        try (BufferedReader reader = new BufferedReader(new FileReader(chatIDListDoc))) {
            String line = reader.readLine();
            while (line != null) {
                counter.incrementAndGet();

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String number = String.valueOf(counter.get());
        int length = number.length(); //4-1
        for (int i = 0; i < 4 - length; i++) {
            id += "0";
        }

        return id + number;
    }

    /**
     * Returns the list of member IDs participating in this chat.
     *
     * @return the member list
     */
    public ArrayList<String> getMemberList() {
        return memberList;
    }

    /**
     * Sets the list of member IDs for this chat.
     *
     * @param memberList the new list of member IDs
     */
    public void setMemberList(ArrayList<String> memberList) {
        this.memberList = memberList;
    }

    /**
     * Returns the list of messages in this chat.
     *
     * @return the message list
     */
    public ArrayList<Message> getMessageList() {
        return messageList;
    }

    /**
     * Adds a new message to the chat and updates the file data.
     *
     * @param message the message to be added
     */
    public void addMessage(Message message) {
        messageList.add(message);
        writeData();
    }

    /**
     * Edits the most recent message by the specified author. Updates the file if the edit is successful.
     *
     * @param messageText the new text for the message
     * @param authorID the ID of the author whose message will be edited
     */
    public void editMessage(String messageText, String authorID) {
        for (int i = messageList.size() - 1; i >= 0; i--) {
            if (messageList.get(i).getAuthorID().equals(authorID)) {
                boolean setSuccessful = messageList.get(i).setMessage(messageText);
                if (setSuccessful) {
                    writeData();
                    break;
                }
            }
        }
    }

    /**
     * Deletes the most recent message by the specified author and updates the file data.
     *
     * @param authorID the ID of the author whose message will be deleted
     */
    public void deleteMessage(String authorID) {
        for (int i = messageList.size() - 1; i >= 0; i--) {
            if (messageList.get(i).getAuthorID().equals(authorID)) {
                messageList.remove(i);
                break;
            }
        }
        writeData();
    }

    /**
     * Returns the current value of the counter used for generating unique chat IDs.
     *
     * @return the current counter value
     */
    public int getCounter() {
        return counter.get();
    }

    /**
     * Determines if this chat is equal to another object based on the chat ID.
     *
     * @param obj the object to compare
     * @return {@code true} if the object is a Chat with the same chat ID, otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Chat chat = (Chat) obj;
        return this.chatID.equals(((Chat) obj).chatID);
    }
}