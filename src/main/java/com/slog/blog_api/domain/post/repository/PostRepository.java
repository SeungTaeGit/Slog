package com.slog.blog_api.domain.post.repository;

import com.slog.blog_api.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    long countByCreatedAtAfter(LocalDateTime date);

    long getTotalViews();
    long countDistinctCategories();
    long countDistinctSeries();
}
