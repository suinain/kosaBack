package com.mycompany.demo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000") //출처
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") //요청 방식
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
