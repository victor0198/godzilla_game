package student.examples.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import student.examples.com.Action;
import student.examples.com.IOStream;
import student.examples.com.Logger;
import student.examples.com.SecureIOStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class IOHandler extends Thread {
	Map<InetAddress, Socket> clients = new ConcurrentHashMap<>();
	
	public IOHandler(Map<InetAddress, Socket> clients) {
		this.clients = clients;
	}

	private final Logger logger = Logger.getInstance();
	
	public void run() {
			
		// IO loop
		while (true) {
			// iterating every client
			synchronized (clients) {

				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				clients.forEach((inetAddress, clientSocket) -> {
//					System.out.println("working with"+clientSocket.toString());
					try {
						SecureIOStream ioStream = new SecureIOStream(
								new BufferedInputStream(clientSocket.getInputStream()),
								new BufferedOutputStream(clientSocket.getOutputStream())
						);
						// blocks
						int in = ioStream.receive();

						if (in >= 0 && in < Action.values().length) {
							Action action = Action.values()[in];
							switch (action) {
								case OK: {
									logger.info("Server Received: OK");
									break;
								}
								
								case POKE: {
									logger.info("Server Received: POKE");
									ioStream.send(Action.OK.ordinal());
									logger.info("Server Sended: OK");
									break;
								}
							}
						} else {
//							logger.warning("Input from client not available: " + in);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				});
			}
		}

	}

}
