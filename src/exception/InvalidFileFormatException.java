package exception;

/**
 * Exception thrown when a file format does not match the expected format.
 * This exception is used to signal format errors in files processed by the social media application.
 *
 * @author Connor Pugliese
 * @version 11/01/2024
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