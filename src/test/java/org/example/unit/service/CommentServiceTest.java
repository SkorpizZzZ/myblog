package org.example.unit.service;

import org.example.domain.CommentEntity;
import org.example.dto.CommentDto;
import org.example.mapper.CommentEntityMapper;
import org.example.repository.CommentRepository;
import org.example.service.CommentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentService service;
    @Mock
    private CommentRepository repository;
    @Mock
    private CommentEntityMapper mapper;

    private Long postId = 1L;

    @Nested
    @DisplayName("Удаления комментария по post_id и comment_id")
    class DeleteComment {
        @Test
        @DisplayName("Успешное обновление комментария")
        void successDeleting() {
            //GIVEN
            Long commentId = 1L;
            //THEN
            service.deleteComment(postId, commentId);
            verify(repository, times(1)).deleteComment(anyLong(), anyLong());
        }
    }

    @Nested
    @DisplayName("Обновление комментария по post_id и comment_id")
    class UpdateComment {
        @Test
        @DisplayName("Успешное обновление комментария")
        void successUpdated() {
            //GIVEN
            String comment = "comment";
            Long commentId = 1L;
            //THEN
            service.updateComment(postId, comment, commentId);
            verify(repository, times(1)).updateComment(anyLong(), anyString(), anyLong());
        }

        @Test
        @DisplayName("Передан comment null")
        void inputCommentNull() {
            //GIVEN
            String comment = null;
            Long commentId = 1L;
            //THEN
            service.updateComment(postId, comment, commentId);
            verify(repository, never()).updateComment(anyLong(), anyString(), anyLong());
        }

        @Test
        @DisplayName("Передан пустой комментарий")
        void inputCommentBlank() {
            //GIVEN
            String comment = "";
            Long commentId = 1L;
            //THEN
            service.updateComment(postId, comment, commentId);
            verify(repository, never()).updateComment(anyLong(), anyString(), anyLong());
        }
    }

    @Nested
    @DisplayName("Добавление комментария по post_id")
    class AddComment {
        @Test
        @DisplayName("Успешное добавление")
        void successAdded() {
            //GIVEN
            String comment = "comment";
            //THEN
            service.addComment(postId, comment);
            verify(repository, times(1)).addComment(anyLong(), anyString());
        }

        @Test
        @DisplayName("Передан пустой комментарий")
        void inputCommentBlank() {
            //GIVEN
            String comment = "";
            //THEN
            service.addComment(postId, comment);
            verify(repository, never()).addComment(anyLong(), anyString());
        }

        @Test
        @DisplayName("Передан comment null")
        void inputCommentNull() {
            //GIVEN
            String comment = null;
            //THEN
            service.addComment(postId, comment);
            verify(repository, never()).addComment(anyLong(), anyString());
        }

        @Test
        @DisplayName("Передан postId null")
        void inputPostIdNull() {
            //GIVEN
            postId = null;
            String comment = "comment";
            //THEN
            service.addComment(postId, comment);
            verify(repository, never()).addComment(anyLong(), anyString());
        }
    }

    @Nested
    @DisplayName("Удаление комментария по post_id")
    class DeleteByPostId {
        @Test
        @DisplayName("Передан null")
        void inputPostIdNull() {
            //THEN
            service.deleteByPostId(null);
            verify(repository, never()).deleteByPostId(anyLong());
        }

        @Test
        @DisplayName("Удаление комментария")
        void successDeleting() {
            //THEN
            service.deleteByPostId(postId);
            verify(repository, times(1)).deleteByPostId(anyLong());
        }
    }

    @Nested
    @DisplayName("Поиск комментария по post_id")
    class FindByPostId {

        @Test
        @DisplayName("Передан null")
        void inputPostIdNull() {
            //THEN
            List<CommentDto> actualResult = service.findByPostId(null);
            assertThat(actualResult).isEmpty();
        }

        @Test
        @DisplayName("По переданному post_id комментарий не найден")
        void commentNotFound() {
            //WHEN
            when(repository.findByPostId(anyLong())).thenReturn(Collections.emptyList());
            //THEN
            List<CommentDto> actualResult = service.findByPostId(postId);
            assertThat(actualResult).isEmpty();
        }

        @Test
        @DisplayName("По переданному post_id комментарий найден")
        void commentFound() {
            //GIVEN
            CommentEntity foundComment = new CommentEntity(
                    1L,
                    "comment",
                    1L
            );
            CommentDto mappedComment = new CommentDto(
                    1L,
                    "comment",
                    1L
            );
            List<CommentDto> expectedResult = Collections.singletonList(mappedComment);
            //WHEN
            when(repository.findByPostId(anyLong())).thenReturn(List.of(foundComment));
            when(mapper.commentEntityToCommentDto(any(CommentEntity.class))).thenReturn(mappedComment);
            //THEN
            List<CommentDto> actualResult = service.findByPostId(postId);
            assertThat(actualResult).isEqualTo(expectedResult);
        }
    }
}
