package student.examples.client;

import student.examples.com.Action;
import student.examples.com.IOStream;
import student.examples.com.Logger;

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
                IOStream ioStream = new IOStream(
                        new BufferedInputStream(socket.getInputStream()),
                        new BufferedOutputStream(socket.getOutputStream())
                );
                //IO loop
                while (true) {
                    Thread.sleep(1000);
                    logger.info("Client Sended: POKE");
                    ioStream.send(Action.POKE.ordinal());

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
