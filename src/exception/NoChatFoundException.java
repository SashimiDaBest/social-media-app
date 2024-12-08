package exception;

/**
 * Exception thrown when a specified chat ID cannot be found.
 * This is typically used to signal the absence of a chat in cases where one is expected,
 * enabling better error handling in the social media application.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * if (chat == null) {
 *     throw new NoChatFoundException("Chat with ID " + chatId + " not found.");
 * }
 * }</pre>
 *
 * @author Soleil Pham
 * @version 1.1 (2024-11-01)
 */
public class NoChatFoundException extends RuntimeException {
    /**
     * Constructs a new {@code NoChatFoundException} with the specified detail message.
     *
     * @param message the detail message explaining why the exception was thrown
     */
    public NoChatFoundException(String message) {
        super(message);
    }
}
