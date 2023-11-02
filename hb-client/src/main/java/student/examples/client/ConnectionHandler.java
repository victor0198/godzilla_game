package student.examples.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    private Socket socket;
    private String host;
    private int port;

    public ConnectionHandler(Socket socket, String host, int port) {
        this.socket = socket;
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
