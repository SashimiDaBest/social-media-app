import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * Social Media App - Chat Class
 * <p>
 * Message class with accessors and mutators
 * <p>
 * Status: Complete
 *
 * @author connor pugliese, soleil pham
 * @version 11/02/2024
 */
public class Chat implements ChatInterface {
    private String chatID;
    private ArrayList<String> memberList;
    private ArrayList<Message> messageList;
    private static AtomicInteger counter = new AtomicInteger(0);
    private static String chatIDListDoc = "chatIDList.txt";

    /**
     * This constructor instantiates a new Chat object by reading data from [chatID].txt.
     * @param chatID The ID of the chat with an existing data file to be read.
     * @throws InvalidFileFormatException if contents of the file are corrupt.
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
     * This constructor instantiates a new, empty Chat object by taking a list of members as input. A data file is
     * created for this new Chat, and the static ID counter is incremented.
     * @param messageList the list of members that this Chat will have.
     */
    public Chat(ArrayList<String> messageList) {
        this.chatID = createChatID();
        this.memberList = messageList;
        this.messageList = new ArrayList<>();
        writeData();

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatIDListDoc, true))) {
            writer.println(this.chatID);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        counter.set(counter.get() + 1);
    }

    /**
     Create or write to the data file matching this Chat.
     Add the chatID and recipientID as the first two lines in the data file.
     Add all Messages as subsequent lines in the data file.
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

    public String getChatID() {
        return chatID;
    }

    /**
     * This method reads from the list of currently used chat IDs, incrementing by one for each
     * existing ID. The result is that the first created ID will be C_0000, next will be C_0001, etc.
     * @return A unique ID that is incremented from the last ID that was generated.
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

    public ArrayList<String> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<String> memberList) {
        this.memberList = memberList;
    }

    public ArrayList<Message> getMessageList() {
        return messageList;
    }

    /**
     * Add the required message to the Messages of the ArrayList and write the new data to the Chat's data file.
     * @param message The Message to be added.
     */
    public void addMessage(Message message) {
        messageList.add(message);
        writeData();
    }

    /**
     * Replace the most recent message by the given author with the given String. Data is rewritten after the message
     * has been edited.
     * @param messageText The message to be written in place of the existing message.
     * @param authorID The ID of the user for whom a message will be edited.
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
     * Delete the most recent message by the given author and rewrite data.
     * @param authorID The ID of the user for whom a message will be deleted.
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

    public int getCounter() {
        return counter.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Chat chat = (Chat) obj;
        return this.chatID.equals(((Chat) obj).chatID);
    }
}
