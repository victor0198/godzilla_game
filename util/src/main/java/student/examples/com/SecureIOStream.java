package student.examples.com;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class SecureIOStream extends IOStream{

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public SecureIOStream(
            BufferedInputStream bis,
            BufferedOutputStream bos,
            PublicKey pbk,
            PrivateKey prk) {
        super(bis, bos);
        publicKey = pbk;
        privateKey = prk;
    }


    @Override
    public void send(int value) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");// Step 7: Initialize the Cipher object
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] msg = new byte[]{(byte) value};
            cipher.update(msg);
            byte[] ciphertext = cipher.doFinal();
            super.sendBytes(ciphertext);
        }catch (Exception e){

        }
    }

    @Override
    public int receive() throws IOException {
        byte[] bytes = super.receiveBytes();
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipher.update(bytes);
            byte[] decrypted = cipher.doFinal();

            return decrypted[0];
        }catch (Exception e){
            return -1;
        }
    }
}
