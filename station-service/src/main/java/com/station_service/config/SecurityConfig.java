package com.station_service.config;

// SỬA: File này đang rỗng, cần được cấu hình
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Tắt Session
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Cho phép mọi request (vì Gateway đã lọc)
                );

        return http.build();
    }
}