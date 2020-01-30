import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class Server {

    private List<Integer> sequence;
    private Set<Integer> ports;
    private Map<ClientKey, Integer> knockers = new HashMap<>();

    public Server(List<Integer> sequence, Set<Integer> ports) throws SocketException {
        this.sequence = sequence;
        this.ports = ports;
        initializeServer();
    }

    private void initializeServer() {
        Server server = this;
        for (Integer port : ports) {
            new Thread(() -> {
                try {
                    new MyServerSocket(server, port).listen();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void knock(int port, int clientPort, InetAddress clientAddress) throws IOException {
        System.out.println("received query at: " + port + ", from: " + clientAddress.toString() + " " + clientPort);
        ClientKey client = new ClientKey(clientPort, clientAddress);
        if (knockers.containsKey(client)) {
            Integer nextPortIndex = knockers.get(client);
            if (sequence.get(nextPortIndex) == port) {
                if (++nextPortIndex >= sequence.size()) {
                    respondTo(client);
                } else {
                    knockers.put(client, nextPortIndex);
                }
            } else {
                knockers.remove(client);
            }
        } else {
            if (port == sequence.get(0)) {
                if (sequence.size() == 1) {
                    respondTo(client);
                } else {
                    knockers.put(client, 1);
                }
            }
        }
    }

    private void respondTo(ClientKey client) throws IOException {
        System.out.println("Responding to: " + client);

        DatagramSocket socket = new DatagramSocket();

        TCPServer tcpServer = new TCPServer();
        tcpServer.listen();

        System.out.println(tcpServer.getPort());
        byte[] queryBuff = ByteBuffer.allocate(4).putInt(tcpServer.getPort()).array();

        DatagramPacket query = new DatagramPacket(queryBuff, queryBuff.length, client.getAddress(), client.getPort());
        socket.send(query);
    }

    public static void main(String[] args) {
        List<Integer> sequence = Arrays.stream(args).map(Integer::parseInt).collect(Collectors.toList());
        Set<Integer> ports = new HashSet<>(sequence);

        try {
            new Server(sequence, ports);
        } catch (SocketException e) {
            System.out.println("Could not set up the server");
        }
    }

}