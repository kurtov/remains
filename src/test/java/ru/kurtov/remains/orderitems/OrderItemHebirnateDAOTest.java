package ru.kurtov.remains.orderitems;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;

public class OrderItemHebirnateDAOTest extends OrderItemAbstractDAOTest {

    private static final SessionFactory sessionFactory = getBean(SessionFactory.class);
    private static final OrderItemDAO orderItemDAO = getBean(OrderItemHebirnateDAO.class);
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