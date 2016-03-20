package ru.kurtov.remains;

import ru.kurtov.remains.orderitems.OrderItemDAO;
import ru.kurtov.remains.orderitems.OrderItemPureJDBCDAO;

public class OrderItemPureJDBCDAOTest extends OrderItemAbstractDAOTest { //DBTestBase {   
    private static final OrderItemDAO orderItemDAO = new OrderItemPureJDBCDAO(database);

    @Override
    protected OrderItemDAO orderItemDAO() {
        return orderItemDAO;
    };
    
    @Override
    protected void commit() {}
}