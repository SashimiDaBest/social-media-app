package object;
import exception.InvalidFileFormatException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Chat Class
 * <p>
 * Represents a chat within a social media application, managing members, messages, and
 * file-based persistence. Provides methods for creating a new chat, reading from a file,
 * adding, editing, and deleting messages, and generating a unique chat ID.
 * </p>
 *
 * @author Connor Pugliese
 * @author Soleil Pham
 * @version 11/02/2024
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

    private final String sampleFolder = "SampleTestFolder/";

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
        synchronized (Chat.class) {
            try (BufferedReader reader = new BufferedReader(new FileReader(chatID + ".txt"))) {

                String line = reader.readLine();

                try {
                    Integer.parseInt(line.substring(2));
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
                        this.messageList.add(new Message(messageParts[0],
                                Integer.parseInt(messageParts[1].substring(0, 1)),
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
        synchronized (Chat.class) {

            // overwrite whole file in order to update chatIDList
            String previousChatFileContent = "";
            try (BufferedReader reader = new BufferedReader(new FileReader(new File(chatIDListDoc)))) {
                
                String line = reader.readLine();
                while(line != null) {
                    previousChatFileContent += line + "\n";
                    line = reader.readLine();
                }
                previousChatFileContent = previousChatFileContent.substring(0, previousChatFileContent.length() - 1);
                System.out.println("After reading chatIDList: " + previousChatFileContent);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatIDListDoc, false))) {
                // writer.println(this.chatID);
                previousChatFileContent += "\n" + this.chatID;
                System.out.println("About to write to chatIDList: " + previousChatFileContent);

                writer.println(previousChatFileContent);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            counter.set(0);
        }
    }

    /**
     * Writes the chat data to a file, including chat ID, member list, and all messages.
     * <p>
     * The chat ID and member list are written as the first two lines, followed by each message.
     * </p>
     */
    public synchronized void writeData() {
        File chatData = new File(sampleFolder + this.chatID + ".txt");
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
    public synchronized String getChatID() {
        return chatID;
    }

    /**
     * Generates a unique chat ID based on the counter. Pads the ID with zeros if its length is less than four digits.
     *
     * @return a unique chat ID in the format "C_XXXX"
     */
    public synchronized String createChatID() {
        String id = "C_";
        counter.incrementAndGet();
        synchronized (Chat.class) {
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
            int length = number.length();
            for (int i = 0; i < 4 - length; i++) {
                id += "0";
            }
        }
        return id + counter.get();
    }

    /**
     * Returns the list of member IDs participating in this chat.
     *
     * @return the member list
     */
    public synchronized ArrayList<String> getMemberList() {
        return memberList;
    }

    /**
     * Sets the list of member IDs for this chat.
     *
     * @param memberList the new list of member IDs
     */
    public synchronized void setMemberList(ArrayList<String> memberList) {
        this.memberList = memberList;
    }

    /**
     * Returns the list of messages in this chat.
     *
     * @return the message list
     */
    public synchronized ArrayList<Message> getMessageList() {
        return messageList;
    }

    /**
     * Adds a new message to the chat and updates the file data.
     *
     * @param message the message to be added
     */
    public synchronized void addMessage(Message message) {
        messageList.add(message);
        writeData();
    }

    /**
     * Edits the most recent message by the specified author. Updates the file if the edit is successful.
     *
     * @param messageText the new text for the message
     * @param authorID    the ID of the author whose message will be edited
     */
    public synchronized void editMessage(String messageText, String authorID) {
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
    public synchronized void deleteMessage(String authorID) {
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
