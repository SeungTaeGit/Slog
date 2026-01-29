package com.slog.blog_api.domain.admin.dto;

import lombok.Data;

public class AdminRequestDto {

    @Data
    public static class RenameRequest {
        private String newName;
    }
}
