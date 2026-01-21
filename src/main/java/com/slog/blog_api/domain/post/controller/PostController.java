package com.slog.blog_api.domain.post.controller;

import com.slog.blog_api.domain.post.dto.PostRequest;
import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.service.PostService;
import com.slog.blog_api.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<Long> write(@RequestBody @Valid PostRequest request) {
        Long postId = postService.writePost(request);
        return ApiResponse.success(postId);
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long id) {
        PostResponse response = postService.getPost(id);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> update(@PathVariable Long id, @RequestBody PostRequest request) {
        Long updatedId = postService.updatePost(id, request);
        return ApiResponse.success(updatedId);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return ApiResponse.success();
    }

    @GetMapping
    public ApiResponse<Page<PostResponse>> getList(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostResponse> response = postService.getPostList(keyword, pageable);
        return ApiResponse.success(response);
    }
}
