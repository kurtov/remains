package ru.kurtov.remains;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import ru.kurtov.remains.orderitems.OrderItem;
import ru.kurtov.remains.orderitems.OrderItemDAO;
import ru.kurtov.remains.orderitems.OrderItemPureJDBCDAO;

public class OrderItemDAOTest extends DBTestBase {   
    private static final OrderItemDAO orderItemDAO = new OrderItemPureJDBCDAO(database);

    
    @Test
    public void insertShouldInsertNewOrderItemInDBAndReturnOrderItemWithAssignedId() throws Exception {
        final OrderItem orderItem1 = OrderItem.create(1, "Селедочка", 10);
        final OrderItem orderItem2 = OrderItem.create(1, "Грибочки", 2);

        orderItemDAO.insert(orderItem1);
        orderItemDAO.insert(orderItem2);

        final OrderItem orderItem1FromDB = orderItemDAO.get(orderItem1.getId()).get();
        assertEquals(orderItem1, orderItem1FromDB);

        final OrderItem orderItem2FromDB = orderItemDAO.get(orderItem2.getId()).get();
        assertEquals(orderItem2, orderItem2FromDB);
    }


    @Test(expected = IllegalArgumentException.class)
    public void insertShouldThrowIllegalArgumentExceptionIfOrderItemHasId() throws Exception {
        final OrderItem orderItem = OrderItem.existing(1, 1, "Селедочка", 10);

        orderItemDAO.insert(orderItem);
    }

    
    @Test
    public void getShouldReturnOrderItem() throws Exception {
        final OrderItem orderItem = OrderItem.create(1, "Селедочка", 10);
        orderItemDAO.insert(orderItem);

        final Optional<OrderItem> orderItemFromDB = orderItemDAO.get(orderItem.getId());

        assertEquals(orderItem, orderItemFromDB.get());
    }


    @Test
    public void getShouldReturnEmptyOptionalIfNoOrderItemWithSuchId() throws Exception {
        final int nonExistentId = 666;

        final Optional<OrderItem> orderItemFromDB = orderItemDAO.get(nonExistentId);

        assertFalse(orderItemFromDB.isPresent());
    }

    @Test
    public void getAllShouldReturnAllOrderItems() throws Exception {
        assertTrue(orderItemDAO.getAll().isEmpty());

        final OrderItem orderItem1 = OrderItem.create(1, "Селедочка", 10);
        final OrderItem orderItem2 = OrderItem.create(1, "Грибочки", 2);

        orderItemDAO.insert(orderItem1);
        orderItemDAO.insert(orderItem2);

        final Set<OrderItem> orderItemFromDB = orderItemDAO.getAll();

        assertEquals(new HashSet<>(Arrays.asList(orderItem1, orderItem2)), orderItemFromDB);
    }

    @Test
    public void updateShouldUpdateOrderItem() throws Exception {
        final OrderItem orderItem = OrderItem.create(1, "Селедочка", 10);
        orderItemDAO.insert(orderItem);
        
        orderItem.setValue(30);

        orderItemDAO.update(orderItem);

        final OrderItem orderItemFromDB = orderItemDAO.get(orderItem.getId()).get();
        assertEquals(orderItem, orderItemFromDB);
    }


    @Test
    public void deleteShouldDeleteOrderItemById() throws Exception {
        final OrderItem orderItem1 = OrderItem.create(1, "Селедочка", 10);
        final OrderItem orderItem2 = OrderItem.create(1, "Грибочки", 2);

        orderItemDAO.insert(orderItem1);
        orderItemDAO.insert(orderItem2);


        orderItemDAO.delete(orderItem1.getId());

        assertFalse(orderItemDAO.get(orderItem1.getId()).isPresent());
        assertTrue(orderItemDAO.get(orderItem2.getId()).isPresent());
    }
}