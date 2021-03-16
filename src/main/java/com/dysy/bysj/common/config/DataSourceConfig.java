package com.dysy.bysj.common.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
@Configuration
public class DataSourceConfig {
    @Bean
    public ApplicationRunner validateDataSource(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
            }
        };
    }
}
