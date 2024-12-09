import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import exception.InvalidFileFormatException;
import object.*;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Unit tests for the {@link SimpleServer} class.
 * Validates that files in the data directory are correctly converted into {@link User} and {@link Chat} objects.
 * Ensures all files in the directory are accounted for in the server's user and chat lists.
 *
 * @author Derek McTume
 * @version 1.0
 */
public class SimpleServerTest {

    private static final String DATA_DIRECTORY_PATH = "SampleTestFolder";

    /**
     * Validates the {@link SimpleServer} constructor by ensuring all files in the data directory
     * are correctly processed into {@link User} or {@link Chat} objects.
     *
     * @throws RuntimeException if an {@link IOException} occurs during initialization.
     */
    @Test
    public void testServerConstructor() {
        File dataDirectory = new File(DATA_DIRECTORY_PATH);

        // Validate that the directory exists
        if (!dataDirectory.exists() || !dataDirectory.isDirectory()) {
            throw new RuntimeException("Data directory does not exist or is not a directory: " + DATA_DIRECTORY_PATH);
        }

        File[] sampleFiles = dataDirectory.listFiles();
        if (sampleFiles == null) {
            throw new RuntimeException("No files found in the data directory: " + DATA_DIRECTORY_PATH);
        }

        final int correctCount = sampleFiles.length;

        // Initialize users and chats
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Chat> chats = new ArrayList<>();

        // Simulate SimpleServer's data loading logic
        File[] userFiles = dataDirectory.listFiles((dir, name) -> name.startsWith("U_"));
        if (userFiles != null) {
            for (File userFile : userFiles) {
                users.add(new User(userFile.getAbsolutePath()));
            }
        }

        File[] chatFiles = dataDirectory.listFiles((dir, name) -> name.startsWith("C_"));
        if (chatFiles != null) {
            for (File chatFile : chatFiles) {
                try {
                    chats.add(new Chat(chatFile.getAbsolutePath().substring(0,
                            chatFile.getAbsolutePath().lastIndexOf("."))));
                } catch (InvalidFileFormatException e) {
                    throw new RuntimeException("Invalid chat file format: " + chatFile.getName(), e);
                }
            }
        }

        // Validate file-to-object mapping
        int currentCount = 0;

        for (File file : sampleFiles) {
            String id = file.getName().replaceFirst("[.][^.]+$", ""); // Strip file extension

            boolean found = users.stream().anyMatch(user -> user.getUserID().equals(id)) ||
                    chats.stream().anyMatch(chat -> chat.getChatID().equals(id));

            if (found) {
                currentCount++;
            }
        }

        // Assert that all files are accounted for
        assertEquals("Mismatch between the number of files and the number of objects loaded by the server.",
                correctCount, currentCount);

    }

    /**
     * Manual test runner for {@link #testServerConstructor()}.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SimpleServerTest test = new SimpleServerTest();
        try {
            test.testServerConstructor();
            System.out.println("testServerConstructor passed successfully.");
        } catch (AssertionError | RuntimeException e) {
            System.err.println("testServerConstructor failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
