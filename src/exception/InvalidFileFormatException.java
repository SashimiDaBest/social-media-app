package exception;

/**
 * InvalidFileFormatException
 * <p>
 * A custom exception that is thrown when a file format does not match the expected format.
 * This exception is used to signal format errors in files used by the social media application.
 * <p>
 * Status: Complete
 * </p>
 *
 * @author Connor Pugliese
 * @version 11/01/2024
 * @since 1.0
 */
public class InvalidFileFormatException extends Exception {
    /**
     * Constructs a new {@code InvalidFileFormatException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidFileFormatException(String message) {
        super(message);
    }
}