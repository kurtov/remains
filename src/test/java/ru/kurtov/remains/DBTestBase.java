package ru.kurtov.remains;

import org.junit.After;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public abstract class DBTestBase {
    
    private static final ConfigurableApplicationContext applicationContext =
        new ClassPathXmlApplicationContext("/ru/kurtov/remains/test-beans.xml");
    
    static {
        applicationContext.registerShutdownHook();
    }

    //Синтаксический сахар
    protected static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }
    
    static EmbeddedDatabase createEmbeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("create-tables.sql")
            .build();
    }

    @After
    public void tearDownDBTestBase() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getBean(EmbeddedDatabase.class));
        
        jdbcTemplate.update("DELETE FROM order_items");
        jdbcTemplate.update("DELETE FROM remains");
    }
}