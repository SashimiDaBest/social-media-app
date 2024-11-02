import org.junit.Test;

import static org.junit.Assert.*;

public class RunUserTests {
    @Test
    public void testGetters() {
        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setPassword("TestPassword");
        testUser.setAccountType(0);
        testUser.setProfilePic("Test/Profile/Pic/Path/pic.jpg");

        assertEquals("getUsername does not return expected username.", testUser.getUsername(), "TestUser");
        assertEquals("getPassword does not return expected password.", testUser.getPassword(), "TestPassword");
        assertEquals("getAccountType does not return expected account type.", testUser.getAccountType(), 0);
        assertEquals("getProfilePic does not return expected profile pic path.", testUser.getProfilePic(),
                "Test/Profile/Pic/Path/pic.jpg");
    }

    @Test
    public void testFindUser() {
        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setPassword("TestPassword");
        testUser.setAccountType(0);
        testUser.setProfilePic("Test/Profile/Pic/Path/pic.jpg");
        testUser.setuser_id("U_0001");

        assertTrue("findUser method returns false for a user that exists.", testUser.findUser("U_0001"));
        assertFalse("findUser method returns true for a user that does not exist.", testUser.findUser("U_0002"));
    }

    @Test
    public void testAddFollower() {
        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setPassword("TestPassword");
        testUser.setAccountType(0);
        testUser.setProfilePic("Test/Profile/Pic/Path/pic.jpg");
        testUser.setuser_id("U_0001");

        User testUser2 = new User();
        testUser2.setUsername("TestUser2");
        testUser2.setPassword("TestPassword2");
        testUser2.setAccountType(0);
        testUser2.setProfilePic("Test/Profile/Pic/Path/pic2.jpg");
        testUser2.setuser_id("U_0002");

        assertTrue("addFollower method fails to follow a user.", testUser.addFollower(testUser2.getUser_id()));
        assertFalse("addFollower method successfully follows a user that does not exist.",
                testUser.addFollower("U_0000"));
    }

    @Test
    public void testDeleteFollower() {
        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setPassword("TestPassword");
        testUser.setAccountType(0);
        testUser.setProfilePic("Test/Profile/Pic/Path/pic.jpg");
        testUser.setuser_id("U_0001");

        User testUser2 = new User();
        testUser2.setUsername("TestUser2");
        testUser2.setPassword("TestPassword2");
        testUser2.setAccountType(0);
        testUser2.setProfilePic("Test/Profile/Pic/Path/pic2.jpg");
        testUser2.setuser_id("U_0002");

        testUser.addFollower(testUser2.getUser_id());

        assertTrue("deleteFollower returns false when a valid follower is to be deleted",
                testUser.deleteFollower(testUser2.getUser_id()));

        assertFalse("deleteFollower returns true when the follower to be deleted does not exist.",
                testUser.deleteFollower("U_0000"));
    }

    @Test
    public void testAddFollowing() {
        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setPassword("TestPassword");
        testUser.setAccountType(0);
        testUser.setProfilePic("Test/Profile/Pic/Path/pic.jpg");
        testUser.setuser_id("U_0001");

        User testUser2 = new User();
        testUser2.setUsername("TestUser2");
        testUser2.setPassword("TestPassword2");
        testUser2.setAccountType(0);
        testUser2.setProfilePic("Test/Profile/Pic/Path/pic2.jpg");
        testUser2.setuser_id("U_0002");

        assertTrue("addFollower returns false when a valid user is to be followed.",
                testUser.addFollowing(testUser2.getUser_id()));

        assertFalse("addFollower returns true when a user that does not exist is to be followed.",
                testUser.addFollowing("U_0000"));
    }

    @Test
    public void testDeleteFollowing() {
        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setPassword("TestPassword");
        testUser.setAccountType(0);
        testUser.setProfilePic("Test/Profile/Pic/Path/pic.jpg");
        testUser.setuser_id("U_0001");

        User testUser2 = new User();
        testUser2.setUsername("TestUser2");
        testUser2.setPassword("TestPassword2");
        testUser2.setAccountType(0);
        testUser2.setProfilePic("Test/Profile/Pic/Path/pic2.jpg");
        testUser2.setuser_id("U_0002");

        testUser.addFollowing(testUser2.getUser_id());

        assertTrue("deleteFollowing returns false when a valid follower is to be deleted.",
                testUser.deleteFollowing(testUser2.getUser_id()));

        assertFalse("deleteFollowing returns true when a follower that does not exist is to be deleted.",
                testUser.deleteFollowing("U_0000"));
    }

}
