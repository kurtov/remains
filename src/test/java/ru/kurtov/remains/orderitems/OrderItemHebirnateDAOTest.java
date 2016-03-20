package ru.kurtov.remains.orderitems;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import ru.kurtov.remains.HibernateSessionFactory;

public class OrderItemHebirnateDAOTest extends OrderItemAbstractDAOTest {

    private static final SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
    private static final OrderItemDAO orderItemDAO = new OrderItemHebirnateDAO(sessionFactory);
    private Transaction transaction;
   
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