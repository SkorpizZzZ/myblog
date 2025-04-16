package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.ImageRepository;
import org.example.utils.TomcatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void saveImage(MultipartFile image, Long postId) {
        if (!image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            Path filePath = TomcatUtils.UPLOAD_PATH.resolve(fileName);

            try {
                Files.write(filePath, image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(
                        MessageFormat.format("Не удалось сохранить Image для поста под id {0}", postId)
                );
            }
            imageRepository.save(fileName, postId);
        }
    }

    public File getImageByPostId(Long postId) {
        if (postId != null) {
            String relativePath = imageRepository.findPathByPostId(postId)
                    .orElseThrow(() -> new RuntimeException(
                            MessageFormat.format("Image с идентификатором поста {0} не найден", postId)
                    ));
            ;
            String catalinaBase = System.getProperty("catalina.base");
            try {
                return ResourceUtils.getFile(catalinaBase.concat(relativePath));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(MessageFormat.format("Image по переданному пути {0} не найден", relativePath));
            }
        } else {
            throw new RuntimeException("Не передан обязательный атрибут - идентификатор Поста");
        }
    }

    @Transactional
    public void deleteByPostId(Long postId) {
        if (postId != null) {
            try {
                Files.delete(getImageByPostId(postId).toPath());
            } catch (IOException e) {
                throw new RuntimeException(
                        MessageFormat.format("Произошла не предвиденная ошибка. Image по post_id {0} не удален", postId)
                );
            }
            imageRepository.deleteByPostId(postId);
        }
    }

    @Transactional
    public void updateImage(MultipartFile image, Long postId) {
        if (!image.isEmpty()) {
            imageRepository.findPathByPostId(postId).ifPresent(path -> deleteByPostId(postId));
            saveImage(image, postId);
        }
    }
}