package com.SkillsSwap.skillswap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    private final DataSource dataSource;

    @Autowired
    public DatabaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public CommandLineRunner testDatabaseConnection() {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("Database connection test: SUCCESS");
                System.out.println("Database product name: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("Database product version: " + connection.getMetaData().getDatabaseProductVersion());
            } catch (SQLException e) {
                System.err.println("Database connection test: FAILED");
                System.err.println("Error message: " + e.getMessage());
                // Don't throw the exception, just log it, so the application can still start
                // even if the database is not available
            }
        };
    }
}