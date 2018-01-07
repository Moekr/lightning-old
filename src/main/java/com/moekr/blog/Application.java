package com.moekr.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.moekr.blog.data.repository")
@EnableElasticsearchRepositories(basePackages = "com.moekr.blog.data.search")
@EnableCaching
public class Application extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
