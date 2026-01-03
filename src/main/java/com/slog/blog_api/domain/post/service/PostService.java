package com.slog.blog_api.domain.post.service;

import com.slog.blog_api.domain.category.entity.Category;
import com.slog.blog_api.domain.category.repository.CategoryRepository;
import com.slog.blog_api.domain.post.dto.PostRequest;
import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.entity.Post;
import com.slog.blog_api.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long writePost(PostRequest request) {
        Category category = getOrCreateCategory(request.getCategoryName());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(request.getStatus())
                .category(category)
                .build();

        return postRepository.save(post).getId();
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostResponse(post);
    }

    @Transactional
    public Long updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Category category = getOrCreateCategory(request.getCategoryName());

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getThumbnailUrl(),
                request.getStatus(),
                category
        );

        return id;
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postRepository.delete(post);
    }

    public Page<PostResponse> getPostList(String keyword, Pageable pageable) {
        Page<Post> postPage = postRepository.search(keyword, pageable);

        return postPage.map(PostResponse::new);
    }

    private Category getOrCreateCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return null;
        }

        return categoryRepository.findByName(categoryName)
                .orElseGet(() -> categoryRepository.save(Category.builder().name(categoryName).build()));
    }
}
