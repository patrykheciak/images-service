package pl.carrental.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageStore {

    private String storeLocation;

    public ImageStore(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public boolean saveImage(byte[] bytes, String fileName) {
        try {
            File newFile = new File(storeLocation + fileName);
            newFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(bytes);
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public File loadImage(String fileName) {
        return new File(storeLocation + fileName);
    }

    public boolean fileDoesExist(String fileName) {
        File f = new File(storeLocation + fileName);
        return f.exists() && !f.isDirectory();
    }
}
