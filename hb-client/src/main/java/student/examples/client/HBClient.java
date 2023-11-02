package student.examples.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import student.examples.com.Action;
import student.examples.com.IOStream;
import student.examples.com.Logger;

public class HBClient{
	private final int port;
	private final String host;
	private final Logger logger = Logger.getInstance();
	private ConnectionHandler connectionHandler = null;

	private IOHandler

			ioHandler = null;

	public HBClient(int port, String host) {
		this.port = port;
		this.host = host;
		Socket clientSocket = null;
		try {
			clientSocket = new Socket();
			this.connectionHandler = new ConnectionHandler(clientSocket, host, port);
			logger.info("Client connected to the host: " + host + "  and port: " + port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ioHandler = new IOHandler(clientSocket);
	}

	public static void main(String[] args) {
		HBClient hbServer = new HBClient(7777, "localhost");

		hbServer.run();
	}

	public void run() {
		// FIXME
		connectionHandler.start();
		ioHandler.start();
		logger.info("Client started");
	}

}
