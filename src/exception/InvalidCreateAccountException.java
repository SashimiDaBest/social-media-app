package exception;

/**
 * InvalidCreateAccountException
 * <p>
 * A custom exception that is thrown when there is an issue with account creation,
 * such as invalid input fields or failure to meet account creation requirements.
 * </p>
 *
 * <p>This exception extends {@link RuntimeException}, allowing it to be used
 * without being explicitly declared in method signatures.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * {@code
 * if (password.length() < 8) {
 *     throw new InvalidCreateAccountException("Password must be at least 8 characters long.");
 * }
 * }
 * </pre>
 *
 * @version 11/01/2024
 */
public class InvalidCreateAccountException extends RuntimeException {
    public InvalidCreateAccountException(String message) {
        super(message);
    }
}
