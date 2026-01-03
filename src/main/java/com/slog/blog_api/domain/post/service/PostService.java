package com.slog.blog_api.domain.post.service;

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

    @Transactional
    public Long writePost(PostRequest request) {
        Post post = postRepository.save(request.toEntity());
        return post.getId();
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostResponse(post);
    }

    @Transactional
    public Long updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        post.update(request.getTitle(), request.getContent(), null, request.getStatus());

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
}
