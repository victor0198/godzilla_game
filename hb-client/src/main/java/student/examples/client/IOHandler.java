package student.examples.client;

import student.examples.com.Action;
import student.examples.com.IOStream;
import student.examples.com.Logger;
import student.examples.com.SecureIOStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

public class IOHandler extends Thread {
    private Socket socket;

    private final Logger logger = Logger.getInstance();

    public IOHandler(Socket socket) {
        this.socket = socket;
    }

    public void run(){
        while (true) {

            try {
                SecureIOStream ioStream = new SecureIOStream(
                        new BufferedInputStream(socket.getInputStream()),
                        new BufferedOutputStream(socket.getOutputStream())
                );
                //IO loop
                while (true) {
                    Thread.sleep(1000);
                    ioStream.send(Action.POKE.ordinal());

                    logger.info("Client Sended: POKE");

                    //blocks
                    int in = ioStream.receive();
                    if (in >= 0 && in < Action.values().length) {
                        Action action = Action.values()[in];
                        switch (action) {
                            case OK: {
                                logger.info("Client Received: OK");
                                break;
                            }
                            case POKE: {
                                // ignore POKE packet as we expect OK answer
                                break;
                            }
                        }
                    } else {
                        logger.warning("Client Received unknown packet: " + in);
                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }
}
