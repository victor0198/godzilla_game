package student.examples.com;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class IOStream {
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;

    public IOStream(BufferedInputStream bufferedInputStream, BufferedOutputStream bufferedOutputStream) {
        this.bufferedInputStream = bufferedInputStream;
        this.bufferedOutputStream = bufferedOutputStream;
    }

    public void send(int value) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        bufferedOutputStream.write(value);
        bufferedOutputStream.flush();
    }

    public void sendBytes(byte[] value) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        bufferedOutputStream.write(value);
        bufferedOutputStream.flush();
    }

    public int receive() throws IOException {
        if(bufferedInputStream.available() > 0){
            return bufferedInputStream.read();
        }
        return -1;

    }

    public byte[] receiveBytes() throws IOException {
        if(bufferedInputStream.available() > 0){
            byte[] all = bufferedInputStream.readNBytes(8);
            return all;
        }
        return new byte[]{(byte)-1};

    }
}
