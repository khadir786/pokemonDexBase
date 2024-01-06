package com.khadir.pokemonserver.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
// This class just populates the roles fields with ROLE_ADMIN AND ROLE_USER
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    public DatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader) {
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(String... args) throws Exception {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), 
            resourceLoader.getResource("classpath:insert-role.sql"));
    }
}
