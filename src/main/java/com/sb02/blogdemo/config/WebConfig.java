package com.sb02.blogdemo.config;

import com.sb02.blogdemo.auth.JwtAuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;
    private final String uploadDir;

    public WebConfig(
            JwtAuthInterceptor jwtAuthInterceptor,
            @Value("${file.upload.dir}") String uploadDir
    ) {
        this.jwtAuthInterceptor = jwtAuthInterceptor;
        this.uploadDir = uploadDir;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // '/uploads/**' URL 패턴으로 요청이 오면 실제 파일 시스템의 uploadDir 경로로 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
