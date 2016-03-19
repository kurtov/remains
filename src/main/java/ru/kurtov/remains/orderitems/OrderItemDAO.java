package ru.kurtov.remains.orderitems;

import java.util.Optional;
import java.util.Set;

public interface OrderItemDAO {

    void insert(OrderItem orderItem);

    Optional<OrderItem> get(int orderItemId);

    Set<OrderItem> getAll();

    void update(OrderItem orderItem);

    void delete(int orderItemId);
}
