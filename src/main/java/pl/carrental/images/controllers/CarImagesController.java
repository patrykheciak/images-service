package pl.carrental.images.controllers;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.carrental.images.ImageResponse;
import pl.carrental.images.ImageStore;
import pl.carrental.images.IncludedImages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class CarImagesController {

    private static final String CARS_LOCATION = System.getProperty("user.home") + File.separator + "Cars" + File.separator;
    private Tika tika = new Tika();
    private ImageStore imageStore = new ImageStore(CARS_LOCATION);

    @Autowired
    ResourceLoader resourceLoader;
    private IncludedImages includedImages = new IncludedImages();

    @GetMapping("/cars/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {

        byte[] img = null;

        if (includedImages.isIncluded(fileName, resourceLoader)){
            System.out.println("isIncluded");
            img = includedImages.getImage(fileName, resourceLoader);
        }

        if (imageStore.fileDoesExist(fileName)) {
            System.out.println("fileDoesExist");
            try {
                img = Files.readAllBytes(imageStore.loadImage(fileName).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (img != null) {
            System.out.println("file not null");
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf(tika.detect(img)))
                    .body(img);
        }
        System.out.println("file is null");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/cars")
    public ImageResponse handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            imageStore.saveImage(file.getBytes(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageResponse("failed", null);
        }
        return new ImageResponse("success", file.getOriginalFilename());
    }
}
