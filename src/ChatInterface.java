import java.util.*;
/**
 * Social Media App - Chat Interface
 *
 *
 * Status: Complete
 *
 * @author soleil pham
 *
 * @version 11/01/2024
 *
 */
public interface ChatInterface {
    public void addMessage (Message message);
    public void deleteMessage ();

    public String getChatID();
    public ArrayList<Message> getMessages();
    public ArrayList<String> getRecipientID();
    public int getCounter();
    public void setRecipientID(ArrayList<String> recipientID);

}
