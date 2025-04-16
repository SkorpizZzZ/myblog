package org.example.unit.repository;

import org.example.domain.TagEntity;
import org.example.repository.JdbcNativeTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcNativeTagRepositoryTest {

    @InjectMocks
    private JdbcNativeTagRepository repository;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByPostId_notFoundTag() {
        //GIVEN
        Long inputPostId = 1L;
        //WHEN
        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class),
                eq(inputPostId))
        ).thenReturn(Collections.emptyList());
        //THEN
        List<TagEntity> actualResult = repository.findByPostId(inputPostId);
        assertThat(actualResult).isEmpty();
    }

    @Test
    void findByPostId_foundTag() {
        //GIVEN
        Long inputPostId = 1L;
        TagEntity mockTag = new TagEntity(
                1L,
                "tag",
                1L
        );
        List<TagEntity> expectedResult = Collections.singletonList(mockTag);
        //WHEN
        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class),
                eq(inputPostId))
        ).thenReturn(expectedResult);
        //THEN
        List<TagEntity> actualResult = repository.findByPostId(inputPostId);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void saveAll() {
        //GIVEN
        String inputTags = "JAVA JVM";
        Long inputPostId = 1L;
        //THEN
        repository.saveAll(inputTags, inputPostId);
        verify(jdbcTemplate, times(1)).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

    @Test
    void deleteByPostId() {
        //GIVEN
        Long inputPostId = 1L;
        //THEN
        repository.deleteByPostId(inputPostId);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }
}