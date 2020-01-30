import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerThread extends Thread {

    private static String FILE_NAME = "datafile.txt";
    private static String FILE_PATH = "datafile.txt";

    private ServerSocket server;
    private Socket socket;

    public TCPServerThread(ServerSocket server) {
        super();
        this.server = server;
    }

    public void communicate() throws IOException {
        File file = new File(FILE_PATH);
        long fileLength = file.length();
        System.out.println(FILE_NAME + " " + fileLength);

        try (OutputStream os = socket.getOutputStream()) {

            PrintWriter out = new PrintWriter(os, true);

            out.println(FILE_NAME);
            out.println(fileLength);

            byte[] byteArray = new byte[(int) fileLength];
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                int read = bis.read(byteArray, 0, byteArray.length);
                if (read != fileLength)
                    throw new IOException("Data length mismatch");
            }
            os.write(byteArray, 0, byteArray.length);
            os.flush();
        }
    }

    public void run() {
        try {
            socket = server.accept();
            communicate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}