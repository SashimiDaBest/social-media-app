package clientPageOperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * The OtherPageClient class handles operations related to viewing and interacting
 * with other users' pages. It allows the user to follow/unfollow, block/unblock,
 * view the followers and following list of another user, and navigate back to the
 * feed view.
 *
 * This class communicates with the server using BufferedReader and BufferedWriter
 * to send and receive data.
 *
 * <p>Features and Functionality:</p>
 * <ul>
 * <li>Follow/Unfollow another user</li>
 * <li>Block/Unblock another user</li>
 * <li>View the follower and following lists</li>
 * <li>Navigate back to the feed page</li>
 * </ul>
 *
 * @author Soleil Pham
 * @version 1.0
 */
public final class OtherPageClient {

    /**
     * Handles the other user's page operations.
     * Sends the other username to the server and provides options for the user to:
     * 1. Follow/Unfollow the other user
     * 2. Block/Unblock the other user
     * 3. View the other user's followers
     * 4. View the other user's following list
     * 5. Return to the feed view
     *
     * @param scanner       The Scanner object for user input
     * @param otherUsername The username of the other user
     * @param br            BufferedReader for reading server responses
     * @param bw            BufferedWriter for sending messages to the server
     */
    public static void otherPage(Scanner scanner, String otherUsername, BufferedReader br, BufferedWriter bw) {
        try {
            // Send the other username to the server
            System.out.println("OTHER USERNAME: " + otherUsername);
            bw.write(otherUsername);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Main loop for handling user options
        while (true) {
            // Display menu options
            System.out.println("Welcome to the Other Page\n" +
                    "OTHER USERNAME: " + otherUsername + "\n" +
                    "1 - Follow/Unfollow Other\n" +
                    "2 - Block/Unblock Other\n" +
                    "3 - View Follower\n" +
                    "4 - View Following\n" +
                    "5 - Go Back to Feed View\n" +
                    "Input: ");
            String input = scanner.nextLine();

            if (input.equals("1")) {
                UserPageClient.write("1", bw);
                try {
                    System.out.print(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (input.equals("2")) {
                UserPageClient.write("2", bw);
                try {
                    System.out.print(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (input.equals("3")) {
                UserPageClient.write("3", bw);
                boolean canView = false;
                try {
                    String line = br.readLine();
                    if (line.equals("message")) {
                        canView = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UserPageClient.readAndPrint(br);
                if (canView) {
                    System.out.print("Do you want to view another user? (Y/N): ");
                    String input2 = scanner.nextLine();
                    if (input2.equals("Y")) {
                        UserPageClient.write("CHANGE", bw);
                        System.out.print("Other Username: ");
                        String other = scanner.nextLine();
                        otherPage(scanner, other, br, bw);
                        break;
                    }
                } else {
                    UserPageClient.write("", bw);
                }
            } else if (input.equals("4")) {
                UserPageClient.write("4", bw);
                boolean canView = false;
                try {
                    String line = br.readLine();
                    if (line.equals("message")) {
                        canView = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UserPageClient.readAndPrint(br);
                if (canView) {
                    System.out.print("Do you want to view another user? (Y/N): ");
                    String input2 = scanner.nextLine();
                    if (input2.equals("Y")) {
                        UserPageClient.write("CHANGE", bw);
                        System.out.print("Other Username: ");
                        String other = scanner.nextLine();
                        otherPage(scanner, other, br, bw);
                        break;
                    } else {
                        UserPageClient.write("", bw);
                    }
                }
            } else if (input.equals("5")) {
                UserPageClient.write("5", bw);
                FeedPageClient.feedPage(scanner, br, bw);
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
