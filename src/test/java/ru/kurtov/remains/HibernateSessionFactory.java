package ru.kurtov.remains;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    public static SessionFactory getSessionFactory() {
        return hibernateTestConfig().buildSessionFactory();
    }

    private static Configuration hibernateTestConfig() {
        return HibernateConfigFactory.prod()
            .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
             //Так называется БД, созданная в DBTestBase
            .setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb")
            .setProperty("hibernate.current_session_context_class", "thread")
            .setProperty("hibernate.connection.username", "sa")
            .setProperty("hibernate.connection.password", "")
            .setProperty("hibernate.show_sql", "true")
            .setProperty("hibernate.format_sql", "true");        
    }
}
