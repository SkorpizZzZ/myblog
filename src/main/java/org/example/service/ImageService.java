package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;


    public File getImage(Long id) throws FileNotFoundException {
        String relativePath = imageRepository.findPathByPostId(id);
        String catalinaBase = System.getProperty("catalina.base");
        return ResourceUtils.getFile(catalinaBase.concat(relativePath));
    }
}