package com.slog.blog_api.domain.post.repository;

import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.dto.PostSearchCondition;
import com.slog.blog_api.domain.post.dto.SidebarDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    Page<PostResponse> search(PostSearchCondition condition, Pageable pageable);

    List<SidebarDto.CategoryCount> getCategoryCounts();

    List<SidebarDto.SeriesCount> getSeriesCounts();

    List<String> getPublicTagNames();

    long getTotalViews();

    long countDistinctCategories();

    long countDistinctSeries();
}