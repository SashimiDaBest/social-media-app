import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.*;
import java.util.ArrayList;

/**
 * Chat Tests
 * <p>
 * Test class to test the Chat class
 * <p>
 * Status: Complete
 *
 * @author Derek Mctume
 * @author Connor Pugliese
 * @version 11/02/2024
 */
public class RunChatTests {
    @Test
    public void testNoReadConstructor() {
        String member1ID = "U_0001";
        String member2ID = "U_0002";
        Message testMessage1 = new Message(member1ID, 0, "hey bud!");
        Message testMessage2 = new Message(member2ID, 0, "hey hru!");

        ArrayList<String> memberIDs = new ArrayList<>();
        memberIDs.add(member1ID);
        memberIDs.add(member2ID);
        Chat testChat = new Chat(memberIDs);
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
        expectedFileContents.add(member1ID + ";" + member2ID);
        expectedFileContents.add(member1ID + ";0hey bud!");
        expectedFileContents.add(member2ID + ";0hey hru!");

        assertEquals("Created chat data file does not match expected contents.", expectedFileContents,
                dataFileContents);

        if (outputFile.exists())
            outputFile.delete();
    }

    @Test
    public void testReadConstructor() {
        File chatID = new File("C_1234.txt");
        Chat testChat = null;

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(chatID))) {
            writer.println("C_1234");
            writer.println("U_0003;U_0004;U_0005");
            writer.println("U_0003;0hey guys");
            writer.println("U_0005;0what's up");
            writer.println("U_0004;0what going on");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            testChat = new Chat("C_1234");
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
        }

        assertEquals("Chat does not properly instantiate chat ID from file.", testChat.getChatID(), "C_1234");

        ArrayList<String> expectedMemberIDs = new ArrayList<>();
        expectedMemberIDs.add("U_0003");
        expectedMemberIDs.add("U_0004");
        expectedMemberIDs.add("U_0005");

        assertEquals("Chat does not properly instantiate member IDs from file.", testChat.getMemberList(),
                expectedMemberIDs);

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("U_0003", 0, "hey guys"));
        messages.add(new Message("U_0005", 0, "what's up"));
        messages.add(new Message("U_0004", 0, "what going on"));

        assertEquals("Chat does not properly instantiate Messages from file.",
                messages, testChat.getMessageList());

        if (chatID.exists())
            chatID.delete();

        File testCorruptFile = new File("C_1234.txt");

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
                InvalidFileFormatException.class, () -> new Chat("C_1234"));

        try (PrintWriter writer = new PrintWriter(
                new FileOutputStream(testCorruptFile))) {
            writer.println("C_1234");
            writer.println("X_0003;U_0004;U_0005");
            writer.println("U_0003;0hey guys");
            writer.println("U_0005;0what's up");
            writer.println("U_0004;0what going on");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows("Chat does not properly catch invalid userIDs when instantiating from file.",
                InvalidFileFormatException.class, () -> new Chat("C_1234"));

        if (testCorruptFile.exists())
            testCorruptFile.delete();
    }

    @Test
    public void testAddMessage() {

        Chat testChat;
        try {
            testChat = new Chat("This works");

        } catch (Exception e) {
            throw new RuntimeException("testAddMessage: Chat construction failed, failing test");
        }

        Message testMessage = new Message("U_0001", 0, "Lol, this works");
        testChat.addMessage(testMessage);

        // make sure that the message sent is in messageList
        boolean messageInMessages = testChat.getMessageList().contains(testMessage);
        assertEquals("testAddMessage: New message was not found in message list!", true, messageInMessages);

        // New message should be detected in file after writeData()

        // Parse chatData
        ArrayList<String> fileData = new ArrayList<>();
        try (BufferedReader bReader = new BufferedReader(new FileReader(new File(testChat.getChatID())))) {

            String line = bReader.readLine();
            while (line != null) {
                fileData.add(line);
                line = bReader.readLine();
            }


        } catch (IOException e) {

            throw new RuntimeException("testAddMessage: Reading from chatData should not throw an error");
        }

        // Check if the chatId at the top of the file matches with the actual chatId
        String chatIdFromFile = fileData.get(0);
        assertEquals("testAddMessage: ChatID written to file does not match!", testChat.getChatID(), chatIdFromFile);


        // Now check the messages and make sure the new message is included
        String[] testMessageInfo = fileData.get(2).split(";");
        String userIDFromFile = testMessageInfo[0];
        String messageFromFile = testMessageInfo[1].substring(1); // ignore message type

        assertEquals("testAddMessage; The author's ID does not match" +
                "up with what is on-file", testMessage.getAuthorID(), userIDFromFile);
        assertEquals("testAddMessage; The message's contents do not " +
                "match up with what is on-file", testMessage.getMessage(), messageFromFile);

        try {   // also test whether message type is being written properly
            int messageTypeFromFile = Integer.parseInt(testMessageInfo[1].substring(0, 1));
            assertEquals("testAddMessage; The message's type does not " +
                    "match up with what is on-file", testMessage.getMessageType(), messageTypeFromFile);

        } catch (NumberFormatException e) {
            throw new RuntimeException("testAddMessage: The message's type " +
                    "could not converted, failing test!");
        }

    }

    // requires testAddMessage() to work
    @Test
    public void testDeleteMessage() {

        // Should delete the message based if its author's ID matches the argument
        String testID = "authorID";
        Message testMessage1 = new Message(testID, 0, "This works");
        Chat testChat1;

        try {
            testChat1 = new Chat("some chat ID");
        } catch (Exception e) {
            throw new RuntimeException("testDeleteMessage: Chat construction should not throw any errors");
        }

        testChat1.addMessage(testMessage1);
        testChat1.deleteMessage(testMessage1.getAuthorID());

        boolean result = testChat1.getMessageList().contains(testMessage1);
        assertEquals("testDeleteMessage: The messengerList should no longer" +
                "contain the message instance!", true, result);

        // Check if writeData() accurately represents the changes
        ArrayList<String> fileData = new ArrayList<>();
        try (BufferedReader bReader = new BufferedReader(new FileReader(new File(testChat1.getChatID())))) {

            String line = bReader.readLine();
            while (line != null) {
                fileData.add(line);
                line = bReader.readLine();
            }


        } catch (IOException e) {

            throw new RuntimeException("testAddMessage: Reading from chatData should not throw an error");
        }

        // Check if the chatId at the top of the file matches with the actual chatId
        String chatIdFromFile = fileData.get(0);
        assertEquals("testAddMessage: ChatID written to file does not match!", testChat1.getChatID(), chatIdFromFile);

        // The added message shouldn't be there anymore, so there should only be two lines in the entire file
        assertEquals("testDeleteMessage: if message is added and then" +
                " subsequently deleted, then the file should " +
                "only have two lines! (empty message list)", 2, fileData.size());
    }

    @Test
    public void testEditMessage() {
        ArrayList<String> memberIDs = new ArrayList<>();
        memberIDs.add("U_0001");
        memberIDs.add("U_0002");
        memberIDs.add("U_0003");
        Message testMessage1 = new Message("U_0001", 0, "hey guys");
        Message testMessage2 = new Message("U_0002", 0, "what's up");

        Chat testChat = new Chat(memberIDs);
        testChat.addMessage(testMessage1);
        testChat.addMessage(testMessage2);

        testChat.editMessage("this is a new message", "U_0001");
        assertEquals("editMessage method does not properly edit the most recent message by the selected author.",
                "this is a new message", testChat.getMessageList().get(0).getMessage());
    }
}