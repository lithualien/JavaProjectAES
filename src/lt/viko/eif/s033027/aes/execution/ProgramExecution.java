package lt.viko.eif.s033027.aes.execution;

import lt.viko.eif.s033027.aes.crypting.Decrypt;
import lt.viko.eif.s033027.aes.crypting.Encrypt;
import lt.viko.eif.s033027.aes.crypting.ForBoth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProgramExecution {

    private Encrypt encrypt = new Encrypt();
    private Decrypt decrypt = new Decrypt();
    private ForBoth forBoth = new ForBoth();

    public boolean stop = false;

    private boolean zipRequired = false;
    private int counterOfFoleders;

    private List<String> folderNames = new ArrayList<>();

    public void checkIfFileForEncode(File file) {
        if(file.isFile() && checkForEncodeFileIndex(file)) {
            encrypt(file);
        }
        else { recursionToEncrypt(file); }
    }

    public void checkIfFileForDecode(File file) {
        if(file.isFile() && checkForDecodeFileIndex(file)) {
            decrypt(file);
        }
        else { recursionToDecrypt(file); }
    }

    private boolean checkForEncodeFileIndex(File file) {
        if (file.getName().lastIndexOf(".") != -1 && file.getName().lastIndexOf(".") != 0) {
            String temp = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if (!temp.equals("des") && !temp.equals("enc")) {
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkForDecodeFileIndex(File file) {
        if (file.getName().lastIndexOf(".") != -1 && file.getName().lastIndexOf(".") != 0) {
            if (file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("des")) {
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void encrypt(File file) {
        encrypt.setParamsForEncryption(file);
        encrypt.createIV(file);
        encrypt.setInFile(file);
        encrypt.setOutFile(file);
        encrypt.encryptFile();
        encrypt.setNewFile(file);
        encrypt.writeMD5HashToFile(file);
        encrypt.deleteFileAfterEncryption(file);
    }

    private void decrypt(File file) {
        if(new String(decrypt.readMD5Hash(file)).equals(new String(forBoth.getMD5(file)))) {
            decrypt.setParamsForDecryption(file);
            decrypt.setChipperForDecode(file);
            decrypt.setInFile(file);
            decrypt.setOutFile(file);
            decrypt.decryptFile();
            decrypt.deleteEverythingAfterDecryption(file);
        } else { System.out.println("Blogi hash"); }
    }

    private void recursionToEncrypt(File file) {
        try {
            File files[] = file.listFiles();
            for(File temp : files) {
                if(temp.isFile() && checkForEncodeFileIndex(temp)) {
                    encrypt(temp);
                }
                if(stop) {break;}

                if(temp.isDirectory()) {

                }
                recursionToEncrypt(temp);
            }
        }
        catch (Exception e) { }
    }

    private void recursionToDecrypt(File file) {
        try {
            File files[] = file.listFiles();
            for(File temp : files) {
                if(temp.isFile() && checkForDecodeFileIndex(temp)) {
                    decrypt(temp);
                }
                if(stop) {break;}
                recursionToDecrypt(temp);
            }
        }
        catch (Exception e) { }
    }

    private void getDir(File file)
    {
        File files[] = file.listFiles();
        for(File temp : files)
        {
            folderNames.add(temp.getAbsolutePath());
        }
    }
}
