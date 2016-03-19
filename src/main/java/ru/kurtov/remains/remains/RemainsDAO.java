package ru.kurtov.remains.remains;

import java.util.Optional;
import java.util.Set;
import ru.kurtov.remains.orderitems.OrderItem;

public interface RemainsDAO {

    void insert(Remains remains);

    Optional<Remains> get(int remainsId);

    Set<Remains> getAll();

    void update(Remains remains);

    void delete(int remainsId);
}