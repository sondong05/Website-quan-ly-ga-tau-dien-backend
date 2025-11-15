package com.gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest; // <-- ĐÂY LÀ DÒNG ĐÚNG (CỦA REACTIVE)import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    // Danh sách các API không cần xác thực
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/login"
    );

    // Phương thức kiểm tra xem 1 request có nằm trong danh sách public không
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}