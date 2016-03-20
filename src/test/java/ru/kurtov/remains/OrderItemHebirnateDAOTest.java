package ru.kurtov.remains;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import ru.kurtov.remains.orderitems.OrderItemDAO;
import ru.kurtov.remains.orderitems.OrderItemHebirnateDAO;

public class OrderItemHebirnateDAOTest extends OrderItemAbstractDAOTest {

    protected static final SessionFactory sessionFactory = getSessionFactory();
    private static final OrderItemDAO orderItemDAO = new OrderItemHebirnateDAO(sessionFactory);
    private Transaction transaction;

    private static SessionFactory getSessionFactory() {
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
    
    @Override
    protected OrderItemDAO orderItemDAO() {
        return orderItemDAO;
    };
    
    @Override
    protected void commit() {
        transaction.commit();
        beginTransaction();        
    }
        
    @Before
    public void beginTransaction() {
        transaction = sessionFactory.getCurrentSession().beginTransaction();
    }
    
    @After
    public void rollbackTransaction() {
        transaction.rollback();
    }
}