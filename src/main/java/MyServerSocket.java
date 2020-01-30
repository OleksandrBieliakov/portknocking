import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MyServerSocket {

    private Server server;
    private int port;
    private DatagramSocket socket;

    MyServerSocket(Server server, Integer port) throws SocketException {
        this.port = port;
        this.server = server;
        socket = new DatagramSocket(port);
        System.out.println("creating " + port);
    }

    public void listen() {
        while (true) {
            try {
                byte[] buff = new byte[UDP.MAX_DATAGRAM_SIZE];
                final DatagramPacket datagram = new DatagramPacket(buff, buff.length);
                socket.receive(datagram);

                new Thread(() -> {
                    int clientPort = datagram.getPort();
                    InetAddress clientAddress = datagram.getAddress();
                    try {
                        server.knock(port, clientPort, clientAddress);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
