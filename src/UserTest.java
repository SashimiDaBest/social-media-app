import org.junit. Test;
import org.junit. Ignore;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Social Media App - Message Tests
 *
 * JUnit tests for Message Class
 *
 * Status: In Progress
 *
 * @author derek mctume
 *
 * @version 11/01/2024
 *
 */

// NOTE: in order for mutator tests to work, accessors must also work
public class UserTest {



    // Mutators:
    public void testSetUsername() {

        String testName = "This totally works";
        User testUser = new User();
        testUser.setUsername(testName);

        String result = testUser.getUsername();
        assertEquals(testName, result);
    }

    public void testSetPassword() {

        String testPassword = "This totally works";
        User testUser = new User();
        testUser.setPassword(testPassword);

        String result = testUser.getPassword();
        assertEquals(testPassword, result);
    }

    public void testSetProfilePic() {

        String testPic = "i_dont_know_what_image_format_to_use.png";
        User testUser = new User();
        testUser.setProfilePic(testPic);

        String result = testUser.getProfilePic();
        assertEquals(testPic, result);
    }

    public void testSetAccountType() {

        int testType = 1;
        User testUser = new User();
        testUser.setAccountType(testType);

        int result = testUser.getAccountType();
        assertEquals(testType, result);
    }

    // All other methods


    // NOTE: Block methods DO NOT UPDATE UserArray as of 11/1/24
    public void testAddBlock() {

        // Test 1: blocking an ID that has not already blocked should return true
        String testId1 = "BLOCK ME, PLEEEASE";
        User.getUserArray().add(testId1);

        User testUser1 = new User();
        boolean result1 = testUser1.addBlock(testId1);
        assertEquals(true, result1);

        // Test 2: blocking a user not in UserArray should return false
        String testId2 = "For second test";

        User testUser2 = new User();
        boolean result2 = testUser2.addBlock(testId2);
        assertEquals(false, result2);

        // Test 3: blocking a user that is already blocked should return false
        String testId3 = "For third test";

        User testUser3 = new User();
        testUser3.getBlockedIds().add(testId3);

        boolean result3 = testUser3.addBlock(testId3);
        assertEquals(false, result3);


    }

    // Requires addBlock() to work
    public void testDeleteBlock() {

        // Test 1: if the blocked ID is removed, then it should no longer
        // be in the UserArray (return true)

        String testId1 = "UNBLOCK ME, PLEEASE";

        User testUser1 = new User();
        testUser1.addBlock(testId1);
        boolean result1 = testUser1.deleteBlock(testId1);
        assertEquals(true, result1);

        // Test 2: if the blocked ID is not in blocked_ids, then it should return false

        String testId2 = "this works";
        User testUser2 = new User();
        boolean result2 = testUser2.deleteBlock(testId2);
        assertEquals(false, result2);

    }

    public void testAddChat() {

        // Test 1: chat_ids should contain the chat_id after appending
        // funct should also return true

        String testChat1 = "Testing chat";
        User testUser1 = new User();
        boolean funcResult1 = testUser1.addChat(testChat1);
        boolean result1 = testUser1.getChat_ids().contains(testChat1);

        assertEquals(true, result1);
        assertEquals(true, funcResult1);

        // Test 2: adding a chat_id that already exists should return false

        String testChat2 = "I'm so tired...zzzz";
        User testUser2 = new User();
        testUser2.getChat_ids().add(testChat2);

        boolean funcResult2 = testUser2.addChat(testChat2);
        boolean result2 = testUser2.getChat_ids().contains(testChat2); // the id should not be added

        assertEquals(false, result2);
        assertEquals(false, funcResult2);
    }

    public void testCreateChat() {

        // Test1: Should create a new chat whose ID appears in chat_ids
        ArrayList<String> testID1 = new ArrayList<>(Arrays.asList("this", "works"));
        User testUser = new User();
        testUser.createChat(testID1);

        boolean result1 = false;
        for (String chatID: testUser.getChat_ids()) {

            if (chatID.equals(testID1)) {
                result1 = true;
            }
        }
        assertEquals(true, result1);

        // Test 2: Should duplicates be prevented???


    }


    // requires createNewUser() to work
    public void testHasLogin() {

        // Test 1: returns true if username is in HashMap and key fits username
        String testName1 = "username";
        String testPassword1 = "123thisWorks";

        User testUser1 = new User();
        testUser1.createNewUser(testName1, testPassword1);
        boolean result1 = testUser1.hasLogin(testName1, testPassword1);

        assertEquals(true, result1);

        // Test 2: returns false if username is not in userpass
        String testName2 = "this works";
        String testPassword2 = "no I doesn't";

        User testUser2 = new User();
        boolean result2 = testUser2.hasLogin(testName2, testPassword2);

        assertEquals(false, result2);

        // Test 3: returns false if the username does not match the password
        String testName3 = "this doesn't match";
        String testPassword3 = "YES IT DOES";
        String otherPassword3 = "other password to try";

        User testUser3 = new User();
        testUser3.createNewUser(testName3, testPassword3);
        boolean result3 = testUser3.hasLogin(testName3, otherPassword3);

        assertEquals(false, result3);
    }

    // requires getPassword() to work
    public void testCreateNewUser() {

        // Test 1: userPass should contain the new username/password pair
        String testName = "username";
        String testPass = "password";
        User testUser = new User();

        testUser.createNewUser(testName, testPass);
        boolean keyIsIn = testUser.getUserPass().containsKey(testName);
        String valCheck = testUser.getUserPass().get(testName);

        assertEquals(true, keyIsIn);
        assertEquals(testPass, valCheck);
    }

    // requires addChat() to work
    public void testSendText() {

        // Test 1: throws NoChatFoundException if chat_id is not in chat_ids
        try {

            String testChatId = "this works";


        } catch (Exception e) {

            assertEquals(new NoChatFoundException("No chat found"), e);
        }
    }
}
