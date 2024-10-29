import java.util.ArrayList;

public interface ChatInterface {

    public ArrayList<String> getMessages();
    public String getChatID();
    public ArrayList<Chat> getActiveChats();
    public String toString();

    
}