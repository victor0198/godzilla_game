package student.examples.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import student.examples.com.Action;
import student.examples.com.IOStream;

public class HBClient {

	public static void main(String[] args) {
		int port = 7777;
		String host = "localhost";
		// TODO separate to reconnect
		Socket socket;
		try {
			socket = new Socket(InetAddress.getByName(host), port);
			System.out.println("Client connectin to the host: " + host + "  and port: " + port);
			
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		
		try {
			IOStream ioStream = new IOStream(
					new BufferedInputStream(socket.getInputStream()),
					new BufferedOutputStream(socket.getOutputStream())
			);
			//IO loop
			while (true)
			{
				Thread.sleep(1000);
				System.out.println("Client Sended: POKE");
				ioStream.send(Action.POKE.ordinal());
				
				//blocks
				int in = ioStream.receive();
				if (in >= 0 && in < Action.values().length) {
					Action action = Action.values()[in];
					switch (action) {
						case OK: {
							System.out.println("Client Received: OK");
							break;
						}
						case POKE: {
							// ignore POKE packet as we expect OK answer
							break;
						}
					}
				} else {
					System.out.println("Client Received unknown packet: " + in);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		


	}

}
