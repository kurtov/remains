package ru.kurtov.remains.orderitems;

public class OrderItemPureJDBCDAOTest extends OrderItemAbstractDAOTest {
    private static final OrderItemDAO orderItemDAO = getBean(OrderItemPureJDBCDAO.class);

    @Override
    protected OrderItemDAO orderItemDAO() {
        return orderItemDAO;
    };
    
    @Override
    protected void commit() {}
}