import java.util.*;
/**
 * Social Media App - Message Interface
 *
 * Status: Complete
 *
 * @author soleil pham
 *
 * @version 11/02/2024
 *
 */
public interface MessageInterface {

    public String getAuthorID();
    public int getMessageType();
    public String getMessage();
    public boolean setMessage(String messageText);

}
