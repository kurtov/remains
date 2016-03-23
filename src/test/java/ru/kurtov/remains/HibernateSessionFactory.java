package ru.kurtov.remains;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    public static SessionFactory getSessionFactory() {
        return hibernateTestConfig().buildSessionFactory();
    }

    private static Configuration hibernateTestConfig() {
        
        //Подтягиваются настройки из src/main/resources/hebirnate.properties
        return HibernateConfigFactory.prod()
            .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
            .setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb")
            .setProperty("hibernate.connection.username", "sa")
            .setProperty("hibernate.connection.password", "");        
    }
}
