package com.slog.blog_api.domain.post.controller;

import com.slog.blog_api.domain.post.dto.PostCreateRequest;
import com.slog.blog_api.domain.post.dto.PostEditRequest;
import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.service.PostService;
import com.slog.blog_api.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<Long> write(@Valid @RequestBody PostCreateRequest request) {
        Long postId = postService.writePost(request);
        return ApiResponse.ok(postId);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> get(@PathVariable Long postId) {
        PostResponse response = postService.getPost(postId);
        return ApiResponse.ok(response);
    }

    @GetMapping
    public ApiResponse<Page<PostResponse>> getList(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String seriesName
    ) {
        Page<PostResponse> postList = postService.getPostList(pageable, keyword, categoryName, tagName, seriesName);
        return ApiResponse.ok(postList);
    }

    @PatchMapping("/{postId}")
    public ApiResponse<String> edit(@PathVariable Long postId, @Valid @RequestBody PostEditRequest request) {
        postService.editPost(postId, request);
        return ApiResponse.ok("수정 성공");
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<String> delete(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.ok("삭제 성공");
    }
}