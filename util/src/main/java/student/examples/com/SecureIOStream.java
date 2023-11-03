package student.examples.com;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SecureIOStream extends IOStream {
    public static final String DESTINATION_DIR = "C:\\Users\\User\\Desktop\\encrypted\\";

    private SecretKey key;
    private final Logger logger = Logger.getInstance();

    public SecureIOStream(
            BufferedInputStream bis,
            BufferedOutputStream bos) {
        super(bis, bos);
        //        generateAndSaveKey();
        Path keyPath = Paths.get(DESTINATION_DIR+"desk.dsk");
        byte[] encodedKey;
        try {
            encodedKey = Files.readAllBytes(keyPath);
            SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "DES");
            this.key = originalKey;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void send(int value) {

        byte[] encrypted;
        try {
            encrypted = encrypt((byte) value);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            super.sendBytes(encrypted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int receive() throws IOException {
        byte[] bytes = super.receiveBytes();
        try {

//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.DECRYPT_MODE, privateKey);
//            cipher.update(bytes);
//            byte[] decrypted = cipher.doFinal();

            //decipher test
            Cipher dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, key);

            byte[] dec = dcipher.doFinal(bytes);
            System.out.print("\nenc["+bytes.length+"]:");
            for(int i=0;i<bytes.length;i++){
                System.out.print(bytes[i]+" ");
            }
            System.out.println(", received action:"+dec[0]);


            return dec[0];
        } catch (Exception e) {
            return -1;
        }
    }

    public byte[] encrypt(byte msg) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException {

        Cipher eCipher = Cipher.getInstance("DES");
        eCipher.init(Cipher.ENCRYPT_MODE, key);

        try {
            byte[] byteMsg = new byte[]{msg};

            byte[] enc = eCipher.doFinal(byteMsg);
            System.out.print("\nsending action:"+byteMsg[0]+", encrypted bytes["+enc.length+"]:");
            for(int i=0;i<enc.length;i++){
                System.out.print(enc[i]+" ");
            }
            System.out.println();


            return enc;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return new byte[]{(byte)126};

    }

    private void generateAndSaveKey() throws NoSuchAlgorithmException, IOException {
        System.out.print("Key name:");

        Scanner scanner = new Scanner(System.in);
        String keyName;
        try {
            keyName = scanner.nextLine();

            this.key = KeyGenerator.getInstance("DES").generateKey();


            byte[] byte_pubkey = this.key.getEncoded();
            writeToFile(keyName+".dsk", byte_pubkey);

        }
        catch (InputMismatchException ex){
            System.out.println("Please enter a word..");
            scanner.next();
        }

    }

    public static void writeToFile(String fileName, byte[] key) throws IOException {
        File outputFile = new File(DESTINATION_DIR+fileName);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(key);
        }
    }

}
