package org.example.myblogspringboot.service;

import lombok.RequiredArgsConstructor;
import org.example.myblogspringboot.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;

    @Transactional
    public void saveImage(MultipartFile image, Long postId) {
        if (!image.isEmpty()) {
            String fileName = fileService.saveFile(image);
            imageRepository.save(fileName, postId);
        }
    }

    public File findImageByPostId(Long postId) {
        if (postId != null) {
            String relativePath = imageRepository.findPathByPostId(postId)
                    .orElseThrow(() -> new RuntimeException(
                            MessageFormat.format("Image с идентификатором поста {0} не найден", postId)
                    ));
            return fileService.getFile(relativePath);
        } else {
            throw new RuntimeException("Не передан обязательный атрибут - идентификатор Поста");
        }
    }

    @Transactional
    public void deleteByPostId(Long postId) {
        if (postId != null) {
            String relativePath = imageRepository.findPathByPostId(postId)
                    .orElseThrow(() -> new RuntimeException(
                            MessageFormat.format("Image с идентификатором поста {0} не найден", postId)
                    ));
            this.deleteImage(postId, relativePath);
        }
    }

    @Transactional
    public void updateImage(MultipartFile image, Long postId) {
        if (!image.isEmpty()) {
            imageRepository.findPathByPostId(postId).ifPresent(path -> this.deleteImage(postId, path));
            this.saveImage(image, postId);
        }
    }

    private void deleteImage(Long postId, String path) {
        fileService.delete(path);
        imageRepository.deleteByPostId(postId);
    }
}