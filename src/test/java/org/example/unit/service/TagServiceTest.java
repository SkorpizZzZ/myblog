package org.example.unit.service;

import org.example.domain.TagEntity;
import org.example.dto.TagDto;
import org.example.mapper.TagEntityMapper;
import org.example.repository.TagRepository;
import org.example.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @InjectMocks
    private TagService service;
    @Mock
    private TagRepository repository;
    @Mock
    private TagEntityMapper mapper;

    private Long postId = 1L;

    @Nested
    @DisplayName("Сохранение тегов по post_id")
    class SaveAll {
        @Test
        @DisplayName("Успешное сохранение тегов")
        void successSaveAll() {
            //GIVEN
            String tags = "tags";
            //THEN
            service.saveAll(tags, postId);
            verify(repository, times(1)).saveAll(anyString(), anyLong());
        }

        @Test
        @DisplayName("Передан null тег")
        void tagsNull() {
            //GIVEN
            String tags = null;
            //THEN
            service.saveAll(tags, postId);
            verify(repository, never()).saveAll(anyString(), anyLong());
        }

        @Test
        @DisplayName("Передан пустой тег")
        void tagsBlank() {
            //GIVEN
            String tags = "";
            //THEN
            service.saveAll(tags, postId);
            verify(repository, never()).saveAll(anyString(), anyLong());
        }
    }

    @Nested
    @DisplayName("Поиск тегов по post_id")
    class FindByPostId {
        @Test
        @DisplayName("Теги успешно найдены")
        void tagsFound() {
            //GIVEN
            TagEntity tagEntity = new TagEntity(
                    1L,
                    "tag",
                    1L
            );
            TagDto tagDto = new TagDto(
                    1L,
                    "tag",
                    1L
            );
            List<TagEntity> foundTags = Collections.singletonList(tagEntity);
            List<TagDto> expectedResult = Collections.singletonList(tagDto);
            //WHEN
            when(repository.findByPostId(anyLong())).thenReturn(foundTags);
            when(mapper.tagEntityToTagDto(any(TagEntity.class))).thenReturn(tagDto);
            //THEN
            List<TagDto> actualResult = service.findByPostId(postId);
            assertThat(actualResult).isEqualTo(expectedResult);
        }

        @Test
        @DisplayName("По переданному post_id теги не найдены")
        void tagsNotFound() {
            //GIVEN
            List<TagEntity> foundTags = Collections.emptyList();
            //WHEN
            when(repository.findByPostId(anyLong())).thenReturn(foundTags);
            //THEN
            List<TagDto> actualResult = service.findByPostId(postId);
            assertThat(actualResult).isEmpty();
            verify(mapper, never()).tagEntityToTagDto(any(TagEntity.class));
        }

        @Test
        @DisplayName("Передан post_id null")
        void inputPostIdNull() {
            //GIVEN
            postId = null;
            //THEN
            List<TagDto> actualResult = service.findByPostId(postId);
            assertThat(actualResult).isEmpty();
        }
    }

    @Nested
    @DisplayName("Обновление тегов по post_id")
    class UpdateAll {
        @Test
        @DisplayName("Успешное обновление всех тегов")
        void successUpdating() {
            //GIVEN
            String tags = "tag";
            //THEN
            service.updateAll(tags, postId);
            verify(repository, times(1)).deleteByPostId(anyLong());
            verify(repository, times(1)).saveAll(anyString(), anyLong());
        }

        @Test
        @DisplayName("Предан тег null")
        void tagNull() {
            //GIVEN
            String tags = null;
            //THEN
            service.updateAll(tags, postId);
            verify(repository, never()).deleteByPostId(anyLong());
            verify(repository, never()).saveAll(anyString(), anyLong());
        }

        @Test
        @DisplayName("Предан пустой тег")
        void tagBlank() {
            //GIVEN
            String tags = "";
            //THEN
            service.updateAll(tags, postId);
            verify(repository, never()).deleteByPostId(anyLong());
            verify(repository, never()).saveAll(anyString(), anyLong());
        }
    }

    @Nested
    @DisplayName("Удаление тегов по post_id")
    class DeleteByPostId {

        @Test
        @DisplayName("Успешное удаление")
        void successDeleting() {
            //THEN
            service.deleteByPostId(postId);
            verify(repository, times(1)).deleteByPostId(anyLong());
        }

        @Test
        @DisplayName("Передан post_id Null")
        void inputPostIdNull() {
            //THEN
            service.deleteByPostId(null);
            verify(repository, never()).deleteByPostId(anyLong());
        }
    }
}
