import java.io.IOException;
import java.util.ArrayList;
import object.Chat;
import object.User;

/**
 * Interface representing the basic operations that a simple server should provide. 
 * This interface defines methods to start and stop the server, as well as retrieve 
 * lists of users and chats handled by the server. Any class implementing this
 * @author Derek Mctume
 * @version 1.0
 * interface must provide concrete implementations of these methods.
 */
public interface ServerInterface {

    /**
     * Starts the server by initializing necessary resources, listening for client 
     * connections, and handling those connections. The server should be able to accept 
     * incoming requests and perform any necessary processing, such as communication 
     * with clients.
     * 
     * @throws IOException If an I/O error occurs while starting or listening for client connections.
     */
    void start() throws IOException;

    /**
     * Stops the server by closing the server socket and releasing any resources 
     * associated with the server. This method ensures that the server can safely 
     * shut down and that resources such as open sockets or file handles are properly 
     * released.
     * 
     * @throws IOException If an I/O error occurs while closing the server socket.
     */
    void stop() throws IOException;

    /**
     * Retrieves the list of {@link User} objects managed by the server. The list of users 
     * represents all clients or entities that are currently registered or interacting with 
     * the server.
     * 
     * @return An {@link ArrayList} of {@link User} objects.
     */
    ArrayList<User> getUsers();

    /**
     * Retrieves the list of {@link Chat} objects managed by the server. The list of chats 
     * represents all active or historical chat sessions that the server is managing.
     * 
     * @return An {@link ArrayList} of {@link Chat} objects.
     */
    ArrayList<Chat> getChats();
}
