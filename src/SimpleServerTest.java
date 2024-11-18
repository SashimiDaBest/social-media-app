import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import object.*;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Message Tests
 * <p>
 * JUnit tests for Message Class
 * <p>
 * Status: Complete
 *
 * @author Derek Mctume
 * @version 11/01/2024
 */

public class SimpleServerTest {



    // this simulaneously checks accessors to make sure they reutnr the correct lists
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

            for (File file: sampleFiles) {
                String id = file.getName().split(".txt")[0];

                // check if id shows up in users
                boolean foundOne = false;
                for (User user: users) {
                    if (user.getUserID().equals(id)) {
                        currentCount++;
                        foundOne = true;
                        break;
                    }
                }
                
                // Don't look for a matching chat if a user is already found
                if (foundOne) {
                    continue;
                }

                // if not a userId, rummage through chats
                for (Chat chat: chats) {
                    if (chat.getChatID().equals(id)) {
                        currentCount++;
                        break;
                    }
                }
            }

            assertEquals("testServerConstructor: a user or chat from the files is missing in the arraylists",
                correctCount, currentCount);

        } catch (IOException e) {
            System.out.println("testServerConstructor should never thrown an error; failing test...");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SimpleServerTest test = new SimpleServerTest();
        test.testServerConstructor();
    }
}
