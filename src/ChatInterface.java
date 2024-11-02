import java.util.*;
/**
 * Social Media App - Chat Interface
 *
 * Status: Complete
 *
 * @author soleil pham
 *
 * @version 11/02/2024
 *
 */
public interface ChatInterface {
    public void writeData();

    public String getChatID();
    public String createChatID();

    public ArrayList<String> getMemberList();
    public void setMemberList(ArrayList<String> memberList);

    public ArrayList<Message> getMessageList();
    public void addMessage(Message message);
    public void editMessage(String messageText, String authorID);
    public void deleteMessage(String authorID);

    public int getCounter();
}
