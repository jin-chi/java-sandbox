package com.example.springjpa_spec;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                                // 全てのエンドポイントに適用
                .allowedOrigins("http://localhost:5173")          // このoriginからのアクセスを許可
                .allowedMethods("GET", "POST", "PUT", "DELETE");  // 許可するHTTPメソッド
    }
}
