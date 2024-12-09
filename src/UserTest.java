import exception.NoChatFoundException;
import object.Chat;
import object.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

import static org.junit.Assert.*;

/**
 * User Tests
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
        String password = "extraSecurePassword1";
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
        File testUserFile = new File("SampleTestFolder/" + testUser.getUserID() + ".txt");
        ArrayList<String> fileContents = new ArrayList<>();

        try (BufferedReader bReader = new BufferedReader(new FileReader(testUserFile))) {

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
                + testUser.getPassWord());
        assertEquals("testSecondUserConstructor: 1st line of new User file is wrong: \n",
                true, result6);


        boolean result7 = fileContents.get(1).equals(testUser.getUsername());
        assertEquals("testSecondUserConstructor: 2nd line of new User file is wrong",
                true, result7);

        // profilepic path should be null, but's printed as a String
        boolean result8 = fileContents.get(2).equals("" + testUser.getProfilePic());
        assertEquals("testSecondUserConstructor: 3rd line of new User file is wrong: ",
                true, result8);

        // accounttype is an int, but's also printed as a String
        boolean result9 = fileContents.get(3).equals("" + testUser.getAccountType());
        assertEquals("testSecondUserConstructor: 4th line of new User file is wrong",
                true, result9);

        boolean result10 = fileContents.get(4).equals("") &&
                fileContents.get(5).equals("") &&
                fileContents.get(6).equals("") &&
                fileContents.get(7).equals("");

        assertEquals("testSecondUserConstructor: the final 4 lines of" +
                "new User file are not just empty newlines", true, result10);

        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }

    }

    // Mutators:
    @Test
    public void testSetUsername() {

        String testName = "This totally works";
        User testUser = new User("username", "extraLongPasswordNoSemicolons1");
        testUser.setUsername(testName);

        String result = testUser.getUsername();
        File testFile = new File("SampleTestFolder/" + testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetUsername: Setting username does not work",
                testName, result);
    }

    @Test
    public void testSetPassword() {

        String testPassword = "This totally works";
        User testUser = new User("username", "extraLongPassword1");
        testUser.setPassword(testPassword);

        String result = testUser.getPassWord();
        File testFile = new File("SampleTestFolder/" + testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetPassword: Setting password does not work",
                testPassword, result);
    }

    @Test
    public void testSetProfilePic() {

        String testPic = "i_dont_know_what_image_format_to_use.png";
        User testUser = new User("username", "realPassword1");
        testUser.setProfilePic(testPic);

        String result = testUser.getProfilePic();
        File testFile = new File("SampleTestFolder/" + testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetProfilePic: Setting the profile pic does not work",
                testPic, result);
    }

    @Test
    public void testSetAccountType() {

        int testType = 1;
        User testUser = new User("username", "DaBestPassword1");
        testUser.setAccountType(testType);

        int result = testUser.getAccountType();
        File testFile = new File("SampleTestFolder/" + testUser.getUserID() + ".txt");
        testFile.delete();
        assertEquals("testSetAccountType: Setting the account type does not work",
                testType, result);
    }

    // this function is just for testing:

    @Test
    public void testSetUserID() {


        String testID = "random id here";
        User testUser = new User("username", "anotherGoodPass1");
        testUser.setUserID(testID);

        String result = testUser.getUserID();
        File testFile = new File("SampleTestFolder/U_0000.txt");
        testFile.delete();
        assertEquals("testSetUserID: Setting the user ID does not work",
                testID, result);
    }

    // All other methods

    // NOTE: Block methods DO NOT UPDATE UserArray as of 11/1/24
    @Test
    public void testAddBlock() {

        // Test 1: blocking an ID that has not already blocked should return true
        String testId1 = "U_0101";
        User blocker = new User("username", "DaGoodPass1");


        blocker.createNewUser("username",
                "DaGoodPass1", testId1); // to be blocked

        boolean result1 = blocker.addBlock(testId1);

        File blockerFile1 = new File(blocker.getUserID() + ".txt");
        blockerFile1.delete();

        // Reset UserIDList.txt
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("testAddBlock: Could not reset UserIDList");
        }

        assertEquals("testAddBlock: Blocking an ID that has not " +
                "already been blocked should return true", true, result1);

        // Test 2: blocking a user not in UserIDList should return false
        String testId2 = "U_9090";
        User blocker2 = new User("dauser", "testPassword1");


        blocker2.createNewUser("dauser", "testPassword1", "wrong ID, man");

        boolean result2 = blocker2.addBlock(testId2);

        File blockerFile2 = new File("SampleTestFolder/" + blocker2.getUserID() + ".txt");
        blockerFile2.delete();

        // Reset UserIDList.txt
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("testAddBlock: Could not reset UserIDList");
        }

        assertEquals("testAddBlock: Blocking a user not in UserIDList should return false",
                false, result2);

        // Test 3: blocking a user that is already blocked should return false
        String testId3 = "U_8732";
        User blocker3 = new User("username59", "daBassword59");


        blocker3.getBlockedList().add(testId3);

        blocker3.createNewUser("username59", "daBassword59", testId3); // being blocked

        boolean result3 = blocker3.addBlock(testId3);

        File blockerFile3 = new File("SampleTestFolder/" + blocker3.getUserID() + ".txt");
        blockerFile3.delete();

        // Reset UserIDList.txt
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("testAddBlock: Could not reset UserIDList");
        }

        assertEquals("testAddBlock: Blocking a user that is already " +
                "blocked should return false", false, result3);

    }

    // Requires addBlock() to work
    @Test
    public void testDeleteBlock() {

        // Test 1: if the blocked ID is removed, then it should no longer
        // be in the UserArray (return true)

        String testId1 = "U_7777";

        User testUser1 = new User("username3", "da3Wordsss");
        testUser1.createNewUser("username3", "da3Wordsss", testId1);

        testUser1.addBlock(testId1);
        boolean result1 = testUser1.deleteBlock(testId1);

        File testFile1 = new File("SampleTestFolder/" + testUser1.getUserID() + ".txt");
        testFile1.delete();
        assertEquals("testDeleteBlock: if the blocked ID is removed," +
                " then is should no longer be in the UserArray", true, result1);

        // Reset UserIdList.txt
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }

        // Test 2: if the blocked ID is not in blocked_ids, then it should return false

        String testId2 = "U_4108";
        User testUser2 = new User("username3123", "DaPassword3123");

        boolean result2 = testUser2.deleteBlock(testId2);

        File testFile2 = new File("SampleTestFolder/" + testUser2.getUserID() + ".txt");
        testFile2.delete();
        assertEquals("testDeleteBlock: if the blocked ID is not in blockedIds," +
                " then it should return false", false, result2);

        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }
    }

    @Test
    public void testAddChat() {

        // Test 1: chat_ids should contain the chat_id after appending
        // func should also return true

        String testChat1 = "C_0319";
        User testUser1 = new User("username123", "daPassword123");

        boolean funcResult1 = testUser1.addChat(testChat1);
        boolean result1 = testUser1.getChatIDList().contains(testChat1);

        File testFile1 = new File("SampleTestFolder/" + testUser1.getUserID() + ".txt");

        testFile1.delete();


        assertEquals("testAddChat: ChatIds should contain " +
                "the chatId after appending", true, result1);

        assertEquals("testAddChat: successfully adding the chat" +
                " should make the function return true", true, funcResult1);

        // Clear the list that contains the chat ids
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("chatIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }

        // Test 2: adding a chat_id that already exists should return false

        String testChat2 = "C_1029";

        User testUser2 = new User("diff user", "diffPassword1");

        testUser2.getChatIDList().add(testChat2);

        boolean funcResult2 = testUser2.addChat(testChat2);
        // item should not appear in arrayList more than once after this
        int count = 0;
        for (String id : testUser2.getChatIDList()) {
            if (id.equals(testChat2)) {
                count++;
            }
        }
        boolean result2 = count > 1;

        File testFile2 = new File("SampleTestFolder/" + testUser2.getUserID() + ".txt");

        testFile2.delete();

        assertEquals("testAddChat: Adding a chat that already" +
                        " exists should not cause the ChatIDList to add another one",
                false, result2);

        assertEquals("testAddChat: Adding a chat that already exists" +
                " should make the function return false", false, funcResult2);

        // Clear the list that contains the chat ids
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("chatIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }
    }

    @Test
    public void testCreateChat() {

        // Test1: Should create a new chat whose ID appears in chat_ids
        ArrayList<String> testID1 = new ArrayList<>(Arrays.asList("C_0808", "C_8989"));
        User testUser = new User("username4124", "daUserPassword1");
        testUser.createChat(testID1);

        boolean result1 = false;

        for (String chatID : testUser.getChatIDList()) {

            // The first new chat ever should be C_0000
            if (chatID.equals("C_0000")) {
                result1 = true;
                break;
            }
        }

        File testFile = new File("SampleTestFolder/" + testUser.getUserID() + ".txt");

        testFile.delete();
        // Clear the list that contains the chat ids
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("chatIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }

        assertEquals("testCreateChat: A new chat should be created" +
                " and its ID should appear in Chat_Ids", true, result1);

        // Clear the list that contains the chat ids
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("chatIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }

        // Clear the chat file
        File testChatFile = new File("SampleTestFolder/C_0000.txt");
        testChatFile.delete();

    }

    // requires createNewUser() to work
    @Test
    public void testHasLogin() {

        // Test 1: returns true if user's username and password is found in the userIDList file
        String testName1 = "username";
        String testPassword1 = "123thisWorks";

        User testUser1 = new User(testName1, testPassword1);
        testUser1.createNewUser(testUser1.getUsername(),
                testUser1.getPassWord(), testUser1.getUserID());
        boolean result1 = testUser1.hasLogin(testName1, testPassword1);


        assertEquals("testHasLogin: UserIdList should contain" +
                " the file information after running writeData()", true, result1);

        // Clear UserIDList.txt
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }

        // Test 2: returns false if user's username and password is not found in the userIDList file
        User testUser2 = new User("some other username", "some other password1");

        testUser2.writeData();
        boolean result2 = testUser2.hasLogin(testName1, testPassword1); // search for some other username or password


        assertEquals("testHasLogin: UserIdList shouldn't contain" +
                " a username and password that wasn't written to it", false, result2);
    }

    // requires getPassword() and setUseID to work
    @Test
    public void testCreateNewUser() {

        // Test 1: user info should appear on the userIDList after being written
        String testUsername = "testUsername";
        String testPassword = "testPassword1";
        String testUserID = "testUserID";

        User testUser1 = new User(testUsername, testPassword);
        testUser1.setUserID(testUserID);

        testUser1.createNewUser(testUser1.getUsername(), testUser1.getPassWord(), testUser1.getUserID());

        String fileContents;
        try (BufferedReader bReader = new BufferedReader(new FileReader("UserIDList.txt"))) {

            fileContents = bReader.readLine(); // there should only be one line

        } catch (IOException e) {
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


        assertEquals("testCreateNewUser: the username returned from the file" +
                " was not the one passed to the user's constructor!", true, usernameChecksOut);

        assertEquals("testCreateNewUser: the password returned from" +
                " the file was not the one passed to the user's constructor!", true, passwordChecksOut);

        assertEquals("testCreateNewUser: the userID returned from" +
                " the file was passed to the user!", true, userIDChecksOut);
        // Clear UserIDList.txt
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
            pWriter.write("");

        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }
    }

    @Test
    public void testFindUsernameFromID() {
        // Create User object
        String testID = "U_1234";
        String testUsername = "TestUser";
        User testUser = new User(testUsername, "password123");
        testUser.setUserID(testID);
        testUser.createNewUser(testUsername, "password123", testID); // Save user

        // Ensure username is correctly found
        String result = testUser.findUsernameFromID(testID);
        File testFile = new File("SampleTestFolder/" + testUser.getUserID() + ".txt");
        testFile.delete();

        assertEquals("testFindUsernameFromID: Improper username returned",
                testUsername, result);

        // Clear list of users
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
            pWriter.write("");
        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }
    }

    @Test
    public void testFindIDFromUsername() {
        // Create User object
        String testID = "U_5678";
        String testUsername = "AnotherUser";
        User testUser = new User(testUsername, "securePassword12");
        testUser.setUserID(testID);
        testUser.createNewUser(testUsername, "securePassword12", testID); // Save user

        // Ensure ID is correctly found
        String result = testUser.findIDFromUsername(testUsername);

        File testFile = new File("SampleTestFolder/" + testUser.getUserID() + ".txt");
        testFile.delete(); // Clean up

        // Clear list of users
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
            pWriter.write("");
        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }

        assertEquals("testFindIDFromUsername: The userID for the given username was not found correctly",
                testID, result);
    }

    @Test
    public void testCheckChatAbility() {
        // Setup: Create two User objects and a Chat object (assuming chat exists)
        String testUserID = "U_1122";
        String testChatID = "C_3344";
        User testUser1 = new User("TestUser1", "password123");
        User testUser2 = new User("TestUser2", "password456");
        testUser2.setUserID("some user ID");

        // Assume the users are added to the chat
        testUser1.addChat(testChatID);  // User 1 has access to the chat
        testUser2.addChat(testChatID);  // User 2 also has access to the chat

        // Test: Check if the first user has the ability to chat in the specified chat
        boolean result1 = Boolean.parseBoolean(testUser1.checkChatAbility(testUser2));  // Check if User 1 can chat
        // with User 2 in the chat
        boolean result2 = Boolean.parseBoolean(testUser2.checkChatAbility(testUser1));  // Check if User 2 can chat
        // with User 1 in the chat

        File testFile = new File("SampleTestFolder/" + testUser1.getUserID() + ".txt");
        File testFile2 = new File("SampleTestFolder/" + testUser2.getUserID() + ".txt");
        testFile.delete();
        testFile2.delete();

        // Clear UserIDList.txt
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("UserIDList.txt")))) {
            pWriter.write("");
        } catch (IOException e) {
            System.out.println("Clearing UserIdFile should not fail");
        }

        // Clear ChatIDList.txt
        try (PrintWriter pWriter = new PrintWriter(new FileOutputStream(new File("ChatIDList.txt")))) {
            pWriter.write("");
        } catch (IOException e) {
            System.out.println("Clearing ChatIdFile should not fail");
        }

        // Both users should have chat ability with each other
        assertTrue("testCheckChatAbility: User 1 should be able to chat with User 2",
                result1);
        assertTrue("testCheckChatAbility: User 2 should be able to chat with User 1",
                result2);
    }
}

