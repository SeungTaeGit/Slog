package com.slog.blog_api.domain.post.controller;

import com.slog.blog_api.domain.post.dto.PostCreateRequest;
import com.slog.blog_api.domain.post.dto.PostEditRequest;
import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.dto.PostSearchCondition;
import com.slog.blog_api.domain.post.entity.PostStatus;
import com.slog.blog_api.domain.post.service.PostService;
import com.slog.blog_api.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        return ApiResponse.success(postId);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> get(@PathVariable Long postId) {
        PostResponse response = postService.getPost(postId);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<Page<PostResponse>> getPostList(
            @ModelAttribute PostSearchCondition condition,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        condition.setStatus(PostStatus.PUBLIC);

        return ApiResponse.success(postService.getPostList(condition, pageable));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<String> edit(@PathVariable Long postId, @Valid @RequestBody PostEditRequest request) {
        postService.editPost(postId, request);
        return ApiResponse.success("수정 성공");
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<String> delete(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.success("삭제 성공");
    }
}