package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.ImageRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;


    public Resource getPath(Long id) {
        String relativePath = imageRepository.findPathByPostId(id);
        return new ClassPathResource(relativePath);
    }
}