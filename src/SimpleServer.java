import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Social Media App - Simple Server
 *
 * Server class with following methods and functionality
 * 1. start() wait and accept connection
 * 2. stop() close down the server
 *
 * Status: Incomplete
 *
 * @author soleil pham
 *
 * @version 11/01/2024
 *
 */

public class SimpleServer {
    private ServerSocket serverSocket;
//    private ExecutorService executorService;

    public SimpleServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("Server listening on port " + serverSocket.getLocalPort());
        while (true) {
            try {
                Socket socket = serverSocket.accept();
//                executorService.submit(new ClientHandler(clientSocket));
            } catch (IOException e) {
                System.out.println("Error accepting connection" + e.getMessage());
            }
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
//        executorService.shutdown();
    }
}
