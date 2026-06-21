package com.dau.cafeteria_portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:C:/cafeteria-data/images/");
        registry.addResourceHandler("/certificates/**")
                .addResourceLocations("file:C:/cafeteria-data/certificates/");
        registry.addResourceHandler("/menus/**")
                .addResourceLocations("file:C:/cafeteria-data/menus/");
        registry.addResourceHandler("/committee_photos/**")
                .addResourceLocations(
                        "file:C:/cafeteria-data/committee_photos/",
                        "file:uploads/committee_photos/"
                );
    }
}
