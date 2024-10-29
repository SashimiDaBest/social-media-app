public interface UserInterface {

   public void setUsername(String username);
   public String getUsername();
   
   public String getPassword();
   public void setPassword(String password);

   // This assumes that BufferedImage is being used:
    public String getProfilePic();
    public void setProfilePic(String pathname);

   public void logIn();
   public void logOut();
}
