package lt.viko.eif.s033027.aes.execution;

import lt.viko.eif.s033027.aes.crypting.Decrypt;
import lt.viko.eif.s033027.aes.crypting.Encrypt;
import lt.viko.eif.s033027.aes.crypting.ForBoth;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ProgramExecution {

    private Encrypt encrypt = new Encrypt();
    private Decrypt decrypt = new Decrypt();
    private ForBoth forBoth = new ForBoth();

    public boolean stop = false;

    private boolean zipRequired = false;

    private List<String> folderNames = new ArrayList<>();

    public void checkIfFileForEncode(File file) {
        if (file.isFile() && checkForEncodeFileIndex(file)) {
            encrypt(file);
        }
        else {
            recursionToEncrypt(file);
            if(zipRequired) {
                setUpForZipping(file);
            }
        }
    }

    public void checkIfFileForDecode(File file) {
        if (file.isFile() && checkForDecodeFileIndex(file)) {
            File newFile = new File(file.getParent() + "\\" + file.getName().replaceFirst(".des",""));
            decrypt(file);
            System.out.println(file.getAbsolutePath() + " file");
            System.out.println(newFile.getAbsolutePath() + " newFile");
            if(newFile.getName().substring(newFile.getName().lastIndexOf(".") + 1).equals("zip")) {
                unZip(newFile);
                newFile.delete();
                File newestFile =  new File(newFile.getParent() + "\\" + newFile.getName().replaceFirst(".zip",""));
                System.out.println(newestFile.getAbsolutePath() + " newestFile");
                recursionToDecrypt(newestFile);
            }
        }
        else {
            recursionToDecrypt(file);
        }
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
            System.out.println("Blogi hash");
        }
    }

    private void recursionToEncrypt(File file) {
        try {
            File files[] = file.listFiles();
            for (File temp : files) {
                if (temp.isFile() && checkForEncodeFileIndex(temp)) {
                    encrypt(temp);
                }
                if (stop) {
                    break;
                }

                if (temp.isDirectory()) {

                }
                recursionToEncrypt(temp);
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
                if (stop) {
                    break;
                }
                recursionToDecrypt(temp);
            }
        }
        catch (Exception e) {
        }
    }

    public void getDir(File file) {
        int counterOfFolders = 0;
        File files[] = file.listFiles();
        for (File temp : files) {
            folderNames.add(temp.getAbsolutePath());
            if (temp.isDirectory()) {
                counterOfFolders++;
                System.out.println("I've been added");
            }
            System.out.println(temp.getAbsolutePath());
        }

        if (counterOfFolders >= 1) {
            zipRequired = true;
            System.out.println("I've been changed");
        }
    }

    public void setUpForZipping(File file) {
        try {
            FileOutputStream outFile = new FileOutputStream(file.getAbsolutePath() + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(outFile);
            zipFile(file, file.getName(), zipOut);
            zipOut.close();
            outFile.close();
            recursionToDeleteFiles(file);
            file.delete();
            encrypt(new File(file.getAbsolutePath() + ".zip"));
        }
        catch (IOException e) { }
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) {
        try {
            if (fileToZip.isDirectory()) {
                if (fileName.endsWith("/")) {
                    zipOut.putNextEntry(new ZipEntry(fileName));
                    zipOut.closeEntry();
                }
                else {
                    zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                    zipOut.closeEntry();
                }
                File[] files = fileToZip.listFiles();
                for (File temp : files) {
                    zipFile(temp, fileName + "/" + temp.getName(), zipOut);
                }
            }

            FileInputStream inFile = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inFile.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            inFile.close();
        }
        catch (IOException e) { }
    }

    public void unZip(File file) {
        try {
            ZipFile zipFile = new ZipFile(file.getAbsolutePath());
            zipFile.extractAll(file.getParent());
        }
        catch (ZipException e) { e.printStackTrace(); }
    }

    private void recursionToDeleteFiles(File file) {
        try {
            File files[] = file.listFiles();
            for (File temp : files) {
                recursionToDeleteFiles(temp);
                temp.delete();
            }
        }
        catch (Exception e) {
        }
    }

}
