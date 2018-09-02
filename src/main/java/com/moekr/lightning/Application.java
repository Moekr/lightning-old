package com.moekr.lightning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.moekr.lightning.data.repository")
@EnableElasticsearchRepositories(basePackages = "com.moekr.lightning.data.search")
@EnableCaching
@EnableScheduling
public class Application extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
