import java.util.ArrayList;

public interface UserInterface {

    public void setUsername(String username);

    public String getUsername();

    public String getUserID();

    public String createUserID();

    public void setProfilePic(String photoPathway);

    public String getProfilePic();

    public ArrayList<String> getFollowerList();

    public boolean deleteFollower(String follower_id);

    public boolean addFollower(String followerID);

    public ArrayList<String> getFollowingList();

    public boolean deleteFollowing(String following_id);

    public boolean addFollowing(String followingID);

    public ArrayList<String> getBlockedList();

    public boolean deleteBlock(String blockedID);

    public boolean addBlock(String blockedID);

    public ArrayList<String> getChatIDList();

    public boolean addChat(String chat_id);

    public void createChat(ArrayList<String> recipient_id);

    public boolean deleteChat(String chat_id);

    public int getAccountType();

    public void setAccountType(int accountType);

    public String getPassWord();

    public void setPassword(String password);

    public boolean findUser(String userID);

    public boolean sendText(String chatID, String message, int type, String userID, String username, int userType) throws NoChatFoundException;

    public boolean hasLogin(String username, String password);

    public void createNewUser(String username, String password, String userIDparameter);

    public boolean userNameValidation(String username);


//    public boolean isFollower(String follower_id, String following_id);

    //IDK WHAT TO DO WITH THIS YET
//    public void logIn();
//    public void logOut();


}
