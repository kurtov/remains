package ru.kurtov.remains.orderitems;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import ru.kurtov.remains.DBTestBase;
import ru.kurtov.remains.HibernateSessionFactory;
import ru.kurtov.remains.remains.Remains;
import ru.kurtov.remains.remains.RemainsService;

public class OrderItemServiceTest extends DBTestBase {

    protected static final SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
    private static final OrderItemDAO orderItemDAO = new OrderItemHebirnateDAO(sessionFactory);
    private static final OrderItemService orderItemService = 
            new OrderItemService(sessionFactory, orderItemDAO, new RemainsService(database));
    
    private static final RemainsService remainsService = new RemainsService(database);

    @Test
    public void InsertOrderItemShouldReduceRemainsIfRemainsEnough() throws Exception {
        Set<Remains> remainsSet =  new HashSet();

        remainsSet.add(Remains.create("Грибочки", 100));
        remainsSet.add(Remains.create("Селедочка", 200));
        
        remainsService.doInventory(remainsSet);
        OrderItem orderItem = OrderItem.create(1, "Грибочки", 40);
        orderItemService.insert(orderItem);
        
        OrderItem orderItemFromDB = orderItemService.get(orderItem.getId()).get();
        Remains remains = remainsService.findByGoodsName("Грибочки").get();
        
        assertEquals(40, orderItemFromDB.getValue());
        assertEquals(60, remains.getValue());
    }
    
    @Test
    public void InsertOrderItemShouldRollbackIfNotEnoughRemains() throws Exception {
        Set<Remains> remainsSet =  new HashSet();

        remainsSet.add(Remains.create("Грибочки", 100));
        remainsSet.add(Remains.create("Селедочка", 200));
        
        remainsService.doInventory(remainsSet);
        
        OrderItem orderItem = OrderItem.create(1, "Селедочка", 210);
        try {
            orderItemService.insert(orderItem);
        }
        catch (Exception e) {
            sessionFactory.getCurrentSession().beginTransaction();
        }
        
        
        assertFalse(orderItemService.get(orderItem.getId()).isPresent());
        assertTrue(orderItemDAO.getAll().isEmpty());
        
        Remains remains = remainsService.findByGoodsName("Селедочка").get();
        assertEquals(200, remains.getValue());
    }
}