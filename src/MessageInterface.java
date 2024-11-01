import java.util.*;

public interface MessageInterface {
    public String convertToMorseCode(String message);

    public String getChatID();
    public int getType();
    public String getMessage();

}
