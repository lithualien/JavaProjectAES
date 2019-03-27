package lt.viko.eif.s033027.aes.zipping;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class UnzipFile {

    public void unZip(File file) {
        try {
            net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(file.getAbsolutePath());
            zipFile.extractAll(file.getParent());
        }
        catch (ZipException e) { e.printStackTrace(); }
    }
}
