package student.examples.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import student.examples.com.Action;
import student.examples.com.IOStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
						IOStream ioStream = new IOStream(
								new BufferedInputStream(clientSocket.getInputStream()),
								new BufferedOutputStream(clientSocket.getOutputStream())
						);
						// blocks
						int in = ioStream.receive();
						if (in >= 0 && in < Action.values().length) {
							Action action = Action.values()[in];
							switch (action) {
								case OK: {
									System.out.println("Server Received: OK");
									break;
								}
								
								case POKE: {
									System.out.println("Server Received: POKE");
									ioStream.send(Action.OK.ordinal());
									System.out.println("Server Sended: OK");
									break;
								}
							}
						} else {
//							System.out.println("Server Received unknown packet: " + in);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (IllegalBlockSizeException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (BadPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidKeyException e) {
                        throw new RuntimeException(e);
                    }

                });
			}
		}

	}

}
