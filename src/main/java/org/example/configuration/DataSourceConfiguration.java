package org.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.TomcatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


@Configuration
@Slf4j
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @EventListener
    @Order(1)
    public void populate(ContextRefreshedEvent event) {
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("scripts/schema.sql"));
        populator.execute(dataSource);
    }

    @EventListener
    @Order(2)
    public void initDataBase(ContextRefreshedEvent event) throws IOException {
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);

        if (!Files.exists(TomcatUtils.UPLOAD_PATH)) {
            Files.createDirectories(TomcatUtils.UPLOAD_PATH);
        }

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("scripts/init.sql"));
        populator.execute(dataSource);
    }

    @EventListener
    public void clearDataBaseEvent(ContextClosedEvent event) throws IOException {
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);


        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("scripts/clear_values.sql"));
        populator.execute(dataSource);
        clearUploadDir();
    }

    private void clearUploadDir() throws IOException {
        if (Files.exists(TomcatUtils.UPLOAD_PATH)) {
            try (Stream<Path> paths = Files.list(TomcatUtils.UPLOAD_PATH)) {
                paths.forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Не удалось удалить: " + path, e);
                    }
                });
            }
        }
    }
}
