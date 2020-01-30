import java.io.*;
import java.net.*;

public class TCPServer {

    private ServerSocket server;

    TCPServer() throws IOException {
        server = new ServerSocket(0);
    }

    public int getPort() {
        return server.getLocalPort();
    }

    public void listen() {
        new TCPServerThread(server).start();
    }

}