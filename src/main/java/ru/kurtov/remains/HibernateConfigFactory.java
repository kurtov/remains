package ru.kurtov.remains;

import org.hibernate.cfg.Configuration;
import ru.kurtov.remains.orderitems.OrderItem;

public class HibernateConfigFactory {
    public static Configuration prod() {
        return new Configuration().addAnnotatedClass(OrderItem.class);
    }

    private HibernateConfigFactory() {
    }
}
