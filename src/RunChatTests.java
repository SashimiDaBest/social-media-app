import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.*;
import java.util.ArrayList;

public class RunChatTests {
    @Test
    public void testNoReadConstructor() {
        String senderID = "U_0001";
        String recipientID = "U_0002";
        Message testMessage1 = new Message(senderID, 0, "hey bud!");
        Message testMessage2 = new Message(recipientID, 0, "hey hru!");

        ArrayList<String> recipientIDs = new ArrayList<>();
        recipientIDs.add(recipientID);
        Chat testChat = new Chat(recipientIDs);
        File outputFile = new File(testChat.getChatID() + ".txt");
        testChat.addMessage(testMessage1);
        testChat.addMessage(testMessage2);
        ArrayList<String> dataFileContents = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line = reader.readLine();
            while (line != null) {
                dataFileContents.add(line);

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> expectedFileContents = new ArrayList<>();
        expectedFileContents.add(testChat.getChatID());
        expectedFileContents.add(recipientID);
        expectedFileContents.add(senderID + ";0hey bud!");
        expectedFileContents.add(recipientID + ";0hey hru!");

        assertEquals("Created chat data file does not match expected contents.", expectedFileContents,
                dataFileContents);

        if(outputFile.exists())
            outputFile.delete();
    }

    @Test
    public void testReadConstructor() {
        File chatData = new File("chat1234_test.txt");
        Chat testChat = null;

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatData))) {
            writer.println("C_1234");
            writer.println("U_0003;U_0004");
            writer.println("U_0003;0hey guys");
            writer.println("U_0005;0what's up");
            writer.println("U_0004;0what going on");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            testChat = new Chat(chatData.getName());
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
        }

        assertEquals("Chat does not properly instantiate chat ID from file.", testChat.getChatID(), "C_1234");

        ArrayList<String> expectedRecipientIDs = new ArrayList<>();
        expectedRecipientIDs.add("U_0003");
        expectedRecipientIDs.add("U_0004");

        assertEquals("Chat does not properly instantiate recipient IDs from file.", testChat.getRecipientID(),
                expectedRecipientIDs);

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("U_0003", 0, "hey guys"));
        messages.add(new Message("U_0005", 0, "what's up"));
        messages.add(new Message("U_0004", 0, "what going on"));

        assertEquals("Chat does not properly instantiate Messages from file.", messages, testChat.getMessages());

        if(chatData.exists())
            chatData.delete();

        File testCorruptFile = new File("corruptChat1234_test.txt");

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(testCorruptFile))) {
            writer.println("Chat_1234");
            writer.println("U_0003;U_0004");
            writer.println("U_0003;0hey guys");
            writer.println("U_0005;0what's up");
            writer.println("U_0004;0what going on");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows("Chat does not properly catch invalid chatIDs when instantiating from file.",
                InvalidFileFormatException.class, () -> new Chat(testCorruptFile.getName()));

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(testCorruptFile))) {
            writer.println("C_1234");
            writer.println("X_0003;U_0004");
            writer.println("U_0003;0hey guys");
            writer.println("U_0005;0what's up");
            writer.println("U_0004;0what going on");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows("Chat does not properly catch invalid userIDs when instantiating from file.",
                InvalidFileFormatException.class, () -> new Chat(testCorruptFile.getName()));

        if(testCorruptFile.exists())
            testCorruptFile.delete();
    }

    public static void main(String[] args) {
        RunChatTests test = new RunChatTests();
        test.testReadConstructor();
    }
}
