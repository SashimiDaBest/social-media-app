import java.io.IOException;

/**
 * Interface for a simple server that defines the basic operations a server should perform.
 */
public interface ServerInterface {

    /**
     * Starts the server, listens for client connections, and handles them.
     *
     * @throws IOException If an I/O error occurs during communication
     */
    void start() throws IOException;

    /**
     * Stops the server by closing the server socket and releasing any resources used.
     *
     * @throws IOException If an I/O error occurs while closing the server socket
     */
    void stop() throws IOException;
}
