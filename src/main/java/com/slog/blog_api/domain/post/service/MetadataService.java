package com.slog.blog_api.domain.post.service;

import com.slog.blog_api.domain.post.dto.SidebarDto;
import com.slog.blog_api.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetadataService {

    private final PostRepository postRepository;

    public List<SidebarDto.CategoryCount> getCategoryCounts() {
        return postRepository.getCategoryCounts();
    }

    public List<String> getTagList() {
        return postRepository.getPublicTagNames();
    }

    public List<SidebarDto.SeriesCount> getSeriesCounts() {
        return postRepository.getSeriesCounts();
    }
}
