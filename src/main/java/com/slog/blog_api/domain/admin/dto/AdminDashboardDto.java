package com.slog.blog_api.domain.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardDto {
    private long totalPosts;
    private long totalViews;
    private long todayPosts;
    private long totalCategories;
    private long totalSeries;
}
