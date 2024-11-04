import org.junit. Test;
import org.junit. Ignore;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

/**
 * Social Media App - User Tests
 * <p>
 * JUnit tests for Message Class
 * <p>
 * Status: In Progress
 *
 * @author Derek Mctume
 * @version 11/03/2024
 */

// NOTE: creating a User object also creates a new file
// In order to avoid unpredictable behaviors, this file is deleted before each
// asserts statement
public class UserTest {
  
    // Second constructor test:
    @Test 
    public void testSecondUserConstructor() {

        String username = "username";
        String password = "password";
        User testUser = new User(username, password);

        boolean result1 = testUser.getUsername().equals(username);
        boolean result2 = testUser.getPassWord().equals(password);

        assertEquals("testSecondUserConstructor: assigning username does not work",
            true, result1);
        assertEquals("testSecondUserConstructor: assigning password does not work",
            true, result2);

        // for fields created as null, 0, or empty
        boolean result3 = (testUser.getAccountType() == 0);
        assertEquals("testSecondUserConstructor: account type was not constructed as 0",
            true, result3);

        boolean result4 = (testUser.getProfilePic() == null);
        assertEquals("testSecondUserConstructor: profile pic was not concructed as null",
            true, result4);

        boolean result5 = testUser.getFollowerList().isEmpty() &&
            testUser.getFollowingList().isEmpty() &&
            testUser.getBlockedList().isEmpty() &&
            testUser.getChatIDList().isEmpty();
        
        assertEquals("testSecondUserConstructor: one or more of the array lists" + 
        "was not constucted as empty",
            true, result5);

        // now check the file was created
        File testUserFile = new File(testUser.getUserID() + ".txt");
        ArrayList<String> fileContents = new ArrayList<>();

        try(BufferedReader bReader = new BufferedReader(new FileReader(testUserFile))) {

            String line = bReader.readLine();
            while (line != null) {
                fileContents.add(line);
                line = bReader.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("testSecondUserConstructor: error while trying to read the file" +
                "created after User construction!");
        }

        testUserFile.delete();

        boolean result6 = fileContents.get(0).equals(testUser.getUserID() + ";" 
            + testUser.getUsername() + "\n");
        assertEquals("testSecondUserConstructor: 1st line of new User file is wrong",
            true, result6);

        boolean result7 = fileContents.get(1).equals(testUser.getUsername() + "\n");
        assertEquals("testSecondUserConstructor: 2nd line of new User file is wrong",
            true, result7);

        boolean result8 = fileContents.get(2).equals(testUser.getProfilePic() + "\n");
        assertEquals("testSecondUserConstructor: 3rd line of new User file is wrong",
            true, result8);
    
        boolean result9 = fileContents.get(3).equals(testUser.getAccountType() + "\n");
        assertEquals("testSecondUserConstructor: 4th line of new User file is wrong",
        true, result9);
    
        boolean result10 = fileContents.get(4).equals("" + "\n") &&
        fileContents.get(5).equals("" + "\n") &&
        fileContents.get(6).equals("" + "\n") &&
        fileContents.get(7).equals("" + "\n");

        assertEquals("testSecondUserConstructor: the final 4 lines of" +
         "new User file are not just empty newlines", true, result10);
    }

    // Mutators:
    @Test
    public void testSetUsername() {

        String testName = "This totally works";
        User testUser = new User("","");
        testUser.setUsername(testName);

        String result = testUser.getUsername();
        File testFile = new File(testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetUsername: Setting username does not work",
            testName, result);
    }

    @Test
    public void testSetPassword() {

        String testPassword = "This totally works";
        User testUser = new User("","");
        testUser.setPassword(testPassword);
        
        String result = testUser.getPassWord();
        File testFile = new File(testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetPassword: Setting password does not work",
            testPassword, result);
    }

    @Test
    public void testSetProfilePic() {

        String testPic = "i_dont_know_what_image_format_to_use.png";
        User testUser = new User("","");
        testUser.setProfilePic(testPic);

        String result = testUser.getProfilePic();
        File testFile = new File(testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetProfilePic: Setting the profile pic does not work", 
            testPic, result);
    }

    @Test
    public void testSetAccountType() {

        int testType = 1;
        User testUser = new User("","");
        testUser.setAccountType(testType);

        int result = testUser.getAccountType();
        File testFile = new File(testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetAccountType: Setting the account type does not work",
            testType, result);
    }

    // this function is just for testing:
    @Test public void testSetUserID() {

        String testID = "random id here";
        User testUser = new User("","");
        testUser.setUserID(testID);
        
        String result = testUser.getUserID();
        File testFile = new File(testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetUserID: Setting the user ID does not work", 
            testID, result);
    }

    // All other methods


    // NOTE: Block methods DO NOT UPDATE UserArray as of 11/1/24
    @Test
    public void testAddBlock() {

        // Test 1: blocking an ID that has not already blocked should return true
        String testId1 = "BLOCK ME, PLEEEASE";
        User blocker = new User("","");

        blocker.createNewUser("some random username", 
            "some random password", testId1); // to be blocked

        boolean result1 = blocker.addBlock(testId1);

        File blockerFile1 = new File(blocker.getUserID() + ".txt");
        blockerFile1.delete();

        File totalUsersFile1 = new File("UserIDList.txt");
        totalUsersFile1.delete();

        assertEquals("testAddBlock: Blocking an ID that has not" +
            "already been blocked should return true",true, result1); 

        // Test 2: blocking a user not in UserIDList should return false
        String testId2 = "For second test";
        User blocker2 = new User("","");

        blocker2.createNewUser("", "", "wrong ID, man");

        boolean result2 = blocker2.addBlock(testId2);

        File blockerFile2 = new File(blocker2.getUserID() + ".txt");
        blockerFile2.delete();

        File totalUsersFile2 = new File("UserIDList.txt");
        totalUsersFile2.delete();
        
        assertEquals("testAddBlock: Blocking a user not in UserIDList should return false",
            false, result2);

        // Test 3: blocking a user that is already blocked should return false
        String testId3 = "For third test";
        User blocker3 = new User("","");

        blocker3.getBlockedList().add(testId3);

        blocker3.createNewUser("", "", testId3); // being blocked
        
        boolean result3 = blocker3.addBlock(testId3);

        File blockerFile3 = new File(blocker3.getUserID() + ".txt");
        blockerFile3.delete();

        File totalUsersFile3 = new File("UserIDList.txt");
        totalUsersFile3.delete();

        assertEquals("testAddBlock: Blocking a user that is already " +
            "blocked should return false",false, result3);
        
    }

    // Requires addBlock() to work
    @Test
    public void testDeleteBlock() {

        // Test 1: if the blocked ID is removed, then it should no longer
        // be in the UserArray (return true)

        String testId1 = "UNBLOCK ME, PLEEASE";

        User testUser1 = new User("","");
        testUser1.addBlock(testId1);
        boolean result1 = testUser1.deleteBlock(testId1);

        File testFile1 = new File(testUser1.getUserID() + ".txt");
        testFile1.delete();
        assertEquals("testDeleteBlock: if the blocked ID is removed," +
            " then is should no longer be in the UserArray",true, result1); 

        // Test 2: if the blocked ID is not in blocked_ids, then it should return false

        String testId2 = "this works";
        User testUser2 = new User("","");
        boolean result2 = testUser2.deleteBlock(testId2);

        File testFile2 = new File(testUser2.getUserID() + ".txt");
        testFile2.delete();
        assertEquals("testDeleteBlock: if the blocked ID is not in blockedIds," +
            " then it should return false",false, result2);

    }

    @Test
    public void testAddChat() {

        // Test 1: chat_ids should contain the chat_id after appending
        // funct should also return true

        String testChat1 = "Testing chat";
        User testUser1 = new User("","");
        boolean funcResult1 = testUser1.addChat(testChat1);
        boolean result1 = testUser1.getChatIDList().contains(testChat1);

        File testFile1 = new File(testUser1.getUserID() + ".txt");
        testFile1.delete();

        assertEquals("testAddChat: ChatIds should caintain " +
            "the chatId after appending",true, result1); 

        assertEquals("testAddChat: succesfully adding the chat" +
            " should make the function return true",true, funcResult1); 

        // Test 2: adding a chat_id that already exists should return false

        String testChat2 = "I'm so tired...zzzz";
        User testUser2 = new User("","");
        testUser2.getChatIDList().add(testChat2);

        boolean funcResult2 = testUser2.addChat(testChat2);
        boolean result2 = testUser2.getChatIDList().contains(testChat2); // the id should not be added

        File testFile2 = new File(testUser2.getUserID() + ".txt");
        testFile2.delete();

        assertEquals("testAddChat: Adding a chat that already" +
            " exists should return false",false, result2); 
            
        assertEquals("testAddChat: Adding a chat that already exists" +
            " should make the funtion return false", funcResult2);
    }

    @Test
    public void testCreateChat() {

        // Test1: Should create a new chat whose ID appears in chat_ids
        ArrayList<String> testID1 = new ArrayList<>(Arrays.asList("this", "works"));
        User testUser = new User("","");
        testUser.createChat(testID1);

        boolean result1 = false;
        Chat testChat1 = new Chat(testID1);
        for (String chatID: testUser.getChatIDList()) {
            if (chatID.equals(testChat1.getChatID())) {
                result1 = true;
            }
        }

        File testFile = new File(testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testCreateChat: A new chat should be created" +
            " and its ID should appear in Chat_Ids",true, result1); 
        
    }


    // requires createNewUser() to work
    @Test
    public void testHasLogin() {

        // Test 1: returns true if user's username and password is found in the userIDList file
        String testName1 = "username";
        String testPassword1 = "123thisWorks";

        User testUser1 = new User(testName1, testPassword1);
        File testUserIDFile = new File("UserIDList.txt");
        testUser1.writeData();
        boolean result1 = testUser1.hasLogin(testName1, testPassword1);
        testUserIDFile.delete();

        assertEquals("testHasLogin: UserIdList should contain" +
            " the file information after running writeData()",true, result1);
        

        // Test 2: returns false if user's username and password is not found in the userIDList file
        User testUser2 = new User("some other username", "some other password");
        File testUserIDFile2 = new File("UserIDList.txt");

        testUser2.writeData();
        boolean result2 = testUser2.hasLogin(testName1, testPassword1); // search for some other username or password
        testUserIDFile2.delete();

        assertEquals("testHasLogin: UserIdList shouldn't contain" +
            " a username and password that wasn't written to it",false, result2);
        

    }


    // requires getPassword() and setUseID to work
    @Test
    public void testCreateNewUser() {

        // Test 1: user info should appear on the userIDList after being written
        String testUsername = "testUsername";
        String testPassword = "testPassword";
        String testUserID = "testUserID";

        User testUser1 = new User(testUsername, testPassword);
        testUser1.setUserID(testUserID);

        testUser1.createNewUser(testUser1.getUsername(), testUser1.getPassWord(), testUser1.getUserID());
        
        String fileContents;
        File testFile = new File("UserIDList.txt"); // should already be created
        try (BufferedReader bReader = new BufferedReader(new FileReader(testFile))) {

            fileContents = bReader.readLine(); // there should only be one line

        } catch(IOException e) {
            throw new RuntimeException("testCreateNewUser: userIDFile was" +
                " not properly created; could not read!");
        }

        String[] parsedInfo = fileContents.split(";");
        String usernameFromFile = parsedInfo[0];
        String passwordFromFile = parsedInfo[1];
        String userIDFromFile = parsedInfo[2];

        boolean usernameChecksOut = usernameFromFile.equals(testUsername);
        boolean passwordChecksOut = passwordFromFile.equals(testPassword);
        boolean userIDChecksOut = userIDFromFile.equals(testUserID);

        testFile.delete();

        assertEquals("testCreateNewUser: the username returned from the file" +
            " was not the one passed to the user's constructor!",true, usernameChecksOut);

        assertEquals("testCreateNewUser: the password returned from" +
            " the file was not the one passed to the user's constructor!",true, passwordChecksOut);

        assertEquals("testCreateNewUser: the userID returned from" +
            " the file was passed to the user!",true, userIDChecksOut);

        
    }

    // requires addChat() to work
    @Test
    public void testSendText() {


        // Test 1: tests successfully sending a message to an existing chat
        User testSender = new User("sender", "senderPass");
        testSender.setUserID("testing userID");
        Chat testChat = new Chat(new ArrayList<>(Arrays.asList("Members", 
            "Don't matter", "In this test >:)")));
        String testChatID = testChat.getChatID();

        try {
            boolean result1 = testSender.sendText(testChatID, "SOME randoM MeZage", 0, testSender.getUserID(), testSender.getUsername(), 0);
            assertEquals("testSendText: Successful function call should return successful",true, result1);

            // check if the message was added
            ArrayList<Message> chatMessages = testChat.getMessageList();
            boolean result2 = false;
            for (Message message: chatMessages) {
                if (message.getMessage().equals("SOME randoM MeZage") &&
                message.getMessageType() == 0 &&
                message.getAuthorID().equals(testSender.getUserID())) {
                    result2 = true;
                }
            }

            File testFile = new File(testSender.getUserID() + ".txt");
            testFile.delete();
            assertEquals("testSendText: The Chat's messages doesn't contain the sent messages!",true, result2);

        } catch(NoChatFoundException e) {
            throw new RuntimeException("testSendText: sending a text to" +
                " an existing chat throws a NoChatFoundException!");
        
        }

        // Test 2: tests sending a message to a chat that doesn't exist
        User testSender2 = new User("sender", "senderPass");
        testSender2.setUserID("testing userID");
        String testChatID2 = "some random ID for a chat that doesn't exist";

        File testFile = new File(testSender2.getUserID() + ".txt");
        testFile.delete();
        assertThrows("testSendText: attempting to send a text to" +
        " a chat that exist should throw an NoChatFoundException",
            NoChatFoundException.class, 
            () -> {
            testSender.sendText(testChatID2, "SOME randoM MeZage", 0, 
                testSender2.getUserID(), testSender2.getUsername(), 0);
        });
    }

    
    public static void main(String[] args) {
        
        UserTest userTests = new UserTest();

        // Constructor and Mutators
        userTests.testSecondUserConstructor();
        userTests.testSetUsername();
        userTests.testSetPassword();
        userTests.testSetProfilePic();
        userTests.testSetAccountType();
        userTests.testSetUserID();

        // Everyt other method:
        userTests.testAddBlock();
        userTests.testDeleteBlock();
        userTests.testAddChat();
        userTests.testCreateChat();
        userTests.testHasLogin();
        userTests.testCreateNewUser();
        userTests.testSendText();
    }

}

