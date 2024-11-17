package clientPageOperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class OtherPageClient {

    /**
     * Send other username over to server
     * 1st and 2nd input - add or remove other from following/blocked list - incomplete
     * note: only when the private other user follow user will both be able to chat
     * 3rd and 4th input - figure out if user have permission to view; display list; give option to navigate to another user profile
     * 5th input redirect to feed page
     * @param scanner
     * @param otherUsername
     * @param br
     * @param bw
     */

    public static void otherPage(Scanner scanner, String otherUsername, BufferedReader br, BufferedWriter bw) {
        try {
            System.out.println("OTHER USERNAME: " + otherUsername);
            bw.write(otherUsername);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
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
                    System.out.print("Do you want to view Other (Y/N): ");
                    String input2 = scanner.nextLine();
                    if (input2.equals("Y")) {
                        System.out.print("Other Username: ");
                        String other = scanner.nextLine();
                        otherPage(scanner, other, br, bw);
                        break;
                    }
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
                    System.out.print("Do you want to view Other (Y/N): ");
                    String input2 = scanner.nextLine();
                    if (input2.equals("Y")) {
                        System.out.print("Other Username: ");
                        String other = scanner.nextLine();
                        otherPage(scanner, other, br, bw);
                        break;
                    }
                }
            } else if (input.equals("5")) {
                FeedPageClient.feedPage(scanner, br, bw);
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
    }
}
