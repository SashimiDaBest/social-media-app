import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
public class User implements UserInterface{
    private String userName;
    private String user_id; // (will be filename as well)
    private ArrayList<String> followers_ids;
    private ArrayList<String> following_ids;
    private ArrayList<String> blocked_ids;
    private ArrayList<String> chat_ids;
    private int userType;
    private String pathName; //path to total user & password
    private String passWord;
    private static ArrayList<String> UserArray; //add total user to an array - change method later

    private static final String totalUserFileName = "UserIDList.txt";

    HashMap<String, String> userPass = new HashMap<String, String>();

    public void setUsername(String username){
        this.userName = username;
    }

    public String getUsername(){
        return this.userName;
    }

    public void setPassword(String password){
        this.passWord = password;
    }

    public String getPassword(){
        return this.passWord;
    }

    public void setProfilePic(String pathname){
        this.pathName = pathname;
    }

    public String getProfilePic(){
        return this.pathName;
    }

    public boolean findUser(String id){
        try (BufferedReader br = new BufferedReader(new FileReader(pathName))) {
            String line = br.readLine();
            if (line.substring(0, 6).equals(id)) {
                return true;
            }
            while (line != null){
                line = br.readLine();
                if (line.substring(0, 6).equals(id)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean deleteFollower(String follower_id){
        if (findUser(follower_id)){
            for (int i = 0; i < followers_ids.size(); i++){
                if (followers_ids.get(i).equals(follower_id)){
                    followers_ids.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addFollower(String follower_id){
        //converse about this class and the possibility to be false on different occasions....
        if (findUser(follower_id)){
            for (int i = 0; i < followers_ids.size(); i++){
                if (followers_ids.get(i).equals(follower_id)) {
                    return true;
                }
            }
        } else {
            return false;
        }
        followers_ids.add(follower_id);
        return true;
    }

    public boolean addFollowing(String following_id){
        //address different types of false conditions?
        if (findUser(following_id) && !following_ids.contains(following_id) && !blocked_ids.contains(following_id)) {
            following_ids.add(following_id);
            return true;
        }
        return false;
    }

    public boolean deleteFollowing(String following_id){
        if (following_ids.contains(following_id)) {
            following_ids.remove(following_id);
            return true;
        }
        return false;
    }

    public boolean addBlock(String blocked_id){
        if(UserArray.contains(blocked_id) && !blocked_ids.contains(blocked_id)){
            blocked_ids.add(blocked_id);
            return true;
        }
        return false;
    }

    public boolean deleteBlock(String blocked_id){
        if(blocked_ids.contains(blocked_id)){
            blocked_ids.remove(blocked_id);
            return true;
        }
        return false;
    }

    public boolean addChat(String chat_id){
        chat_ids.add(chat_id);
        return true;
    }

    public void createChat(ArrayList<String> recipient_id){
        Chat newChat = new Chat(recipient_id);
        chat_ids.add(newChat.getChatID());
    }

    public boolean deleteChat(String chat_id){
        chat_ids.remove(chat_id);
        return false;
    }

    public void setAccountType(int accountType){
        this.userType = accountType;
    }

    public int getAccountType(){
        return this.userType;
    }

    //revise ---------
    public boolean hasLogin(String username, String password) {
        if (userPass.containsKey(username) && userPass.get(username).equals(password)) {
           return true;
        }
        return false;
    }

    public void createNewUser(String username, String password) {
        userPass.put(username, password);
    }

    public boolean sendText(String chat_id, String message, int type, String user_id, String username, int userType) throws NoChatFoundException {
        if (chat_ids.contains(chat_id)) {
            Chat existingChat = null;
            try {
                existingChat = new Chat(chat_id);
            } catch (InvalidFileFormatException e) {
                e.printStackTrace();
            }
            Message intendedMessage = new Message(user_id, type, message);
            existingChat.addMessage(intendedMessage);
            return true;
        }
        throw new NoChatFoundException("No chat found");
    }

    public ArrayList<String> getFollowers_ids() {
        return followers_ids;
    }
    public ArrayList<String> getFollowing_ids() {
        return following_ids;
    }
    public ArrayList<String> getBlocked_ids() {
        return blocked_ids;
    }
    public ArrayList<String> getChat_ids() {
        return chat_ids;
    }

     // for testing 
     
    public HashMap<String, String> getUserPass() {
        return userPass;
    }

    public ArrayList<String> getUserArray() {
        return UserArray;
    }

    public void removeFromUserArray(String id) {
        UserArray.remove(id);
    }
}
