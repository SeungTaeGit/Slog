package com.slog.blog_api.domain.post.repository;

import com.slog.blog_api.domain.post.dto.SidebarDto;
import com.slog.blog_api.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    Page<Post> search(String keyword, String categoryName, String tagName, String seriesName, Pageable pageable);

    List<SidebarDto.CategoryCount> getCategoryCounts();

    List<SidebarDto.SeriesCount> getSeriesCounts();

    List<String> getPublicTagNames();
}