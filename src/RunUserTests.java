import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Run User Tests Class
 * <p>
 * Tests some of the methods and constructors within the User class.
 * <p>
 * Status: Complete
 * </p>
 *
 * @author Connor Pugliese
 * @version 11/03/2024
 * @since 1.0
 */
public class RunUserTests {

    @Test
    public void testUserMethods() {
        User[] testUsers = new User[10];

        for (int i = 0; i < 10; i++) {
            testUsers[i] = new User("userNumber" + i, "userPassword" + i);
            testUsers[i].createNewUser(testUsers[i].getUsername(), testUsers[i].getPassWord(),
                    testUsers[i].getUserID());
        }

        testUsers[0].addFollower(testUsers[1].getUserID());
        testUsers[1].addFollowing(testUsers[0].getUserID());

        testUsers[0].addFollower(testUsers[2].getUserID());
        testUsers[2].addFollowing(testUsers[0].getUserID());

        testUsers[0].addBlock(testUsers[3].getUserID());
        testUsers[0].addBlock(testUsers[4].getUserID());

        ArrayList<String> members = new ArrayList<>();
        members.add(testUsers[0].getUserID());
        members.add(testUsers[5].getUserID());
        members.add(testUsers[6].getUserID());

        Chat testChat = new Chat(members);
        Chat testChat2 = new Chat(members);
        testUsers[0].addChat(testChat.getChatID());
        testUsers[0].addChat(testChat2.getChatID());

        testUsers[0].setProfilePic("test/path/pic.jpeg");

        assertEquals("User does not properly get or assign userIDs.", "U_0000", testUsers[0].getUserID());
        assertEquals("User does not properly get or assign usernames.", "userNumber0", testUsers[0].getUsername());
        assertEquals("User does not properly get or assign passwords.", "userPassword0", testUsers[0].getPassWord());
        assertEquals("User does not properly get or assign profile pic paths.", "test/path/pic.jpeg", testUsers[0].getProfilePic());
        assertEquals("User does not properly get or assign account type.", 0, testUsers[0].getAccountType());

        ArrayList<String> expectedFollowerList = new ArrayList<>();
        expectedFollowerList.add("U_0001");
        expectedFollowerList.add("U_0002");
        assertEquals("User does not properly get or assign list of followers.", expectedFollowerList, testUsers[0].getFollowerList());

        ArrayList<String> expectedFollowingList = new ArrayList<>();
        expectedFollowingList.add("U_0000");
        assertEquals("User does not properly get or assign list of following users.", expectedFollowingList, testUsers[1].getFollowingList());

        ArrayList<String> expectedBlockList = new ArrayList<>();
        expectedBlockList.add("U_0003");
        expectedBlockList.add("U_0004");
        assertEquals("User does not properly get or assign list of blocked users.", expectedBlockList, testUsers[0].getBlockedList());

        ArrayList<String> expectedChatList = new ArrayList<>();
        expectedChatList.add("C_0000");
        expectedChatList.add("C_0001");
        assertEquals("User does not properly get or assign list of chats.", expectedChatList, testUsers[0].getChatIDList());

        testUsers[0].deleteFollower(testUsers[1].getUserID());
        testUsers[1].deleteFollowing(testUsers[0].getUserID());

        expectedFollowerList.remove(0);
        expectedFollowingList.remove(0);

        assertEquals("User does not properly remove followers.", expectedFollowerList, testUsers[0].getFollowerList());
        assertEquals("User does not properly remove following users.", expectedFollowingList, testUsers[1].getFollowingList());

        for (int i = 0; i < 10; i++) {
            File f = new File("U_000" + i + ".txt");
            if (f.exists())
                f.delete();
        }

        for (int i = 0; i < 2; i++) {
            File f = new File("C_000" + i + ".txt");
            if (f.exists())
                f.delete();
        }

        File f = new File("UserIDList.txt");
        File g = new File("chatIDList.txt");

        assertTrue("findUser() returns false on a user that exists.", testUsers[8].findUser(testUsers[9].getUserID()));
        assertFalse("findUser() returns true when searching for a user that does not exist.",
                testUsers[8].findUser("U_1000"));

        assertTrue("userNameValidation() returns true on a free username.", testUsers[8].userNameValidation("free"));
        assertFalse("userNameValidation() returns false on a taken username.", testUsers[8].userNameValidation("userNumber0"));

        // Clear the UserIDList and ChatIDList files
        try (FileWriter fWriter = new FileWriter(f, false);
             FileWriter gWriter = new FileWriter(g, false)) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        RunUserTests test = new RunUserTests();
        test.testUserMethods();
    }
}
