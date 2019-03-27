package lt.viko.eif.s033027.aes.zipping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFile {

    public void setUpForZipping(File file) {
        try {
            FileOutputStream outFile = new FileOutputStream(file.getAbsolutePath() + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(outFile);
            zipFile(file, file.getName(), zipOut);
            zipOut.close();
            outFile.close();
            recursionToDeleteFiles(file);
            file.delete();
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
