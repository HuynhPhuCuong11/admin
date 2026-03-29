package com.example.trasua.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Serve static images:
 *  - /images/**  →  src/main/resources/static/images/ (ảnh seed từ DB)
 *  - /images/products/** → uploadDir (ảnh mới upload)
 *
 * Ảnh seed trong DB lưu tên kiểu "CaPheSuaDa.webp" không có prefix "products/"
 * nên template dùng @{'/images/' + ${p.defaultImage}} là đúng.
 *
 * Khi admin upload ảnh mới, file lưu vào uploadDir và DB cũng chỉ lưu tên file.
 * WebConfig map cả 2 nguồn về cùng URL prefix /images/ nên template không cần thay đổi.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadAbsUri = Paths.get(uploadDir).toAbsolutePath().toUri().toString();
        // Nếu path không kết thúc bằng "/" thì thêm vào
        if (!uploadAbsUri.endsWith("/")) uploadAbsUri += "/";

        // /images/** → thư mục upload trước (ảnh mới), fallback về classpath (ảnh seed)
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadAbsUri)
                .addResourceLocations("classpath:/static/images/");
    }
}
