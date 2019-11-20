package pl.carrental.images.controllers;

import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.carrental.images.ImageResponse;
import pl.carrental.images.ImageStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@RestController
public class ReportImagesController {

    private static final String CARS_LOCATION = System.getProperty("user.home") + File.separator + "Reports" + File.separator;
    private Tika tika = new Tika();
    private ImageStore imageStore = new ImageStore(CARS_LOCATION);


    @GetMapping("/reports/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {

        if (imageStore.fileDoesExist(fileName)) {
            File img = imageStore.loadImage(fileName);
            try {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.valueOf(tika.detect(img)))
                        .body(Files.readAllBytes(img.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/reports")
    public ImageResponse handleFileUpload(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        String extension = name.substring(name.lastIndexOf('.'));

        String generatedFileName;
        do {
            generatedFileName = UUID.randomUUID() + extension;
        } while (imageStore.fileDoesExist(generatedFileName));

        try {
            imageStore.saveImage(file.getBytes(), generatedFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageResponse("failed", null);
        }
        return new ImageResponse("success", generatedFileName);
    }
}
