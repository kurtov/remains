package ru.kurtov.remains.orderitems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import static java.util.Objects.requireNonNull;

public class OrderItemPureJDBCDAO implements OrderItemDAO {

    private final DataSource dataSource;

    public OrderItemPureJDBCDAO(final DataSource dataSource) {
        this.dataSource = requireNonNull(dataSource);
    }

    @Override
    public void insert(final OrderItem orderItem) {

        if (orderItem.getId() != null) {
            throw new IllegalArgumentException("can not save " + orderItem + " with already assigned id");
        }

        try (final Connection connection = dataSource.getConnection()) {
            final String query = "INSERT INTO order_items (order_id, goods_name, value) VALUES (?, ?, ?)";
            try (final PreparedStatement statement =
                    connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

                statement.setInt(1, orderItem.getOrderId());
                statement.setString(2, orderItem.getGoodsName());
                statement.setInt(3, orderItem.getValue());

                statement.executeUpdate();

                try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    orderItem.setId(generatedKeys.getInt(1));
                }
            }

        } catch (final SQLException e) {
            throw new RuntimeException("failed to persist " + orderItem, e);
        }
    }

    @Override
    public Optional<OrderItem> get(final int orderItemId) {
        try (final Connection connection = dataSource.getConnection()) {

            final String query = "SELECT id, order_id, goods_name, value FROM order_items WHERE id = ?";
            try (final PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, orderItemId);

                try (final ResultSet resultSet = statement.executeQuery()) {

                    final boolean exists = resultSet.next();
                    if (!exists) {
                        return Optional.empty();
                    }
                    return Optional.of(
                        OrderItem.existing(
                            orderItemId,
                            resultSet.getInt("order_id"),
                            resultSet.getString("goods_name"),
                            resultSet.getInt("value")
                        )
                    );
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException("failed to get orderItem by id " + orderItemId, e);
        }
    }

    @Override
    public Set<OrderItem> getAll() {

        try (final Connection connection = dataSource.getConnection()) {

            try (final Statement statement = connection.createStatement()) {

                final String query = "SELECT id, order_id, goods_name, value FROM order_items";
                try (final ResultSet resultSet = statement.executeQuery(query)) {

                    final Set<OrderItem> orderItems = new HashSet<>();
                    while (resultSet.next()) {
                        orderItems.add(
                            OrderItem.existing(
                                resultSet.getInt("id"),
                                resultSet.getInt("order_id"),
                                resultSet.getString("goods_name"),
                                resultSet.getInt("value")
                            )
                        );
                    }
                    return orderItems;
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException("failed to get orderItems", e);
        }
    }
          
    @Override
    public void update(final OrderItem orderItem) {
        if (orderItem.getId() == null) {
            throw new IllegalArgumentException("can not update " + orderItem + " without id");
        }

        try(final Connection connection = dataSource.getConnection()) {
            final String query = "UPDATE order_items SET order_id = ?, goods_name = ?, value = ? WHERE id = ?";
            try (final PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, orderItem.getOrderId());
                statement.setString(2, orderItem.getGoodsName());
                statement.setInt(3, orderItem.getValue());
                statement.setInt(4, orderItem.getId());

                statement.executeUpdate();
            }
        } catch (final SQLException e) {
            throw new RuntimeException("failed to update " + orderItem, e);
        }
    }

    @Override
    public void delete(final int orderItemId) {

        try (final Connection connection = dataSource.getConnection()) {

            final String query = "DELETE FROM order_items WHERE id = ?";
            try(final PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, orderItemId);

               statement.executeUpdate();
            }

        } catch (final SQLException e) {
            throw new RuntimeException("failed to remove orderItem by id " + orderItemId, e);
        }
    }
}