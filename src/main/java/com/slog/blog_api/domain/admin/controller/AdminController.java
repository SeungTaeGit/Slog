package com.slog.blog_api.domain.admin.controller;

import com.slog.blog_api.domain.admin.dto.AdminDashboardDto;
import com.slog.blog_api.domain.admin.dto.AdminRequestDto;
import com.slog.blog_api.domain.admin.service.AdminService;
import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.dto.PostSearchCondition;
import com.slog.blog_api.domain.post.entity.PostStatus;
import com.slog.blog_api.domain.post.service.PostService;
import com.slog.blog_api.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final PostService postService;

    @GetMapping("/dashboard")
    public ApiResponse<AdminDashboardDto> getDashboard() {
        return ApiResponse.success(adminService.getDashboardStats());
    }

    @PatchMapping("/categories/{id}")
    public ApiResponse<Void> updateCategory(@PathVariable Long id, @RequestBody AdminRequestDto.RenameRequest request) {
        adminService.updateCategory(id, request.getNewName());
        return ApiResponse.success();
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return ApiResponse.success();
    }

    @PatchMapping("/series/{id}")
    public ApiResponse<Void> updateSeries(@PathVariable Long id, @RequestBody AdminRequestDto.RenameRequest request) {
        adminService.updateSeries(id, request.getNewName());
        return ApiResponse.success();
    }

    @DeleteMapping("/series/{id}")
    public ApiResponse<Void> deleteSeries(@PathVariable Long id) {
        adminService.deleteSeries(id);
        return ApiResponse.success();
    }

    @GetMapping("/posts")
    public ApiResponse<Page<PostResponse>> getAdminPosts(
            @ModelAttribute PostSearchCondition condition,
            Pageable pageable
    ) {
        return ApiResponse.success(postService.getPostList(condition, pageable));
    }

    @PatchMapping("/posts/{id}/status")
    public ApiResponse<Void> changePostStatus(
            @PathVariable Long id,
            @RequestParam PostStatus status
    ) {
        adminService.changePostStatus(id, status);
        return ApiResponse.success();
    }

    @DeleteMapping("/posts/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return ApiResponse.success();
    }

    @PostMapping("/posts/{id}/restore")
    public ApiResponse<Void> restorePost(@PathVariable Long id) {
        adminService.restorePost(id);
        return ApiResponse.success();
    }

    @DeleteMapping("/posts/{id}/hard")
    public ApiResponse<Void> hardDeletePost(@PathVariable Long id) {
        adminService.hardDeletePost(id);
        return ApiResponse.success();
    }
}
