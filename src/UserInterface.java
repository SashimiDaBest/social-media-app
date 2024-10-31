public interface UserInterface {

    public boolean userDoesExist(String idToBeSearched);
    public boolean addFollower(String follower_id);
    public boolean deleteFollower(String follower_id);
    public boolean blockFollower(String follower_id);
    public boolean hasLogin(String username, String password);
    public void createNewUser(String username, String password);
    public boolean sendText(String chat_id, String message, int type, String user_id, String username, int userType);
    public boolean isFollower(String follower_id);
    public boolean setProfileImage(String photoFileName);

}
