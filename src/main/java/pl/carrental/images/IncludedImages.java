package pl.carrental.images;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IncludedImages {

    public boolean isIncluded(String fileName, ResourceLoader resourceLoader) {
        Resource[] pngs = getResourcesInResourceFolder("classpath*:/BOOT-INF/classes/static/*");
        for (Resource png : pngs) {
            if(png.getFilename().equals(fileName))
                return true;
        }
        return false;
    }

    public byte[] getImage(String fileName, ResourceLoader resourceLoader) {
        try {
            Resource resource = resourceLoader.getResource("classpath:/BOOT-INF/classes/static/" + fileName);
            InputStream is = resource.getInputStream();
            return inputStreamToByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Resource[] getResourcesInResourceFolder(String folder) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(folder);
            return resources;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] inputStreamToByteArray(InputStream is){
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }
}
