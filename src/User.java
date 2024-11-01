import java.util.*;
public class User implements UserInterface{
    private String userName;
    private String passWord;
    private String pathName;
    private static ArrayList<String> UserArray; 
    private String user_id (will be filename as well);
    private ArrayList<String> followers_ids;
    private ArrayList<String> following_ids;
    private ArrayList<String> blocked_ids;
    private ArrayList<String> chat_ids;
    private String photoFileName;
    private int userType;
    static final String totalUserFileName;
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
    public boolean deleteFollower(String follower_id){
        if(followers_ids.contains(follower_id)){
            followers_ids.remove(follower_id);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean addFollower(String follower_id){
        //converse about this class and the possibility to be false on different occasions....
        if(!followers_ids.contains(follower_id) && UserArray.contains(follower_id) && !blocked_ids.contains(follower_id)){
            followers_ids.add(follower_id);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean addFollowing(String following_id){
        //address different types of false conditions?
        if(UserArray.contains(following_id) && !following_ids.contains(following_id) && !blocked_ids.contains(following_id)){
            following_ids.add(following_id);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean deleteFollowing(String following_id){
        if(following_ids.contains(following_id)){
            following_ids.remove(following_id);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean addBlock(String blocked_id){
        if(UserArray.contains(blocked_id) && !blocked_ids.contains(blocked_id)){
            blocked_ids.add(blocked_id);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean deleteBlock(String blocked_id){
        if(blocked_ids.contains(blocked_id)){
            blocked_ids.remove(blocked_id);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean addChat(String chat_id){
        chat_ids.add(chat_id);

    }
    public boolean deleteChat(String chat_id){
        chat_ids.remove(chat_id);
    }
    public boolean setAccountType(int accountType){
        this.userType = accountType;
    }
    public boolean userDoesExist(String idToBeSearched){
        if(UserArray.contains(idToBeSearched)){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean hasLogin(String username, String password){
        if(userPass.containsKey(username)){
            if(userPass.get(username).equals(password)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public void createNewUser(String username, String password){
        userPass.put(username, password);
    }

    public boolean sendText(String chat_id, String message, int type, String user_id, String username, int userType){
        
    }




}
