/**
 * Social Media App - InvalidFileFormatException
 *
 * Subclass of exception that throws error when file format is not as expected
 *
 * Status: Complete
 *
 * @author connor pugliese
 *
 * @version 11/01/2024
 *
 */
public class InvalidFileFormatException extends Exception {
    public InvalidFileFormatException(String message) {
        super(message);
    }
}