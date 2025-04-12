package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.ImageRepository;
import org.example.utils.TomcatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;


    public File getImage(Long id) throws FileNotFoundException {
        String relativePath = imageRepository.findPathByPostId(id);
        String catalinaBase = System.getProperty("catalina.base");
        return ResourceUtils.getFile(catalinaBase.concat(relativePath));
    }

    public void saveImage(MultipartFile image, Long savedPostId) throws IOException {
        if (!image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            Path filePath = TomcatUtils.UPLOAD_PATH.resolve(fileName);

            Files.write(filePath, image.getBytes());

            imageRepository.save(fileName, savedPostId);
        }
    }
}