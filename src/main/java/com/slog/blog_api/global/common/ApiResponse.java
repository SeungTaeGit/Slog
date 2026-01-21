package com.slog.blog_api.global.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final String status;
    private final String message;
    private final T data;

    private ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 성공 (데이터 있음)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", null, data);
    }

    // 성공 (데이터 없음 - 삭제 등)
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>("SUCCESS", null, null);
    }

    // 실패
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }
}