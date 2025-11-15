package com.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public GatewayFilter jwtAuthFilter(JwtAuthFilter filter) {
        return filter;
    }
}
