import java.util.*;

public interface ChatInterface {
    public void addMessage (Message message);
    public void deleteMessage (Message message);

    public String getChatID();
    public ArrayList<String> getMessages();
}
