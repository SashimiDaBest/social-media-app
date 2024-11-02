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
            String line = "";
            while((line = br.readLine()) != null){
                
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
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
        String number = String.valueOf(counter.get());
        int length = number.length();
        for (int i = 0; i < 4 - length; i++) {
            id += "0";
        }
        return id + number;
    }

    public void setProfilePic(String photoPathway){
        this.photoPathway = photoPathway;
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
            for (int i = 0; i < followingList.size(); i++){
                if (followingList.get(i).equals(followerID)){
                    followingList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }


    //add writeData() method
    public void writeData(){
        try(PrintWriter pr = new PrintWriter(new FileWriter(userIDinfo))){
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
                    return true;
                }
            }
        } else {
            return false;
        }
        followingList.add(followerID);
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
            return true;
        }
        return false;
    }
    //add writeData() method
    public boolean deleteFollowing(String followingID) {
        if (followingList.contains(followingID)) {
            followingList.remove(followingID);
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
            return true;
        }
        return false;
    }
    //add writeData() method
    public boolean deleteBlock(String blockedID) {
        if(blockedList.contains(blockedID)){
            blockedList.remove(blockedID);
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
        return true;
    }
    //add writeData() method
    public void createChat(ArrayList<String> recipient_id) {
        Chat newChat = new Chat(recipient_id);
        chatIDList.add(newChat.getChatID());
    }
    //add writeData() method
    public boolean deleteChat(String chat_id) {
        chatIDList.remove(chat_id);
        return false;
    }

    public int getAccountType(){
        return this.accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getPassWord() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean findUser(String userID) {
        try (BufferedReader br = new BufferedReader(new FileReader(userIDinfo))) {
            String line = br.readLine();
            if (line.substring(0, 6).equals(userID)) {
                return true;
            }
            while (line != null){
                line = br.readLine();
                if (line.substring(0, 6).equals(userID)) {
                    return true;
                }
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
        try(PrintWriter pw = new PrintWriter(new FileWriter(userIDList))){
            pw.println(username + ";" + password + ";" + userIDparameter);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
