public interface UserInterface {

    public String getUsername();
    public void setUsername(String username);

    public String getPassword();
    public void setPassword(String password);

    public String getProfilePic();
    public void setProfilePic(String pathname);

    public boolean addFollower(String follower_id);
    public boolean deleteFollower(String follower_id);

    public boolean addFollowing(String following_id);
    public boolean deleteFollowing(String following_id);

    public boolean addBlock(String blocked_id);
    public boolean deleteBlock(String blocked_id);

    public boolean addChat(String chat_id);
    public boolean deleteChat(String chat_id);

    public boolean setAccountType(int accountType);

    public boolean userDoesExist(String idToBeSearched);

    public boolean hasLogin(String username, String password);
    public void createNewUser(String username, String password);
    public boolean sendText(String chat_id, String message, int type, String user_id, String username, int userType);
    public boolean isFollower(String follower_id);

    public boolean setProfileImage(String photoFileName);

    //IDK WHAT TO DO WITH THIS YET
    public void logIn();
    public void logOut();

}
