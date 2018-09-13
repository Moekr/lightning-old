package com.moekr.lightning.web;

import com.moekr.lightning.web.interceptor.BlockRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final BlockRequestInterceptor blockRequestInterceptor;

    @Autowired
    public WebConfig(BlockRequestInterceptor blockRequestInterceptor) {
        this.blockRequestInterceptor = blockRequestInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedMethods("POST", "GET", "PUT", "DELETE");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(blockRequestInterceptor);
    }
}
