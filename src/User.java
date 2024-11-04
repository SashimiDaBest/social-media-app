import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;

public class User implements UserInterface{
    private String userName;
    private String userID;
    private String photoPathway;
    private ArrayList<String> followerList;
    private ArrayList<String> followingList;
    private ArrayList<String> blockedList;
    private ArrayList<String> chatIDList;
    private int accountType;
    private String password;
    private static AtomicInteger counter = new AtomicInteger(0);
    private final String userIDinfo = this.userID + ".txt";
    private static final String userIDList = "UserIDList.txt";

    public User(String userIdinfo){
        try(BufferedReader br = new BufferedReader(new FileReader(userIdinfo))){
            String line1 = "";
            line1 = br.readLine();
            this.userID = line1.split(";")[0];
            this.password = line1.split(";")[1];
            this.userName = br.readLine();
            this.photoPathway = br.readLine();
            this.accountType = Integer.parseInt(br.readLine());
            String followers = br.readLine();
            String followersArray[] = followers.split(";");
            for(String user: followersArray){
                followerList.add(user);
            }
            String following = br.readLine();
            String followingArray[] = following.split(";");
            for(String user: followingArray){
                followingList.add(user);
            }
            String blocking = br.readLine();
            String blockingArray[] = blocking.split(";");
            for(String user: blockingArray){
                blockedList.add(user);
            }
            String chatting = br.readLine();
            String chattingArray[] = chatting.split(";");
            for(String user: chattingArray){
                chatIDList.add(user);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public User(String userName, String password){
        //check if username is valid before you pass it into the constructor, then inside the constructor generate a new id
        //creating a new user from just the username and password

        this.userName = userName;
        this.password = password; 
        this.userID = createUserID();
        this.accountType = 0;
        this.photoPathway = null;
        followerList = new ArrayList<String>();
        followingList = new ArrayList<String>();
        blockedList = new ArrayList<String>();
        chatIDList = new ArrayList<String>();

        try(PrintWriter pw = new PrintWriter(new FileWriter(this.userID + ".txt"))){
        try(PrintWriter pw = new PrintWriter(new FileWriter(this.userID + ".txt"))){
            pw.println(this.userID + ";" + this.userName);
            pw.println(this.userName);
            pw.println(this.photoPathway);
            pw.println(this.accountType);
            pw.println("");
            pw.println("");
            pw.println("");
            pw.println("");
        }
        catch(IOException e){
            e.printStackTrace();
        }

        counter.set(0);

        counter.set(0);
    }

    public void setUsername(String username){
        this.userName = username;
    }

    public String getUsername(){
        return this.userName;
    }

    public String getUserID(){
        return this.userID;
    }

    public String createUserID() {
        String id = "U_";

        try (BufferedReader reader = new BufferedReader(new FileReader(userIDList))) {
            String line = reader.readLine();
            while (line != null) {
                counter.incrementAndGet();

                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        try (BufferedReader reader = new BufferedReader(new FileReader(userIDList))) {
            String line = reader.readLine();
            while (line != null) {
                counter.incrementAndGet();

                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String number = String.valueOf(counter.get());
        int length = number.length();
        for (int i = 0; i < 4 - length; i++) {
            id += "0";
        }
        return id + number;
    }

    public void setProfilePic(String photoPathway){
        this.photoPathway = photoPathway;
        writeData();
    }

    public String getProfilePic(){
        return this.photoPathway;
    }

    public ArrayList<String> getFollowerList() {
        return followerList;
    }

    //add writeData() method
    public boolean deleteFollower(String followerID) {
        if (findUser(followerID)){
            for (int i = 0; i < followerList.size(); i++){
                if (followerList.get(i).equals(followerID)){
                    followerList.remove(i);
                    writeData();
            for (int i = 0; i < followerList.size(); i++){
                if (followerList.get(i).equals(followerID)){
                    followerList.remove(i);
                    writeData();
                    return true;
                    
                    
                }
            }
        }
        return false;
    }


    //add writeData() method
    public void writeData(){
        try(PrintWriter pr = new PrintWriter(new FileWriter(this.getUserID() + ".txt"))){
        try(PrintWriter pr = new PrintWriter(new FileWriter(this.getUserID() + ".txt"))){
            pr.println(this.userID + ";" + this.password);
            pr.println(this.userName);
            pr.println(this.photoPathway);
            pr.println(this.accountType);
            for(int i = 0; i < followerList.size(); i++){
                if(i != followerList.size() - 1){
                    pr.print(followerList.get(i) + ";");
                }
                else{
                    pr.println(followerList.get(i));
                }
            }
            for(int i = 0; i < followingList.size(); i++){
                if(i != followingList.size() - 1){
                    pr.print(followingList.get(i) + ";");
                }
                else{
                    pr.println(followingList.get(i));
                }
            }
            for(int i = 0; i < blockedList.size(); i++){
                if(i != blockedList.size() - 1){
                    pr.print(blockedList.get(i) + ";");
                }
                else{
                    pr.println(blockedList.get(i));
                }
            }
            for(int i = 0; i < chatIDList.size(); i++){
                if(i != chatIDList.size() - 1){
                    pr.print(chatIDList.get(i) + ";");
                }
                else{
                    pr.println(chatIDList.get(i));
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public boolean addFollower(String followerID) {
        //converse about this class and the possibility to be false on different occasions....
        if (findUser(followerID)){
            for (int i = 0; i < followerList.size(); i++){
                if (followerList.get(i).equals(followerID)) {
                    writeData();
                    return true;
                }
            }
        } else {
            return false;
        }
        followerList.add(followerID);
        writeData();
        followerList.add(followerID);
        return true;
    }

    public ArrayList<String> getFollowingList() {
        return followingList;
    }
    //add writeData() method
    public boolean addFollowing(String followingID) {
        //address different types of false conditions?
        if (findUser(followingID) && !followingList.contains(followingID) && !blockedList.contains(followingID)) {
            followingList.add(followingID);
            writeData();
            writeData();
            return true;
        }
        return false;
    }
    //add writeData() method
    public boolean deleteFollowing(String followingID) {
        if (followingList.contains(followingID)) {
            followingList.remove(followingID);
            writeData();
            writeData();
            return true;
        }
        return false;
    }

    public ArrayList<String> getBlockedList() {
        return blockedList;
    }
    //add writeData() method
    public boolean addBlock(String blockedID) {
        if(findUser(blockedID) && !blockedList.contains(blockedID)){
            blockedList.add(blockedID);
            writeData();
            writeData();
            return true;
        }
        return false;
    }
    //add writeData() method
    public boolean deleteBlock(String blockedID) {
        if(blockedList.contains(blockedID)){
            blockedList.remove(blockedID);
            writeData();
            writeData();
            return true;
        }
        return false;
    }

    public ArrayList<String> getChatIDList() {
        return chatIDList;
    }
    //add writeData() method
    public boolean addChat(String chat_id) {
        chatIDList.add(chat_id);
        writeData();
        writeData();
        return true;
    }
    //add writeData() method
    public void createChat(ArrayList<String> recipient_id) {
        Chat newChat = new Chat(recipient_id);
        chatIDList.add(newChat.getChatID());
        writeData();
        writeData();
    }
    //add writeData() method
    public boolean deleteChat(String chat_id) {
        chatIDList.remove(chat_id);
        writeData();
        writeData();
        return false;
    }

    public int getAccountType(){
        return this.accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
        writeData();
    }

    public String getPassWord() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        writeData();
    }

    public boolean findUser(String userID) {
        try (BufferedReader br = new BufferedReader(new FileReader(userIDList))) {
        try (BufferedReader br = new BufferedReader(new FileReader(userIDList))) {
            String line = br.readLine();
            while (line != null){
                if (line.substring(line.length() - 6).equals(userID)) {
                if (line.substring(line.length() - 6).equals(userID)) {
                    return true;
                }
                line = br.readLine();
                line = br.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean sendText(String chatID, String message, int type, String userID, String username, int userType) throws NoChatFoundException {
        if (chatIDList.contains(chatID)) {
            Chat existingChat = null;
            try {
                existingChat = new Chat(chatID);
            } catch (InvalidFileFormatException e) {
                e.printStackTrace();
            }
            Message intendedMessage = new Message(userID, type, message);
            existingChat.addMessage(intendedMessage);
            return true;
        }
        throw new NoChatFoundException("No chat found");
    }
    //hi
//hello
    //revise ---------
    public boolean hasLogin(String username, String password) {
        try(BufferedReader br = new BufferedReader(new FileReader(userIDList))){
            String userIterator = "";
            while((userIterator = br.readLine()) != null){
                String userN = userIterator.split(";")[0];
                String passW = userIterator.split(";")[1];
                if(username.equals(userN) && password.equals(passW)){
                    return true;
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean userNameValidation(String username){
        try(BufferedReader br = new BufferedReader(new FileReader(userIDList))){
            String userIterator = "";
            while((userIterator = br.readLine()) != null){
                String userN = userIterator.split(";")[0];
                if(userN.equals(username)){
                    return false;
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }


    public void createNewUser(String username, String password, String userIDparameter) {
        try(PrintWriter pw = new PrintWriter(new FileWriter(userIDList, true))){
        try(PrintWriter pw = new PrintWriter(new FileWriter(userIDList, true))){
            pw.println(username + ";" + password + ";" + userIDparameter);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // for testing
    public void setUserID(String id) {
        this.userID = id;
    }

}