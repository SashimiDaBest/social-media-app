import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
/**
 * Social Media App - Chat Class
 *
 * Message class with accessors and mutators
 *
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

    /*
    read from [chatID].txt and reconstruct chat object
    check if chatID is in the right format
     */
    public Chat(String chatID) throws InvalidFileFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(chatID))) {

            String line = reader.readLine();
            this.chatID = line;

            try {
                Integer.parseInt(chatID.substring(2));
                if (line.length() != 6 || !line.startsWith("C_"))
                    throw new InvalidFileFormatException("Invalid chatID Format!");
            } catch (NumberFormatException e) {
                throw new InvalidFileFormatException("Invalid chatID Format!");
            }

            if (line != null) {
                line = reader.readLine();

                int count = 0;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ';') {
                        count++;
                    }
                }

                String[] members = line.split(";", count + 1);
                this.memberList = new ArrayList<>(Arrays.asList(members));
            } else {
                throw new InvalidFileFormatException("No Users Found in Chat");
            }

            this.messageList = new ArrayList<>();

            while (line != null) {
                line = reader.readLine();
                String[] member = line.split(";", 2);
                try {
                    this.memberList.add(member[0]);
                    this.messageList.add(new Message(member[0], Integer.parseInt(member[1].substring(0, 1)), member[1].substring(1)));
                } catch (Exception e) {
                    throw new InvalidFileFormatException("Invalid User Message in Chat");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Chat constructor for initially creating a new Chat.
    Create this Chat with a unique ID and write it a data file.
    Add this chat's ID to the list of chat IDs.
     */
    public Chat(ArrayList<String> messageList) {
        this.chatID = createChatID();
        this.memberList = messageList;
        this.messageList = new ArrayList<>();
        writeData();

        /*
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatIDListDoc, true))) {
            writer.println(this.chatID);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        */
        counter.set(counter.get() + 1);
    }

    /*
    Create or write to the data file matching this Chat.
    Add the chatID and recipientID as the first two lines in the data file.
    Add all Messages to the data file.
     */
    public void writeData() {
        File chatData = new File(this.chatID + ".txt");
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatData, false))) {

            writer.println(this.chatID);
            for (int i = 0; i < memberList.size(); i++) {
                writer.print(memberList.get(i));
                if(i != memberList.size() - 1)
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
    /*
    REVISE
    Get the current status of the counter.
    Pad the ID with 0s if its length is less than 4.
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

    public void addMessage(Message message) {
        messageList.add(message);
        writeData();
    }

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