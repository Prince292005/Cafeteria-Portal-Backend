package com.dau.cafeteria_portal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Configurable via UPLOAD_DIR env var so this works on any OS/host
    // (the old hardcoded "C:/cafeteria-data/..." path only ever worked on
    // Windows and silently broke every upload on Linux hosts like Render).
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String base = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + base + "images/");
        registry.addResourceHandler("/certificates/**")
                .addResourceLocations("file:" + base + "certificates/");
        registry.addResourceHandler("/menus/**")
                .addResourceLocations("file:" + base + "menus/");
        registry.addResourceHandler("/committee_photos/**")
                .addResourceLocations("file:" + base + "committee_photos/");
    }
}
