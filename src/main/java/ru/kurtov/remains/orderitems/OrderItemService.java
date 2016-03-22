package ru.kurtov.remains.orderitems;

import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.function.Supplier;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.kurtov.remains.remains.Remains;
import ru.kurtov.remains.remains.RemainsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderItemService {
    private final SessionFactory sessionFactory;
    private final OrderItemDAO orderItemDAO;
    private final RemainsService remainsService;
    
    private static final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    public OrderItemService(
        final SessionFactory sessionFactory, 
        final OrderItemDAO orderItemDAO,
        final RemainsService remainsService)
    {   
        this.sessionFactory = requireNonNull(sessionFactory);
        this.orderItemDAO = requireNonNull(orderItemDAO);
        this.remainsService = remainsService;
    }

    public void insert(final OrderItem orderItem) {
        inTransaction(() -> {
            orderItemDAO.insert(orderItem);
            Remains remains = remainsService.findByGoodsName(orderItem.getGoodsName()).get();
            
            int value = remains.getValue() - orderItem.getValue();
            
            log.info("insert: {}, new remains: {}", orderItem, value);
            
            remains.setValue(value);
            remainsService.update(remains);
            
            
            if(value>=0) {
            //    remains.setValue(value);
            //    remainsService.update(remains);
            } else {
                throw new RuntimeException("Позиция заказа приведет к отрицательным остаткам");
            }
        });
    }
    
    public Optional<OrderItem> get(final int orderItemId) {
        return inTransaction(() -> orderItemDAO.get(orderItemId));
    }
    
    private <T> T inTransaction(final Supplier<T> supplier) {
        final Optional<Transaction> transaction = beginTransaction();
        try {
            final T result = supplier.get();
            transaction.ifPresent(Transaction::commit);
            return result;
        } catch (RuntimeException e) {
            transaction.ifPresent(Transaction::rollback);
            throw e;
        }
    }

    private void inTransaction(final Runnable runnable) {
        inTransaction(() -> {
            runnable.run();
            return null;
        });
    }

    private Optional<Transaction> beginTransaction() {
        final Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
            return Optional.of(transaction);
        }
        return Optional.empty();
    }
}