package student.examples.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import student.examples.comm.Action;

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
			BufferedInputStream bin = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream bout = new BufferedOutputStream(socket.getOutputStream());
			
			//IO loop
			while (true)
			{
				Thread.sleep(1000);
				System.out.println("Client Sended: POKE");
				bout.write(Action.POKE.ordinal());
				bout.flush();
				
				//blocks
				int in = bin.read();
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