package com.bibli.bia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(basePackages = "com.bibli.bia.repository")
public class BiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiaApplication.class, args);
    }
}