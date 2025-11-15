package com.auth_service.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

// Class này sẽ "lắng nghe" tất cả các Exception trong project
@ControllerAdvice
public class GlobalExceptionHandler {

    // Bắt lỗi khi login sai (ném ra từ AuthService)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // Trả về 401
                .body(Map.of("error", ex.getMessage()));
    }

    // Bắt lỗi chung, ví dụ như "Username already exists"
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        if ("Username already exists".equals(ex.getMessage())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // Trả về 409
                    .body(Map.of("error", ex.getMessage()));
        }

        // Các lỗi runtime khác
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // Trả về 400
                .body(Map.of("error", ex.getMessage()));
    }
}