package ru.kurtov.remains.orderitems;

import java.util.HashSet;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class OrderItemHebirnateDAO implements OrderItemDAO {

    private final SessionFactory sessionFactory;

    public OrderItemHebirnateDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = requireNonNull(sessionFactory);
    }

    @Override
    public void insert(final OrderItem orderItem) {
        if (orderItem.getId() != null) {
            throw new IllegalArgumentException("can not save " + orderItem + " with assigned id");
        }
        session().save(orderItem);
    }

    @Override
    public Optional<OrderItem> get(final int orderItemId) {
        final OrderItem orderItem = (OrderItem) session().get(OrderItem.class, orderItemId);
    
        return Optional.ofNullable(orderItem);
    }

    @Override
    public Set<OrderItem> getAll() {
        final Criteria criteria = session().createCriteria(OrderItem.class);
        final List<OrderItem> orderItems = criteria.list();

        return new HashSet<>(orderItems);
    }

    @Override
    public void update(final OrderItem orderItem) {
        session().update(orderItem);
    }

    @Override
    public void delete(final int orderItemId) {
        session().createQuery("DELETE OrderItem WHERE id = :id")
            .setInteger("id", orderItemId)
            .executeUpdate();
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}