package org.example.unit.repository;

import org.example.myblogspringboot.repository.JdbcNativeImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcNativeImageRepositoryTest {

    @InjectMocks
    private JdbcNativeImageRepository repository;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void findPathByPostId() {
        //GIVEN
        Long postId = 1L;
        String foundPath = "path/to/image.png";
        Optional<String> expectedResult = Optional.of(foundPath);
        List<String> foundPaths = Collections.singletonList(foundPath);
        //WHEN
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong())).thenReturn(foundPaths);
        //THEN
        Optional<String> actualResult = repository.findPathByPostId(postId);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void save() {
        //GIVEN
        String fileName = "fileName";
        Long postId = 1L;
        //THEN
        repository.save(fileName, postId);
        verify(jdbcTemplate, times(1)).update(anyString(), anyString(), anyLong());
    }

    @Test
    void deleteByPostId() {
        //GIVEN
        Long postId = 1L;
        //THEN
        repository.deleteByPostId(postId);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }
}