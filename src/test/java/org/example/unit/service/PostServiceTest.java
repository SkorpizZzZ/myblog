package org.example.unit.service;

import org.example.myblogspringboot.domain.CommentEntity;
import org.example.myblogspringboot.domain.PostEntity;
import org.example.myblogspringboot.domain.TagEntity;
import org.example.myblogspringboot.dto.CommentDto;
import org.example.myblogspringboot.dto.PostDto;
import org.example.myblogspringboot.dto.TagDto;
import org.example.myblogspringboot.mapper.CommentEntityMapper;
import org.example.myblogspringboot.mapper.PostEntityMapper;
import org.example.myblogspringboot.mapper.TagEntityMapper;
import org.example.myblogspringboot.repository.PostRepository;
import org.example.myblogspringboot.service.CommentService;
import org.example.myblogspringboot.service.ImageService;
import org.example.myblogspringboot.service.PostService;
import org.example.myblogspringboot.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService service;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private TagService tagService;
    @Mock
    private CommentService commentService;
    @Mock
    private PostEntityMapper postEntityMapper;
    @Mock
    private CommentEntityMapper commentEntityMapper;
    @Mock
    private TagEntityMapper tagEntityMapper;


    @Nested
    @DisplayName("Удаление комментария")
    class deleteComment {

        @Test
        @DisplayName("Успешное удаление комментария")
        void successDeletingComment() {
            //GIVEN
            Long postiId = 1L;
            Long commentId = 1L;
            //THEN
            service.deleteComment(postiId, commentId);
            verify(commentService, times(1)).deleteComment(anyLong(),anyLong());
        }
    }

    @Nested
    @DisplayName("Обновление комментария")
    class UpdateComment {

        @Test
        @DisplayName("Успешное обновление комментария")
        void successUpdatingComment() {
            //GIVEN
            Long postiId = 1L;
            String comment = "comment";
            Long commentId = 1L;
            //THEN
            service.updateComment(postiId, comment, commentId);
            verify(commentService, times(1)).updateComment(anyLong(), anyString(), anyLong());
        }

        @Test
        @DisplayName("Передан null комментарий")
        void inputCommentNull() {
            //GIVEN
            Long postiId = 1L;
            String comment = null;
            Long commentId = 1L;
            //THEN
            service.updateComment(postiId, comment, commentId);
            verify(commentService, never()).updateComment(anyLong(), anyString(), anyLong());
        }

        @Test
        @DisplayName("Передан пустой комментарий")
        void inputCommentBlank() {
            //GIVEN
            Long postiId = 1L;
            String comment = "";
            Long commentId = 1L;
            //THEN
            service.updateComment(postiId, comment, commentId);
            verify(commentService, never()).updateComment(anyLong(), anyString(), anyLong());
        }
    }

    @Nested
    @DisplayName("Добавления комментария")
    class AddComment {

        @Test
        @DisplayName("Успешное добавление комментария")
        void successAddedComment() {
            //GIVEN
            Long postiId = 1L;
            String comment = "comment";
            //THEN
            service.addComment(postiId, comment);
            verify(commentService, times(1)).addComment(anyLong(), anyString());
        }

        @Test
        @DisplayName("Передан null комментарий")
        void inputCommentNull() {
            //GIVEN
            Long postiId = 1L;
            String comment = null;
            //THEN
            service.addComment(postiId, comment);
            verify(commentService, never()).addComment(anyLong(), anyString());
        }

        @Test
        @DisplayName("Передан пустой комментарий")
        void inputCommentBlank() {
            //GIVEN
            Long postiId = 1L;
            String comment = "";
            //THEN
            service.addComment(postiId, comment);
            verify(commentService, never()).addComment(anyLong(), anyString());
        }

        @Test
        @DisplayName("Передан null postId")
        void inputPostIdNull() {
            //GIVEN
            Long postiId = null;
            String comment = "comment";
            //THEN
            service.addComment(postiId, comment);
            verify(commentService, never()).addComment(anyLong(), anyString());
        }
    }

    @Nested
    @DisplayName("Лайк/Дизлайк поста")
    class Like {

        @Test
        @DisplayName("Передан null postId")
        void inputPostIdNull() {
            //GIVEN
            Long postId = null;
            boolean likeValue = false;
            //THEN
            service.like(postId, likeValue);
            verify(postRepository, never()).dislike(anyLong());
            verify(postRepository, never()).like(anyLong());
        }

        @Test
        @DisplayName("Дизлайк поста")
        void dislike() {
            //GIVEN
            Long postId = 1L;
            boolean likeValue = false;
            //THEN
            service.like(postId, likeValue);
            verify(postRepository, times(1)).dislike(anyLong());
            verify(postRepository, never()).like(anyLong());
        }

        @Test
        @DisplayName("Лайк поста")
        void like() {
            //GIVEN
            Long postId = 1L;
            boolean likeValue = true;
            //THEN
            service.like(postId, likeValue);
            verify(postRepository, times(1)).like(anyLong());
            verify(postRepository, never()).dislike(anyLong());
        }
    }

    @Nested
    @DisplayName("Обновление поста")
    class Update {

        @Test
        @DisplayName("Успешное обновление")
        void successUpdating() {
            //GIVEN
            Long postId = 1L;
            String title = "title";
            String text = "text";
            String tags = "tags";
            MultipartFile image = new MockMultipartFile("test.txt", "test content".getBytes());
            //WHEN
            when(postRepository.update(postId, title, text, text)).thenReturn(postId);
            //THEN
            service.update(postId, title, text, tags, image);
            verify(tagService, times(1)).updateAll(anyString(), anyLong());
            verify(imageService, times(1)).updateImage(any(MultipartFile.class), anyLong());
        }
    }

    @Nested
    @DisplayName("Удаление поста по id")
    class Delete {

        @Test
        @DisplayName("Успешное удаление")
        void successDeleting() {
            //GIVEN
            Long postId = 1L;
            //THEN
            service.delete(postId);
            verify(tagService, times(1)).deleteByPostId(anyLong());
            verify(imageService, times(1)).deleteByPostId(anyLong());
            verify(commentService, times(1)).deleteByPostId(anyLong());
            verify(postRepository, times(1)).delete(anyLong());
        }

        @Test
        @DisplayName("Передан null postId")
        void inputPostIdNull() {
            //GIVEN
            Long postId = null;
            //THEN
            assertThatThrownBy(() -> service.delete(postId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Не передан обязательный параметр - идентификатор Поста");
        }
    }

    @Nested
    @DisplayName("Поиск поста по id")
    class FindById {
        Long id = null;

        @BeforeEach
        void setUpId() {
            id = 1L;
        }

        @Test
        @DisplayName("Пост найден")
        void postFound() {
            //GIVEN
            PostEntity fondPostEntity = new PostEntity(
                    1L,
                    "title",
                    "textPreview",
                    1L,
                    "text"
            );
            CommentEntity foundCommentEntity = new CommentEntity(
                    1L,
                    "comment",
                    1L
            );
            TagEntity foundTagEntity = new TagEntity(
                    1L,
                    "comment",
                    1L
            );
            CommentDto foundCommentDto = new CommentDto(
                    1L,
                    "comment",
                    1L
            );
            TagDto foundTagDto = new TagDto(
                    1L,
                    "comment",
                    1L
            );
            PostDto expectedResult = new PostDto(
                    1L,
                    "title",
                    "textPreview",
                    1L,
                    "text",
                    Collections.singletonList(foundCommentDto),
                    Collections.singletonList(foundTagDto)
            );
            //WHEN
            when(postRepository.findById(id)).thenReturn(Optional.of(fondPostEntity));
            when(commentService.findByPostId(any())).thenReturn(Collections.singletonList(foundCommentDto));
            when(commentEntityMapper.commentDtoToCommentEntity(any(CommentDto.class))).thenReturn(foundCommentEntity);
            when(tagService.findByPostId(any())).thenReturn(Collections.singletonList(foundTagDto));
            when(tagEntityMapper.tagDtoToTagEntity(any(TagDto.class))).thenReturn(foundTagEntity);
            when(postEntityMapper.postEntityToPostDto(any(PostEntity.class))).thenReturn(expectedResult);
            //THEN
            PostDto actualResult = service.findById(id);
            assertThat(actualResult).isEqualTo(expectedResult);
            verify(commentService, times(1)).findByPostId(anyLong());
            verify(tagService, times(1)).findByPostId(anyLong());
            verify(commentEntityMapper, times(1)).commentDtoToCommentEntity(any(CommentDto.class));
            verify(tagEntityMapper, times(1)).tagDtoToTagEntity(any(TagDto.class));
            verify(postEntityMapper, times(1)).postEntityToPostDto(any(PostEntity.class));
        }

        @Test
        @DisplayName("Пост не найден")
        void postNotFound() {
            //WHEN
            when(postRepository.findById(id)).thenReturn(Optional.empty());
            //THEN
            assertThatThrownBy(() -> service.findById(id))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Post с идентификатором 1 не найден");
        }

        @Test
        @DisplayName("Передан null id")
        void idNull() {
            //GIVEN
            id = null;
            //THEN
            assertThatThrownBy(() -> service.findById(id))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Не передан обязательный параметр - идентификатор Поста");
        }
    }

    @Nested
    @DisplayName("Добавление поста")
    class Save {
        @Test
        @DisplayName("Успешное добавление поста")
        void successSave() {
            //GIVEN
            String title = "title";
            String text = "text";
            String tags = "tags";
            MultipartFile image = new MockMultipartFile("test.txt", "test content".getBytes());
            Long savedPostId = 1L;
            //WHEN
            when(postRepository.save(title, text, text)).thenReturn(savedPostId);
            //THEN
            service.save(title, text, tags, image);
            verify(postRepository, times(1)).save(anyString(), anyString(), anyString());
            verify(tagService, times(1)).saveAll(anyString(), anyLong());
            verify(imageService, times(1)).saveImage(any(MultipartFile.class), anyLong());
        }
    }

    @Nested
    @DisplayName("Поиск всех постов")
    class FindAll {
        final Pageable PAGEABLE = PageRequest.of(0, 1);
        PageImpl<PostEntity> page = null;
        PostEntity fondPostEntity = null;
        CommentEntity foundCommentEntity = null;
        TagEntity foundTagEntity = null;
        CommentDto foundCommentDto = null;
        TagDto foundTagDto = null;

        @BeforeEach
        void setUp() {
            fondPostEntity = new PostEntity(
                    1L,
                    "title",
                    "textPreview",
                    1L,
                    "text"
            );
            page = new PageImpl<>(Collections.singletonList(fondPostEntity), PAGEABLE, 0);
            foundCommentEntity = new CommentEntity(
                    1L,
                    "comment",
                    1L
            );
            foundTagEntity = new TagEntity(
                    1L,
                    "comment",
                    1L
            );
            foundCommentDto = new CommentDto(
                    1L,
                    "comment",
                    1L
            );
            foundTagDto = new TagDto(
                    1L,
                    "comment",
                    1L
            );
        }

        @Nested
        @DisplayName("Поиск всех постов, без учета тега")
        class FindAllWithoutTag {
            final String TAG = null;

            @Test
            @DisplayName("Пост найден")
            void postsFound() {
                //GIVEN
                PostDto expectedResult = new PostDto(
                        1L,
                        "title",
                        "textPreview",
                        1L,
                        "text",
                        Collections.singletonList(foundCommentDto),
                        Collections.singletonList(foundTagDto)
                );
                //WHEN
                when(postRepository.findAll(any(PageRequest.class))).thenReturn(page);
                when(commentService.findByPostId(any())).thenReturn(Collections.singletonList(foundCommentDto));
                when(commentEntityMapper.commentDtoToCommentEntity(any(CommentDto.class))).thenReturn(foundCommentEntity);
                when(tagService.findByPostId(any())).thenReturn(Collections.singletonList(foundTagDto));
                when(tagEntityMapper.tagDtoToTagEntity(any(TagDto.class))).thenReturn(foundTagEntity);
                when(postEntityMapper.postEntityToPostDto(any(PostEntity.class))).thenReturn(expectedResult);
                //THEN
                Page<PostDto> actualResult = service.findAll(PAGEABLE, TAG);
                assertThat(actualResult.getContent()).isEqualTo(List.of(expectedResult));
                assertThat(actualResult.getTotalElements()).isEqualTo(1);
                verify(commentService, times(1)).findByPostId(anyLong());
                verify(tagService, times(1)).findByPostId(anyLong());
                verify(commentEntityMapper, times(1)).commentDtoToCommentEntity(any(CommentDto.class));
                verify(tagEntityMapper, times(1)).tagDtoToTagEntity(any(TagDto.class));
                verify(postRepository, never()).findAllByTag(any(Pageable.class), anyString());
                verify(postEntityMapper, times(1)).postEntityToPostDto(any(PostEntity.class));
            }

            @Test
            @DisplayName("Пост без тегов")
            void postWithoutTags() {
                //GIVEN
                PostDto expectedResult = new PostDto(
                        1L,
                        "title",
                        "textPreview",
                        1L,
                        "text",
                        Collections.singletonList(foundCommentDto),
                        Collections.emptyList()
                );
                //WHEN
                when(postRepository.findAll(any(PageRequest.class))).thenReturn(page);
                when(commentService.findByPostId(any())).thenReturn(Collections.singletonList(foundCommentDto));
                when(commentEntityMapper.commentDtoToCommentEntity(any(CommentDto.class))).thenReturn(foundCommentEntity);
                when(tagService.findByPostId(any())).thenReturn(Collections.emptyList());
                when(postEntityMapper.postEntityToPostDto(any(PostEntity.class))).thenReturn(expectedResult);
                //THEN
                Page<PostDto> actualResult = service.findAll(PAGEABLE, TAG);
                assertThat(actualResult.getContent()).isEqualTo(List.of(expectedResult));
                assertThat(actualResult.getTotalElements()).isEqualTo(1);
                verify(commentService, times(1)).findByPostId(anyLong());
                verify(tagService, times(1)).findByPostId(anyLong());
                verify(commentEntityMapper, times(1)).commentDtoToCommentEntity(any(CommentDto.class));
                verify(tagEntityMapper, never()).tagDtoToTagEntity(any(TagDto.class));
                verify(postRepository, never()).findAllByTag(any(Pageable.class), anyString());
                verify(postEntityMapper, times(1)).postEntityToPostDto(any(PostEntity.class));
            }

            @Test
            @DisplayName("Пост без комментариев")
            void postWithoutComments() {
                //GIVEN
                PostDto expectedResult = new PostDto(
                        1L,
                        "title",
                        "textPreview",
                        1L,
                        "text",
                        Collections.emptyList(),
                        Collections.singletonList(foundTagDto)
                );
                //WHEN
                when(postRepository.findAll(any(PageRequest.class))).thenReturn(page);
                when(commentService.findByPostId(any())).thenReturn(Collections.emptyList());
                when(tagService.findByPostId(any())).thenReturn(Collections.singletonList(foundTagDto));
                when(tagEntityMapper.tagDtoToTagEntity(any(TagDto.class))).thenReturn(foundTagEntity);
                when(postEntityMapper.postEntityToPostDto(any(PostEntity.class))).thenReturn(expectedResult);
                //THEN
                Page<PostDto> actualResult = service.findAll(PAGEABLE, TAG);
                assertThat(actualResult.getContent()).isEqualTo(List.of(expectedResult));
                assertThat(actualResult.getTotalElements()).isEqualTo(1);
                verify(commentService, times(1)).findByPostId(anyLong());
                verify(tagService, times(1)).findByPostId(anyLong());
                verify(commentEntityMapper, never()).commentDtoToCommentEntity(any(CommentDto.class));
                verify(tagEntityMapper, times(1)).tagDtoToTagEntity(any(TagDto.class));
                verify(postRepository, never()).findAllByTag(any(Pageable.class), anyString());
                verify(postEntityMapper, times(1)).postEntityToPostDto(any(PostEntity.class));
            }

            @Test
            @DisplayName("Пост не найден")
            void postNotFound() {
                //GIVEN
                page = new PageImpl<>(Collections.emptyList(), PAGEABLE, 0);
                //WHEN
                when(postRepository.findAll(any(PageRequest.class))).thenReturn(page);
                //THEN
                Page<PostDto> actualResult = service.findAll(PAGEABLE, TAG);
                assertThat(actualResult.getContent()).isEmpty();
                assertThat(actualResult.getTotalElements()).isEqualTo(0);
                verify(commentService, never()).findByPostId(anyLong());
                verify(tagService, never()).findByPostId(anyLong());
                verify(commentEntityMapper, never()).commentDtoToCommentEntity(any(CommentDto.class));
                verify(tagEntityMapper, never()).tagDtoToTagEntity(any(TagDto.class));
                verify(postRepository, never()).findAllByTag(any(Pageable.class), anyString());
                verify(postEntityMapper, never()).postEntityToPostDto(any(PostEntity.class));
            }
        }

        @Nested
        @DisplayName("Поск по тегу")
        class FindByTag {
            final String TAG = "tag";

            @Test
            @DisplayName("Пост найден")
            void postsFound() {
                //GIVEN
                PostDto expectedResult = new PostDto(
                        1L,
                        "title",
                        "textPreview",
                        1L,
                        "text",
                        Collections.singletonList(foundCommentDto),
                        Collections.singletonList(foundTagDto)
                );
                //WHEN
                when(postRepository.findAllByTag(any(PageRequest.class), anyString())).thenReturn(page);
                when(commentService.findByPostId(any())).thenReturn(Collections.singletonList(foundCommentDto));
                when(commentEntityMapper.commentDtoToCommentEntity(any(CommentDto.class))).thenReturn(foundCommentEntity);
                when(tagService.findByPostId(any())).thenReturn(Collections.singletonList(foundTagDto));
                when(tagEntityMapper.tagDtoToTagEntity(any(TagDto.class))).thenReturn(foundTagEntity);
                when(postEntityMapper.postEntityToPostDto(any(PostEntity.class))).thenReturn(expectedResult);
                //THEN
                Page<PostDto> actualResult = service.findAll(PAGEABLE, TAG);
                assertThat(actualResult.getContent()).isEqualTo(List.of(expectedResult));
                assertThat(actualResult.getTotalElements()).isEqualTo(1);
                verify(commentService, times(1)).findByPostId(anyLong());
                verify(tagService, times(1)).findByPostId(anyLong());
                verify(commentEntityMapper, times(1)).commentDtoToCommentEntity(any(CommentDto.class));
                verify(tagEntityMapper, times(1)).tagDtoToTagEntity(any(TagDto.class));
                verify(postRepository, never()).findAll(any(Pageable.class));
                verify(postEntityMapper, times(1)).postEntityToPostDto(any(PostEntity.class));
            }

            @Test
            @DisplayName("Пост без тегов")
            void postWithoutTags() {
                //GIVEN
                PostDto expectedResult = new PostDto(
                        1L,
                        "title",
                        "textPreview",
                        1L,
                        "text",
                        Collections.singletonList(foundCommentDto),
                        Collections.emptyList()
                );
                //WHEN
                when(postRepository.findAllByTag(any(PageRequest.class), anyString())).thenReturn(page);
                when(commentService.findByPostId(any())).thenReturn(Collections.singletonList(foundCommentDto));
                when(commentEntityMapper.commentDtoToCommentEntity(any(CommentDto.class))).thenReturn(foundCommentEntity);
                when(tagService.findByPostId(any())).thenReturn(Collections.emptyList());
                when(postEntityMapper.postEntityToPostDto(any(PostEntity.class))).thenReturn(expectedResult);
                //THEN
                Page<PostDto> actualResult = service.findAll(PAGEABLE, TAG);
                assertThat(actualResult.getContent()).isEqualTo(List.of(expectedResult));
                assertThat(actualResult.getTotalElements()).isEqualTo(1);
                verify(commentService, times(1)).findByPostId(anyLong());
                verify(tagService, times(1)).findByPostId(anyLong());
                verify(commentEntityMapper, times(1)).commentDtoToCommentEntity(any(CommentDto.class));
                verify(tagEntityMapper, never()).tagDtoToTagEntity(any(TagDto.class));
                verify(postRepository, never()).findAll(any(Pageable.class));
                verify(postEntityMapper, times(1)).postEntityToPostDto(any(PostEntity.class));
            }

            @Test
            @DisplayName("Пост без комментариев")
            void postWithoutComments() {
                //GIVEN
                PostDto expectedResult = new PostDto(
                        1L,
                        "title",
                        "textPreview",
                        1L,
                        "text",
                        Collections.emptyList(),
                        Collections.singletonList(foundTagDto)
                );
                //WHEN
                when(postRepository.findAllByTag(any(PageRequest.class), anyString())).thenReturn(page);
                when(commentService.findByPostId(any())).thenReturn(Collections.emptyList());
                when(tagService.findByPostId(any())).thenReturn(Collections.singletonList(foundTagDto));
                when(tagEntityMapper.tagDtoToTagEntity(any(TagDto.class))).thenReturn(foundTagEntity);
                when(postEntityMapper.postEntityToPostDto(any(PostEntity.class))).thenReturn(expectedResult);
                //THEN
                Page<PostDto> actualResult = service.findAll(PAGEABLE, TAG);
                assertThat(actualResult.getContent()).isEqualTo(List.of(expectedResult));
                assertThat(actualResult.getTotalElements()).isEqualTo(1);
                verify(commentService, times(1)).findByPostId(anyLong());
                verify(tagService, times(1)).findByPostId(anyLong());
                verify(commentEntityMapper, never()).commentDtoToCommentEntity(any(CommentDto.class));
                verify(tagEntityMapper, times(1)).tagDtoToTagEntity(any(TagDto.class));
                verify(postRepository, never()).findAll(any(Pageable.class));
                verify(postEntityMapper, times(1)).postEntityToPostDto(any(PostEntity.class));
            }

            @Test
            @DisplayName("Пост не найден")
            void postNotFound() {
                //GIVEN
                page = new PageImpl<>(Collections.emptyList(), PAGEABLE, 0);
                //WHEN
                when(postRepository.findAllByTag(any(PageRequest.class), anyString())).thenReturn(page);
                //THEN
                Page<PostDto> actualResult = service.findAll(PAGEABLE, TAG);
                assertThat(actualResult.getContent()).isEmpty();
                assertThat(actualResult.getTotalElements()).isEqualTo(0);
                verify(commentService, never()).findByPostId(anyLong());
                verify(tagService, never()).findByPostId(anyLong());
                verify(commentEntityMapper, never()).commentDtoToCommentEntity(any(CommentDto.class));
                verify(tagEntityMapper, never()).tagDtoToTagEntity(any(TagDto.class));
                verify(postRepository, never()).findAll(any(Pageable.class));
                verify(postEntityMapper, never()).postEntityToPostDto(any(PostEntity.class));
            }
        }
    }
}
