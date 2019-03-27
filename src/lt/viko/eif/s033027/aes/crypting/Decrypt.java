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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Decrypt {

    private ForBoth forBoth = new ForBoth();
    private SecretKey secret;
    private Cipher cipher;

    private FileOutputStream outFile;
    private FileInputStream inFile;

    public void setParamsForDecryption(File file) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(forBoth.PASSWORD.toCharArray(), readSalt(file), 65536, 256);
            SecretKey tmp = factory.generateSecret(keySpec);
            secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private byte[] readSalt(File file) {
        byte[] salt = null;
        try {
            FileInputStream saltFis = new FileInputStream(file.getParent() + "\\" + file.getName().replaceFirst(".des", "") + "Salt.enc");
            salt = new byte[8];
            saltFis.read(salt);
            saltFis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return salt;
    }

    public void setChipperForDecode(File file) {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(readIV(file)));
        }
        catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private byte[] readIV(File file) {
        byte[] iv = null;
        try {
            FileInputStream ivFis = new FileInputStream(file.getParent() + "\\" + file.getName().replaceFirst(".des", "") + "Iv.enc");
            iv = new byte[16];
            ivFis.read(iv);
            ivFis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return iv;
    }

    public byte[] readMD5Hash(File file) {
        byte[] md5Hash = null;
        try {
            FileInputStream ivFis = new FileInputStream(file.getParent() + "\\" + file.getName().replaceFirst(".des", "") + "Md5.enc");
            md5Hash = new byte[16];
            ivFis.read(md5Hash);
            ivFis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return md5Hash;
    }

    public void setInFile(File file) {
        try {
            inFile = new FileInputStream(file.getParent() + "\\" + file.getName());

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setOutFile(File file) {
        try {
            outFile = new FileOutputStream(file.getParent() + "\\" + file.getName().replaceFirst(".des", ""));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void decryptFile() {
        forBoth.fileEncryptionAndDecryption(inFile, outFile, cipher);
    }

    public void deleteEverythingAfterDecryption(File file) {
        file.delete();
        File temp = new File(file.getParent() + "\\" + file.getName().replaceFirst(".des", "") + "Md5.enc");
        temp.delete();
        temp = new File(file.getParent() + "\\" + file.getName().replaceFirst(".des", "") + "Salt.enc");
        temp.delete();
        temp = new File(file.getParent() + "\\" + file.getName().replaceFirst(".des", "") + "Iv.enc");
        temp.delete();
    }

}
