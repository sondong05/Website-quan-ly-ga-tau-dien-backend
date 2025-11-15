package com.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        // Sửa lại:
        // Logic phân quyền (authorizeExchange) đã được chuyển vào JwtAuthFilter
        // Config này chỉ cần tắt CSRF và cho phép mọi request đi qua
        // để đến được Global Filter của chúng ta.
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // Cho phép tất cả để đi tới GlobalFilter
                );

        return http.build();
    }
}