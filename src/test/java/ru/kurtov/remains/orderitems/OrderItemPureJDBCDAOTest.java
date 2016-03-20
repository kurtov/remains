package ru.kurtov.remains.orderitems;

public class OrderItemPureJDBCDAOTest extends OrderItemAbstractDAOTest {
    private static final OrderItemDAO orderItemDAO = new OrderItemPureJDBCDAO(database);

    @Override
    protected OrderItemDAO orderItemDAO() {
        return orderItemDAO;
    };
    
    @Override
    protected void commit() {}
}