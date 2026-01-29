package com.slog.blog_api.domain.post.controller;

import com.slog.blog_api.domain.post.dto.SidebarDto;
import com.slog.blog_api.domain.post.service.MetadataService;
import com.slog.blog_api.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping("/categories")
    public ApiResponse<List<SidebarDto.CategoryCount>> getCategories() {
        return ApiResponse.success(metadataService.getCategoryCounts());
    }

    @GetMapping("/tags")
    public ApiResponse<List<String>> getTags() {
        return ApiResponse.success(metadataService.getTagList());
    }

    @GetMapping("/series")
    public ApiResponse<List<SidebarDto.SeriesCount>> getSeries() {
        return ApiResponse.success(metadataService.getSeriesCounts());
    }
}