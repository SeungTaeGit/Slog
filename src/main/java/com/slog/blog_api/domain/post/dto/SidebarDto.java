package com.slog.blog_api.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SidebarDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryCount {
        private Long id;
        private String name;
        private Long count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SeriesCount {
        private Long id;
        private String name;
        private Long count;
    }
}
