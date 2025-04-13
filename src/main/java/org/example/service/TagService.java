package org.example.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.TagDto;
import org.example.mapper.TagEntityMapper;
import org.example.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagEntityMapper tagEntityMapper;

    @Transactional
    public void deleteByPostId(Long postId) {
        if (postId != null) {
            tagRepository.deleteByPostId(postId);
        }
    }

    @Transactional
    public void updateAll(String tags, Long postId) {
        if (StringUtils.isNotBlank(tags)) {
                tagRepository.deleteByPostId(postId);
                tagRepository.saveAll(tags, postId);
        }
    }

    public List<TagDto> findByPostId(Long postId) {
        if (postId != null) {
            return tagRepository.findByPostId(postId).stream()
                    .map(tagEntityMapper::tagEntityToTagDto)
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void saveAll(String tags, Long postId) {
        if (StringUtils.isNotBlank(tags)) {
            tagRepository.saveAll(tags, postId);
        }
    }
}
