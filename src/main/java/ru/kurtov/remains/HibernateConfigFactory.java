package ru.kurtov.remains;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.kurtov.remains.orderitems.OrderItem;

public class HibernateConfigFactory {
    public static Configuration prod() {
        return new Configuration().addAnnotatedClass(OrderItem.class);
    }

    public static SessionFactory getSessionFactory() {
        return prod().buildSessionFactory();
    }
    
    private HibernateConfigFactory() {
    }
}
