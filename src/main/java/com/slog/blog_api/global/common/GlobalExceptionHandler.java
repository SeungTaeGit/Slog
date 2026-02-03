package com.slog.blog_api.global.common;

import com.slog.blog_api.domain.log.entity.SystemLog;
import com.slog.blog_api.domain.log.repository.SystemLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SystemLogRepository systemLogRepository;

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleAllExceptions(Exception e, HttpServletRequest request) {

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();

        SystemLog errorLog = SystemLog.builder()
                .level("ERROR")
                .method(request.getMethod())
                .url(request.getRequestURI())
                .message(e.getMessage())
                .stackTrace(stackTrace)
                .clientIp(request.getRemoteAddr())
                .build();

        systemLogRepository.save(errorLog);

        log.error("서버 에러 발생: {}", e.getMessage(), e);

        return ApiResponse.error("서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");
    }
}