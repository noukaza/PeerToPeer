package client.peer;

public class Peer {
    private int port;
    private String address;

    public Peer(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }
}
