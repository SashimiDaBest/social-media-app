package clientPageOperation;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * The UserPageClient class handles operations related to a user's profile page
 * in the client-side application. It provides options to view and manage followers,
 * following, and blocked users, change the user profile (incomplete), and navigate
 * back to the main feed view.
 *
 * <p>Features and Functionality:</p>
 * <ul>
 *     <li>Display the user's username and account type (public/private)</li>
 *     <li>Option to change the user profile (future feature for image handling)</li>
 *     <li>View the list of followers, following, or blocked users and navigate to their profiles</li>
 *     <li>Navigate back to the main feed view</li>
 * </ul>
 *
 * @version 1.0
 * @author Soleil Pham
 */
public final class UserPageClient {
    /**
     * Main method for user page operations. It reads the user's details and
     * provides options for interacting with their profile.
     *
     * @param scanner Scanner for user input
     * @param br      BufferedReader for reading server responses
     * @param bw      BufferedWriter for sending messages to the server
     */
    public static void userPage(Scanner scanner, BufferedReader br, BufferedWriter bw) {
        String username = "";
        String accountType = "";

        // Read username and account type from the server
        try {
            String line = br.readLine();
            username = line;
            if (line != null) {
                username = line;
                line = br.readLine();
                accountType = "1".equals(line) ? "private" : "public";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display menu and handle user input
        while (true) {
            System.out.println("Welcome to the User Page\n" +
                    "USERNAME: " + username + "\n" +
                    "ACCOUNT_TYPE: " + accountType + "\n" +
                    "1 - Change User Profile\n" +
                    "2 - View Follower\n" +
                    "3 - View Following\n" +
                    "4 - View Blocked\n" +
                    "5 - Go Back to Feed View\n" + 
                    "6 - Quit");
            String input = scanner.nextLine();

            if (input.equals("1")) {
                write("1", bw);
                // TODO: Implement profile change functionality (e.g., image storage)
            } else if (input.equals("2")) {
                write("2", bw);
                readAndPrint(br);
                System.out.print("Do you want to view Other (Y/N): ");
                String input2 = scanner.nextLine();
                if (input2.equals("Y")) {
                    try {
                        bw.write("VIEW");
                        bw.newLine();
                        bw.flush();
                        System.out.print("Other Username: ");
                        String otherUsername = scanner.nextLine();
                        OtherPageClient.otherPage(scanner, otherUsername, br, bw);
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bw.newLine();
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (input.equals("3")) {
                write("3", bw);
                readAndPrint(br);
                System.out.print("Do you want to view Other (Y/N): ");
                String input2 = scanner.nextLine();
                if (input2.equals("Y")) {
                    try {
                        bw.write("VIEW");
                        bw.newLine();
                        bw.flush();
                        System.out.print("Other Username: ");
                        String otherUsername = scanner.nextLine();
                        OtherPageClient.otherPage(scanner, otherUsername, br, bw);
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bw.newLine();
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (input.equals("4")) {
                write("4", bw);
                readAndPrint(br);
                System.out.print("Do you want to view Other (Y/N): ");
                String input2 = scanner.nextLine();
                if (input2.equals("Y")) {
                    try {
                        bw.write("VIEW");
                        bw.newLine();
                        bw.flush();
                        System.out.print("Other Username: ");
                        String otherUsername = scanner.nextLine();
                        OtherPageClient.otherPage(scanner, otherUsername, br, bw);
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bw.newLine();
                        bw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (input.equals("5")) {
                write("5", bw);
                FeedPageClient.feedPage(scanner, br, bw);
                break;
            } else if (input.equals("6")) {
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Reads and prints lines from the server until "END" is received.
     *
     * @param br BufferedReader for reading server responses
     * @return true if successful, false otherwise
     */
    public static boolean readAndPrint(BufferedReader br) {
        try {
            String line = br.readLine();
            while (line != null && !line.equals("END")) {
                System.out.println(line);
                line = br.readLine();
            }
            return true;
        } catch (Exception e) {
            System.out.println("readAndPrint() ERROR");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes a message to the server and flushes the BufferedWriter.
     *
     * @param outMessage The message to send
     * @param bw         BufferedWriter for sending the message
     * @return true if successful, false otherwise
     */
    public static boolean write(String outMessage, BufferedWriter bw) {
        try {
            bw.write(outMessage);
            bw.newLine();
            bw.flush();
            return true;
        } catch (IOException e) {
            System.out.println("write() ERROR");
            e.printStackTrace();
            return false;
        }
    }
/*
    public void uploadPhoto(String path, BufferedWriter bw, Socket socket) throws IOException {

     Socket socket

        try {
            FileInputStream fileInputStream = new FileInputStream(new File("path/to/your/image.jpg"));
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[4096]; // Buffer size of 4 KB
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Image uploaded successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

 */
}
