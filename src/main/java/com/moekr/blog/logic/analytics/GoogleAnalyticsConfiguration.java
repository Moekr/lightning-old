package com.moekr.blog.logic.analytics;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("blog.analytics")
public class GoogleAnalyticsConfiguration {
    private String name;
    private String viewId;
    private String begin;
    private String key;
}
