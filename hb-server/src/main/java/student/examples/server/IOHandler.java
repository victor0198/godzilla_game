package student.examples.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import student.examples.comm.Action;

public class IOHandler extends Thread {
	Map<InetAddress, Socket> clients = new ConcurrentHashMap<>();
	
	public IOHandler(Map<InetAddress, Socket> clients) {
		this.clients = clients;
	}
	
	public void run() {
			
		// IO loop
		while (true) {
			// iterating every client
			synchronized (clients) {
				
				
				clients.forEach((inetAddress, clientSocket) -> {
					
					try {
						BufferedInputStream bin = new BufferedInputStream(clientSocket.getInputStream());
						System.out.println("x="+bin.available());
						BufferedOutputStream bout = new BufferedOutputStream(clientSocket.getOutputStream());
						// blocks
						int in = bin.read();
						if (in >= 0 && in < Action.values().length) {
							Action action = Action.values()[in];
							switch (action) {
								case OK: {
									System.out.println("Server Received: OK");
									break;
								}
								
								case POKE: {
									System.out.println("Server Received: POKE");
									bout.write(Action.OK.ordinal());
									bout.flush();
									System.out.println("Server Sended: OK");
									break;
								}
							}
						} else {
							System.out.println("Server Received unknown packet: " + in);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
	
				});
			}
		}

	}

}
