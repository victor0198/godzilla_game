package student.examples.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler extends Thread{
	
	private ServerSocket serverSocket;
	private Map<InetAddress, Socket> clients = new ConcurrentHashMap<>();
	
	public ConnectionHandler(ServerSocket serverSocket, Map<InetAddress, Socket> clients) {
		super();
		this.serverSocket = serverSocket;
		this.clients = clients;
	}

	@Override
	public void run(){
		// connection loop
		while (true) {
			Socket clientSocket;
			try {
				synchronized (clients) {
					clientSocket = serverSocket.accept(); // avoid sync
					serverSocket.setSoTimeout(100);
//
					System.out.println("Client joined");
					clients.put(clientSocket.getInetAddress(), clientSocket);
				}
				
				// break;

			} catch (SocketTimeoutException s){
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
