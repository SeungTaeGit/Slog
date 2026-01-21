package com.slog.blog_api.domain.post.service;

import com.slog.blog_api.domain.category.entity.Category;
import com.slog.blog_api.domain.category.repository.CategoryRepository;
import com.slog.blog_api.domain.post.dto.PostCreateRequest;
import com.slog.blog_api.domain.post.dto.PostEditRequest;
import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.entity.Post;
import com.slog.blog_api.domain.post.entity.PostStatus;
import com.slog.blog_api.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long writePost(PostCreateRequest request) {
        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(request.getCategoryName())
                        .build()));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .thumbnailUrl("https://image.com/default.png")
                .category(category)
                .status(PostStatus.PUBLIC)
                .build();

        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return new PostResponse(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPostList(Pageable pageable, String keyword, String categoryName) {
        return postRepository.search(keyword, categoryName, pageable)
                .map(PostResponse::new);
    }

    @Transactional
    public void editPost(Long postId, PostEditRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(request.getCategoryName())
                        .build()));

        post.update(request.getTitle(), request.getContent(), category, request.getStatus());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        postRepository.delete(post);
    }
}