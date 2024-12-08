package exception;

/**
 * Exception thrown when there is an issue with account creation,
 * such as invalid input fields or failure to meet account creation requirements.
 *
 * <p>This exception extends {@link RuntimeException}, so it does not need to be explicitly
 * declared in method signatures. It is typically used for critical validation failures
 * during account creation.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * if (password.length() < 8) {
 *     throw new InvalidCreateAccountException("Password must be at least 8 characters long.");
 * }
 * }</pre>
 *
 * @version 11/01/2024
 */
public class InvalidCreateAccountException extends RuntimeException {
    /**
     * Constructs a new InvalidCreateAccountException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidCreateAccountException(String message) {
        super(message);
    }
}
