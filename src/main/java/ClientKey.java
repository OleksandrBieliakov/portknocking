import java.net.InetAddress;
import java.util.Objects;

public class ClientKey {
    private int port;
    private InetAddress address;

    ClientKey(int port, InetAddress address) {
        this.port = port;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientKey)) return false;
        ClientKey client = (ClientKey) o;
        return port == client.port &&
                Objects.equals(address, client.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, address);
    }

    @Override
    public String toString() {
        return "Client{" +
                "port=" + port +
                ", address=" + address +
                '}';
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }
}
