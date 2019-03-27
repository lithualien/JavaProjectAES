package lt.viko.eif.s033027.aes.crypting;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

public class Encrypt {

    private Cipher cipher;
    private AlgorithmParameters params;

    private ForBoth forBoth = new ForBoth();
    private FileOutputStream outFile;
    private FileInputStream inFile;
    private File newFile;

    public void setParamsForEncryption(File file) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(forBoth.PASSWORD.toCharArray(), createSalt(file), 65536, 256);
            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            params = cipher.getParameters();
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private byte[] createSalt(File file) {
        byte[] salt = null;
        try {
            salt = new byte[8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);
            FileOutputStream saltOutFile = new FileOutputStream(file.getAbsolutePath() + "Salt.enc");
            saltOutFile.write(salt);
            saltOutFile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return salt;
    }

    public void createIV(File file) {
        try {
            FileOutputStream ivOutFile = new FileOutputStream(file.getAbsolutePath() + "Iv.enc");
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            ivOutFile.write(iv);
            ivOutFile.close();
        }
        catch (IOException | InvalidParameterSpecException e){
            e.printStackTrace();
        }
    }

    public void setInFile(File file) {
        try {
            inFile = new FileInputStream(file.getParent() + "\\" + file.getName());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOutFile(File file) {
        try {
            outFile = new FileOutputStream(file.getParent() + "\\" + file.getName() + ".des");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encryptFile() {
        forBoth.fileEncryptionAndDecryption(inFile, outFile, cipher);
    }

    public void setNewFile(File file) {
        newFile = new File( file.getAbsolutePath() + ".des");
    }

    public void writeMD5HashToFile(File file) {
        try {
            FileOutputStream md5HashWrite = new FileOutputStream(file.getAbsolutePath() + "Md5.enc");
            md5HashWrite.write(forBoth.getMD5(newFile));
            md5HashWrite.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFileAfterEncryption(File file) {
        file.delete();
    }
}
