package com.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired; // THÊM
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter; // XÓA
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter; // THÊM
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter implements GlobalFilter { // SỬA: từ GatewayFilter -> GlobalFilter

    @Value("${jwt.secret}") // SỬA: từ app.jwt.secret -> jwt.secret
    private String secret;

    @Autowired // THÊM
    private RouterValidator routerValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. Kiểm tra xem request có cần bảo vệ hay không
        if (!routerValidator.isSecured.test(exchange.getRequest())) {
            // Nếu không cần, cho qua
            return chain.filter(exchange);
        }

        // 2. Nếu cần bảo vệ, thực hiện kiểm tra token
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        try {
            // 3. Giải mã token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 4. LỖI NGHIÊM TRỌNG ĐÃ SỬA:
            // Lấy thêm ROLE từ token (auth-service đã cung cấp)
            String role = claims.get("role", String.class);
            if (role == null) {
                throw new RuntimeException("Missing role in token");
            }

            // 5. Chuyển thông tin user và role cho các service con
            exchange = exchange.mutate()
                    .request(builder -> builder
                            .header("X-User-Id", claims.getSubject())
                            .header("X-User-Role", role) // THÊM DÒNG NÀY
                    )
                    .build();

        } catch (Exception e) {
            return unauthorized(exchange, "Invalid or expired token: " + e.getMessage());
        }

        // 6. Cho request đi tiếp
        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap(message.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}