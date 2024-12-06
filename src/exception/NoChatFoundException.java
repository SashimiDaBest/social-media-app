package exception;

/**
 * Social Media App - NoChatFoundException
 * <p>
 * A custom exception that is thrown when a specified chat ID cannot be found.
 * This exception is used to signal the absence of a chat in cases where one is expected,
 * enabling better error handling within the social media application.
 * </p>
 *
 * @author Soleil Pham
 * @version 11/01/2024
 */
public class NoChatFoundException extends RuntimeException {
    /**
     * Constructs a new {@code NoChatFoundException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public NoChatFoundException(String message) {
        super(message);
    }
}
