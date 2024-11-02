import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.*;
import java.util.ArrayList;

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

        if(outputFile.exists())
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

        assertEquals("Chat does not properly instantiate Messages from file.", messages, testChat.getMessageList());

        if(chatID.exists())
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

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(testCorruptFile))) {
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

        if(testCorruptFile.exists())
            testCorruptFile.delete();
    }

    public static void main(String[] args) {
        RunChatTests test = new RunChatTests();
        test.testNoReadConstructor();
        test.testReadConstructor();
    }
}
