import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    private static String FILE_DIRECTORY = "downloads/";

    public static void main(String[] args) throws IOException {

        File dir = new File(FILE_DIRECTORY);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.delete())
                    System.out.println("File " + file.getName() + " deleted");
            }
        }

        //UDP communication
        List<String> arguments = Arrays.asList(args);
        String serverIP = arguments.get(0);
        List<Integer> sequence = arguments.subList(1, arguments.size()).stream().map(Integer::parseInt).collect(Collectors.toList());

        InetAddress address = InetAddress.getByName(serverIP);

        String message = "knock knock";
        byte[] queryBuff = message.getBytes();

        DatagramSocket socket = new DatagramSocket();

        for (Integer serverPort : sequence) {
            DatagramPacket query = new DatagramPacket(queryBuff, queryBuff.length, address, serverPort);
            socket.send(query);
            System.out.println("Sent query to: " + address.toString() + " " + serverPort);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        byte[] buff = new byte[UDP.MAX_DATAGRAM_SIZE];
        DatagramPacket packet = new DatagramPacket(buff, buff.length);
        socket.receive(packet);

        int serverPort = ByteBuffer.wrap(packet.getData()).getInt();

        System.out.println("Received port number: " + serverPort);
        socket.close();
        // End of UDP phase

        // TCP communication
        try (Socket tcpSocket = new Socket(address, serverPort);
             InputStream is = tcpSocket.getInputStream();
        ) {
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String fileName = in.readLine();
            int fileLength = Integer.parseInt(in.readLine());

            System.out.println(FILE_DIRECTORY + fileName + " " + fileLength);

            byte[] byteArray = new byte[fileLength];
            is.read(byteArray, 0, byteArray.length);

            File file = new File(FILE_DIRECTORY + fileName);
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                bos.write(byteArray, 0, byteArray.length);
                bos.flush();
            }
        }

    }

}