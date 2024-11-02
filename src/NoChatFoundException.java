/**
 * Social Media App - NoChatFoundException
 *
 * Subclass of exception that throws error when no chatID is found
 *
 * Status: Complete
 *
 * @author soleil pham
 *
 * @version 11/01/2024
 *
 */
public class NoChatFoundException extends RuntimeException {
    public NoChatFoundException(String message) {
        super(message);
    }
}
