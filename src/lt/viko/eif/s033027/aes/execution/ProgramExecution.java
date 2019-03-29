package lt.viko.eif.s033027.aes.execution;

import lt.viko.eif.s033027.aes.crypting.Decrypt;
import lt.viko.eif.s033027.aes.crypting.Encrypt;
import lt.viko.eif.s033027.aes.crypting.ForBoth;
import lt.viko.eif.s033027.aes.zipping.UnzipFile;
import lt.viko.eif.s033027.aes.zipping.ZipFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProgramExecution {

    private Encrypt encrypt = new Encrypt();
    private Decrypt decrypt = new Decrypt();
    private ForBoth forBoth = new ForBoth();
    private ZipFile zipFile = new ZipFile();
    private UnzipFile unzipFile = new UnzipFile();


    public boolean stop = false;
    private File newestFile;
    public boolean zipRequired = false;

    private List<String> folderNames = new ArrayList<>();

    public boolean running = true;
    public int counterOfFolders;
    public int currentFolder;

    public void checkIfFileForEncode(File file) {
        if (file.isFile() && checkForEncodeFileIndex(file)) {
            encrypt(file);
            newestFile = new File(file + ".des");
        }
        else {
            getDir(file);
            recursionToEncrypt(file);
            if(zipRequired) {
                zipFile.setUpForZipping(file);
                encrypt(new File(file.getAbsolutePath() + ".zip"));
                newestFile = new File(file + ".zip.des");
            }
        }
        running = false;
    }

    public void checkIfFileForDecode(File file) {
        if (file.isFile() && checkForDecodeFileIndex(file)) {
            File newFile = new File(file.getParent() + "\\" + file.getName().replaceFirst(".des",""));
            decrypt(file);
            if(newFile.getName().substring(newFile.getName().lastIndexOf(".") + 1).equals("zip")) {
                unzipFile.unZip(newFile);

                newFile.delete();
                newestFile =  new File(newFile.getParent() + "\\" + newFile.getName().replaceFirst(".zip",""));
                getDir(newestFile);
                recursionToDecrypt(newestFile);
            }
        }
        else {
            recursionToDecrypt(file);
        }
        running = false;
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
        }
        else {
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
        }
        else {
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
        if (new String(decrypt.readMD5Hash(file)).equals(new String(forBoth.getMD5(file)))) {
            decrypt.setParamsForDecryption(file);
            decrypt.setChipperForDecode(file);
            decrypt.setInFile(file);
            decrypt.setOutFile(file);
            decrypt.decryptFile();
            decrypt.deleteEverythingAfterDecryption(file);
        }
        else {
        }
    }

    private void recursionToEncrypt(File file) {
        try {
            File files[] = file.listFiles();
            for (File temp : files) {
                if (temp.isFile() && checkForEncodeFileIndex(temp)) {
                    encrypt(temp);
                }
                recursionToEncrypt(temp);

                if(temp.getAbsolutePath().equals(folderNames.get(currentFolder))) {
                    currentFolder++;
                }
            }
        }
        catch (Exception e) {
        }
    }

    private void recursionToDecrypt(File file) {
        try {
            File files[] = file.listFiles();
            for (File temp : files) {
                if (temp.isFile() && checkForDecodeFileIndex(temp)) {
                    decrypt(temp);
                }
                recursionToDecrypt(temp);

                if(counterOfFolders > currentFolder) {
                    if(temp.getAbsolutePath().equals(folderNames.get(currentFolder))){
                        currentFolder++;
                    }
                }
            }
        }
        catch (Exception e) { }
    }

    public void getDir(File file) {
        counterOfFolders = 0;
        File files[] = file.listFiles();
        for (File temp : files) {

            if (temp.isDirectory()) {
                counterOfFolders++;
                folderNames.add(temp.getAbsolutePath());
            }
        }

        if (counterOfFolders >= 1) {
            zipRequired = true;
        }
    }

    public File returnNewFile() {
        return newestFile;
    }

    public boolean returnRunning() {
        return running;
    }

    public int getCounterOfFolders() {
        return counterOfFolders;
    }

    public int getCurrentFolder() {
        return currentFolder;
    }

}
