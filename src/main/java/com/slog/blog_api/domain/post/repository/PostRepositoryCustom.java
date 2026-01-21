package com.slog.blog_api.domain.post.repository;

import com.slog.blog_api.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> search(String keyword, String categoryName, Pageable pageable);
}