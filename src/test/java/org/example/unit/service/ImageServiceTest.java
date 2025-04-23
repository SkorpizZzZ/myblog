package org.example.unit.service;

import org.example.repository.ImageRepository;
import org.example.service.FileService;
import org.example.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @InjectMocks
    private ImageService service;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private FileService fileService;
    @Mock
    private MultipartFile image;

    private Long postId = null;
    private final String FOUND_RELATIVE_PATH = "path/to/image.png";


    @BeforeEach
    void setUp() {
        postId = 1L;
    }

    @Nested
    @DisplayName("Обновление image")
    class UpdateImage {
        @Test
        @DisplayName("Успешное обновление")
        void successUpdating() {
            //WHEN
            when(image.isEmpty()).thenReturn(false);
            when(imageRepository.findPathByPostId(anyLong())).thenReturn(Optional.of(FOUND_RELATIVE_PATH));
            when(fileService.saveFile(any(MultipartFile.class))).thenReturn("fileName.png");
            //THEN
            service.updateImage(image, postId);
            verify(imageRepository, times(1)).findPathByPostId(anyLong());
            verify(fileService, times(1)).delete(anyString());
            verify(imageRepository, times(1)).deleteByPostId(anyLong());
            verify(fileService, times(1)).saveFile(any(MultipartFile.class));
            verify(imageRepository, times(1)).save(anyString(), anyLong());
        }
        @Test
        @DisplayName("Передан пустой image")
        void imageEmpty() {
            //WHEN
            when(image.isEmpty()).thenReturn(true);
            //THEN
            service.updateImage(image, postId);
            verify(imageRepository, never()).findPathByPostId(anyLong());
            verify(fileService, never()).delete(anyString());
            verify(imageRepository, never()).deleteByPostId(anyLong());
            verify(fileService, never()).saveFile(any(MultipartFile.class));
            verify(imageRepository, never()).save(anyString(), anyLong());
        }
    }

    @Nested
    @DisplayName("Удаление image по post_id")
    class DeleteByPostId {
        @Test
        @DisplayName("Успешное удаление")
        void successDeleting() {
            //WHEN
            when(imageRepository.findPathByPostId(anyLong())).thenReturn(Optional.of(FOUND_RELATIVE_PATH));
            //THEN
            service.deleteByPostId(postId);
            verify(fileService, times(1)).delete(anyString());
            verify(imageRepository, times(1)).deleteByPostId(anyLong());
        }
        @Test
        @DisplayName("По переданному post_id image не найден")
        void imageNotFound() {
            //WHEN
            when(imageRepository.findPathByPostId(anyLong())).thenReturn(Optional.empty());
            //THEN
            assertThatThrownBy(() -> service.deleteByPostId(postId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Image с идентификатором поста 1 не найден");
            verify(fileService, never()).delete(anyString());
            verify(imageRepository, never()).deleteByPostId(anyLong());
        }

        @Test
        @DisplayName("Передан null post_id")
        void postIdNull() {
            //GIVEN
            postId = null;
            //THEN
            service.deleteByPostId(postId);
            verify(imageRepository, never()).findPathByPostId(anyLong());
            verify(fileService, never()).delete(anyString());
            verify(imageRepository, never()).deleteByPostId(anyLong());
        }
    }

    @Nested
    @DisplayName("Поиск image по post_id")
    class FindImageByPostId {
        @Test
        @DisplayName("Image успешно найден")
        void successFound() {
            //GIVEN
            File expectedResult = mock(File.class);
            //WHEN
            when(imageRepository.findPathByPostId(anyLong())).thenReturn(Optional.of(FOUND_RELATIVE_PATH));
            when(fileService.getFile(anyString())).thenReturn(expectedResult);
            when(expectedResult.isFile()).thenReturn(true);
            when(expectedResult.exists()).thenReturn(true);
            //THEN
            File actualResult = service.findImageByPostId(postId);
            assertThat(actualResult).isFile();
            assertThat(actualResult).exists();
        }

        @Test
        @DisplayName("Image с переданным идентификатором поста не найден")
        void imageNotFound() {
            //WHEN
            when(imageRepository.findPathByPostId(anyLong())).thenReturn(Optional.empty());
            //THEN
            assertThatThrownBy(() -> service.findImageByPostId(postId)).isInstanceOf(RuntimeException.class)
                    .hasMessage("Image с идентификатором поста 1 не найден");
            verify(fileService, never()).getFile(anyString());
        }

        @Test
        @DisplayName("Передан null post_id")
        void postIdNull() {
            //GIVEN
            postId = null;
            //THEN
            assertThatThrownBy(() -> service.findImageByPostId(postId)).isInstanceOf(RuntimeException.class)
                    .hasMessage("Не передан обязательный атрибут - идентификатор Поста");
            verify(imageRepository, never()).findPathByPostId(anyLong());
            verify(fileService, never()).getFile(anyString());
        }
    }

    @Nested
    @DisplayName("Сохранение Image")
    class SaveImage {
        @Test
        @DisplayName("Успешное сохранение")
        void successSaving() {
            //WHEN
            when(image.isEmpty()).thenReturn(false);
            when(fileService.saveFile(any(MultipartFile.class))).thenReturn("fileName");
            //THEN
            service.saveImage(image, postId);
            verify(fileService, times(1)).saveFile(any(MultipartFile.class));
            verify(imageRepository, times(1)).save(anyString(), anyLong());
        }

        @Test
        @DisplayName("Передан пустой файл")
        void imageEmpty() {
            //WHEN
            when(image.isEmpty()).thenReturn(true);
            //THEN
            service.saveImage(image, postId);
            verify(fileService, never()).saveFile(any(MultipartFile.class));
            verify(imageRepository, never()).save(anyString(), anyLong());
        }
    }
}
