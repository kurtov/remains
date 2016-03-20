package ru.kurtov.remains;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import ru.kurtov.remains.remains.Remains;
import ru.kurtov.remains.remains.RemainsDAO;
import ru.kurtov.remains.remains.RemainsSpringJDBCDAO;

public class RemainsDAOTest extends DBTestBase{
    private static final RemainsDAO remainsDAO = new RemainsSpringJDBCDAO(database);

    
    @Test
    public void insertShouldInsertNewRemainsInDBAndReturnRemainsWithAssignedId() throws Exception {
        final Remains remains1 = Remains.create("Селедочка", 100);
        final Remains remains2 = Remains.create("Грибочки", 200);

        remainsDAO.insert(remains1);
        remainsDAO.insert(remains2);

        final Remains remains1FromDB = remainsDAO.get(remains1.getId()).get();
        assertEquals(remains1, remains1FromDB);

        final Remains remains2FromDB = remainsDAO.get(remains2.getId()).get();
        assertEquals(remains2, remains2FromDB);
    }


    @Test(expected = IllegalArgumentException.class)
    public void insertShouldThrowIllegalArgumentExceptionIfRemainsHasId() throws Exception {
        final Remains remains = Remains.existing(1, "Селедочка", 100);

        remainsDAO.insert(remains);
    }

    
    @Test
    public void getShouldReturnRemains() throws Exception {
        final Remains remains = Remains.create("Селедочка", 100);
        remainsDAO.insert(remains);

        final Optional<Remains> remainsFromDB = remainsDAO.get(remains.getId());

        assertEquals(remains, remainsFromDB.get());
    }
    
    @Test
    public void findByNameShouldReturnRemains() throws Exception {
        final Remains remains = Remains.create("Селедочка", 100);
        remainsDAO.insert(remains);

        final Optional<Remains> remainsFromDB = remainsDAO.findByGoodsName(remains.getGoodsName());

        assertEquals(remains, remainsFromDB.get());
    }


    @Test
    public void getShouldReturnEmptyOptionalIfNoRemainsWithSuchId() throws Exception {
        final int nonExistentId = 666;

        final Optional<Remains> remainsFromDB = remainsDAO.get(nonExistentId);

        assertFalse(remainsFromDB.isPresent());
    }

    @Test
    public void getAllShouldReturnAllRemains() throws Exception {
        assertTrue(remainsDAO.getAll().isEmpty());

        final Remains remains1 = Remains.create("Селедочка", 100);
        final Remains remains2 = Remains.create("Грибочки", 200);

        remainsDAO.insert(remains1);
        remainsDAO.insert(remains2);

        final Set<Remains> remainsFromDB = remainsDAO.getAll();

        assertEquals(new HashSet<>(Arrays.asList(remains1, remains2)), remainsFromDB);
    }

    @Test
    public void updateShouldUpdateRemains() throws Exception {
        final Remains remains = Remains.create("Селедочка", 100);
        remainsDAO.insert(remains);
        
        remains.setValue(30);

        remainsDAO.update(remains);

        final Remains remainsFromDB = remainsDAO.get(remains.getId()).get();
        assertEquals(remains, remainsFromDB);
    }


    @Test
    public void deleteShouldDeleteRemainsById() throws Exception {
        final Remains remains1 = Remains.create("Селедочка", 100);
        final Remains remains2 = Remains.create("Грибочки", 200);

        remainsDAO.insert(remains1);
        remainsDAO.insert(remains2);


        remainsDAO.delete(remains1.getId());

        assertFalse(remainsDAO.get(remains1.getId()).isPresent());
        assertTrue(remainsDAO.get(remains2.getId()).isPresent());
    }
}