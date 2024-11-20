import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import object.*;
import org.junit.Test;

import java.util.ArrayList;

/**
 * This class contains unit tests for the {@link SimpleServer} class.
 * It checks the functionality of the server constructor and ensures that files in the data
 * directory are correctly converted into {@link User} and {@link Chat} objects.
 * The tests validate that every file in the directory has a corresponding {@link User} or
 * {@link Chat} object in the server's internal lists.
 *
 * @author Derek McTume
 * @version 1.0
 */
public class SimpleServerTest {

    /**
     * This test checks the constructor of the {@link SimpleServer} class to ensure that it
     * correctly processes files from the data directory into {@link User} and {@link Chat}
     * objects. It compares the number of files in the data directory with the number of
     * corresponding objects in the server's internal lists.
     * <p>
     * The test assumes that the data directory contains text files, where each file represents
     * either a {@link User} or a {@link Chat}. The file names should match the IDs of the
     * corresponding {@link User} or {@link Chat} object. The test iterates through the files
     * and checks if their names correspond to any user or chat IDs, and asserts that the
     * total number of objects in the server's lists matches the number of files in the directory.
     * </p>
     *
     * @throws RuntimeException if an {@link IOException} occurs while loading files.
     */
    @Test
    public void testServerConstructor() {

        // Test 1: check if every file gets converted into a user object or chat object
        // How?: check the file names and try to match them with the user and chat ids
        File dataDirectory = new File("Sample Test Folder");
        File[] sampleFiles = dataDirectory.listFiles();
        final int correctCount = sampleFiles.length;
        int currentCount = 0;

        try {

            SimpleServer testServer = new SimpleServer(null);

            ArrayList<User> users = testServer.getUsers();
            ArrayList<Chat> chats = testServer.getChats();

            // Iterate through files and check if each one corresponds to a User or Chat
            for (File file : sampleFiles) {
                String id = file.getName().split(".txt")[0];

                // check if id matches a User
                boolean foundOne = false;
                for (User user : users) {
                    if (user.getUserID().equals(id)) {
                        currentCount++;
                        foundOne = true;
                        break;
                    }
                }

                // Don't check chats if a matching User is found
                if (foundOne) {
                    continue;
                }

                // If no User found, check if it matches a Chat
                for (Chat chat : chats) {
                    if (chat.getChatID().equals(id)) {
                        currentCount++;
                        break;
                    }
                }
            }

            // Assert that the number of users and chats matches the number of files
            assertEquals("testServerConstructor: a user or chat from the files is missing in the arraylists",
                    correctCount, currentCount);

        } catch (IOException e) {
            System.out.println("testServerConstructor should never throw an error; failing test...");
            throw new RuntimeException(e);
        }
    }

    /**
     * The main method runs the {@link #testServerConstructor()} test. This method is used for
     * manual testing, typically in a development environment where the test is run outside of
     * the JUnit framework.
     *
     * @param args command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        SimpleServerTest test = new SimpleServerTest();
        test.testServerConstructor();
    }
}
