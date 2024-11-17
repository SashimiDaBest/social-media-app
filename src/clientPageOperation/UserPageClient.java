package clientPageOperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserPageClient {

    //TODO: figure out how to take and store images & add log in log out feature
    //TODO: if user click exit, close bufferedwriter and bufferedreader to close the underlying socket as well

    /**
     * read 2 lines and replace value of username and account type
     * take in user input
     * 1st input is incomplete - image storing
     * 2nd input - view all followers; take other username and redirect to other profile page
     * 3nd and 4th input does the same for following and follower
     * 5th input redirect to feed page
     * @param scanner
     */

    public static void userPage(Scanner scanner, BufferedReader br, BufferedWriter bw) {
        String username = "";
        String accountType = "";
        try {
            String line = br.readLine();
            username = line;
            if (line != null) {
                line = br.readLine();
                System.out.println("Line: " + line);
                if (line.equals("1")) {
                    accountType = "private";
                } else {
                    accountType = "public";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            System.out.println("Welcome to the User Page\n" +
                    "USERNAME: " + username + "\n" +
                    "ACCOUNT_TYPE: " + accountType + "\n" +
                    "1 - Change User Profile\n" +
                    "2 - View Follower\n" +
                    "3 - View Following\n" +
                    "4 - View Blocked\n" +
                    "5 - Go Back to Feed View");
            String input = scanner.nextLine();
            if (input.equals("1")) {
                write("1", bw);
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
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    public static boolean readAndPrint(BufferedReader br) {
        try {
            String line = br.readLine();
            System.out.println(line);
            while (line != null && !line.equals("END")) {
                line = br.readLine();
                if (line.equals("END")) {
                    break;
                }
                System.out.println(line);
            }
            return true;
        } catch (Exception e) {
            System.out.println("readAndPrint() ERROR");
            e.printStackTrace();
            return false;
        }
    }

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
}
