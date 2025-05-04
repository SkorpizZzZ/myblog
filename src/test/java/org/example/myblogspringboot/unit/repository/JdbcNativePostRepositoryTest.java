package org.example.myblogspringboot.unit.repository;

import org.example.myblogspringboot.domain.PostEntity;
import org.example.myblogspringboot.repository.JdbcNativePostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcNativePostRepositoryTest {

    @InjectMocks
    private JdbcNativePostRepository repository;
    @Mock
    private JdbcTemplate jdbcTemplate;


    @Test
    void findAll() {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 1);
        Integer countPosts = 1;
        PostEntity foundPost = new PostEntity(
                1L,
                "title",
                "textPreview",
                0L,
                "text"
        );
        List<PostEntity> expectedResult = Collections.singletonList(foundPost);
        //WHEN
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(countPosts);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt(), anyLong())).thenReturn(expectedResult);
        //GIVEN
        Page<PostEntity> actualResult = repository.findAll(pageable);
        assertThat(actualResult.getContent()).isEqualTo(expectedResult);
    }

    @Test
    void save() {
        //GIVEN
        String title = "title";
        String text = "text";
        String textPreview = "textPreview";
        Long expectedResult = 1L;
        //WHEN
        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Long.class),
                any(),
                any(),
                eq(0),
                any())
        ).thenReturn(expectedResult);
        //THEN
        Long actualResult = repository.save(title, text, textPreview);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void update() {
        //GIVEN
        Long id = 1L;
        String title = "title";
        String text = "text";
        String textPreview = "textPreview";
        Long expectedResult = 1L;
        //WHEN
        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Long.class),
                any(),
                any(),
                any(),
                anyLong())
        ).thenReturn(expectedResult);
        //THEN
        Long actualResult = repository.update(id, title, text, textPreview);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void findById() {
        //GIVEN
        Long id = 1L;
        PostEntity foundPost = new PostEntity(
                1L,
                "title",
                "textPreview",
                0L,
                "text"
        );
        List<PostEntity> foundPosts = Collections.singletonList(foundPost);
        Optional<PostEntity> expectedResult = Optional.of(foundPost);
        //WHEN
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong())).thenReturn(foundPosts);
        //THEN
        Optional<PostEntity> actualResult = repository.findById(id);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void delete() {
        //GIVEN
        Long id = 1L;
        //THEN
        repository.delete(id);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }

    @Test
    void like() {
        //GIVEN
        Long id = 1L;
        //THEN
        repository.like(id);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }

    @Test
    void dislike() {
        //GIVEN
        Long id = 1L;
        //THEN
        repository.dislike(id);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }

    @Test
    void findAllByTag() {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 1);
        String tag = "tag";
        Integer countPosts = 1;
        PostEntity foundPost = new PostEntity(
                1L,
                "title",
                "textPreview",
                0L,
                "text"
        );
        List<PostEntity> expectedResult = Collections.singletonList(foundPost);
        //WHEN
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyString())).thenReturn(countPosts);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString(), anyInt(), anyLong())).thenReturn(expectedResult);
        //GIVEN
        Page<PostEntity> actualResult = repository.findAllByTag(pageable, tag);
        assertThat(actualResult.getContent()).isEqualTo(expectedResult);
    }
}