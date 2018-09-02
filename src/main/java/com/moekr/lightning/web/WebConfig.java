package com.moekr.lightning.web;

import com.moekr.lightning.web.interceptor.BlockRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
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
        super.addInterceptors(registry);
    }
}
