package lt.viko.eif.s033027.aes.crypting;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ForBoth {

    public final String PASSWORD = "123456";

    public void fileEncryptionAndDecryption(FileInputStream inFile, FileOutputStream outFile, Cipher cipher) {
        try {
            byte[] input = new byte[64];
            int bytesRead;

            while ((bytesRead = inFile.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }

            byte[]output = cipher.doFinal();
            if (output != null)
                outFile.write(output);

            inFile.close();
            outFile.flush();
            outFile.close();
        }
        catch(IOException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    public byte[] getMD5(File newFile) {
        byte[] hash = null;
        try {
            byte[] b = Files.readAllBytes(Paths.get(newFile.toString()));
            hash = MessageDigest.getInstance("MD5").digest(b);
        }
        catch(IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
