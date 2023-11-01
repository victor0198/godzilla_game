package student.examples.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HBServer {
	private final int backlog;
	private final int port;
	private final String host;
	
	// usually this is injected
	private ConnectionHandler connectionHandler = null;
	
	private IOHandler ioHandler = null;
	
	private Map<InetAddress, Socket> clients;

	public HBServer(int port, String host, int backlog) {
		this.backlog = backlog;
		this.host = host;
		this.port = port;
		clients = new ConcurrentHashMap<>();
		try {
			this.connectionHandler = new ConnectionHandler(new ServerSocket(port, backlog, InetAddress.getByName(host)), clients);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ioHandler = new IOHandler(clients);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		//TODO add a logger
		HBServer hbServer = new HBServer(7777, "localhost", 100);
		
		hbServer.run();
//		System.out.println("Server started on port:"+port);
	}
	
	public void run() {
		// FIXME
		connectionHandler.start();
		ioHandler.start();
	}

}
