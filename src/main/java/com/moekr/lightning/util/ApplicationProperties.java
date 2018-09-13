package com.moekr.lightning.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("application")
public class ApplicationProperties {
    private String name = "Lightning";
    private String version = "Unknown version";
    private String url = "https://github.com/Moekr/lightning";
    private String commit = "-1";
    private String timestamp = "-1";
}
