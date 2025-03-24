package com.sb02.blogdemo.config;

import com.sb02.blogdemo.auth.JwtAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;

    public WebConfig(JwtAuthInterceptor jwtAuthInterceptor) {
        this.jwtAuthInterceptor = jwtAuthInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/posts/**", "/api/images/**") // 인증이 필요한 경로 패턴
                .excludePathPatterns("/api/posts", "/api/posts/**") // GET 요청 제외 (필요에 따라 조정)
                .excludePathPatterns("/api/users/register", "/api/users/login"); // 인증 제외 경로
    }
}
