package com.bibli.bia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.bibli.bia.postgres.repository")
public class PostgresConfig {
    // Spring Boot usa automáticamente las propiedades del application.properties
}