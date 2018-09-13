package com.moekr.lightning.util;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties("lightning")
@Validated
public class LightningProperties {
    @URL
    @NotNull
    private String host;
    @NotNull
    private String eof;
    @NotNull
    private String copyright;
    @NotNull
    private AnalyticsProperties analytics;
    private ApplicationProperties application;

    @Autowired
    public LightningProperties(ApplicationProperties application) {
        this.application = application;
        ToolKit.properties = this;
    }

    @Data
    public static class AnalyticsProperties {
        @NotBlank
        private String appName;
        @NotBlank
        private String appId;
        @NotBlank
        private String viewId;
        @NotBlank
        private String beginDate;
        @NotBlank
        private String keyFile;
    }
}
