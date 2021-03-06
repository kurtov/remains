package ru.kurtov.remains.remains;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import ru.kurtov.remains.DBTestBase;

public class RemainsServiceTest extends DBTestBase{
    
    private static final RemainsDAO remainsDAO = getBean(RemainsSpringJDBCDAO.class);
    private static final RemainsService service = getBean(RemainsService.class);
    
    
    @Before
    public void setUp() {
        remainsDAO.insert(Remains.create("Грибочки", 200));
        remainsDAO.insert(Remains.create("Помидорчики", 3));
    }
    
    @Test
    public void inventoryShouldModifyRemains_Set() {
        Set<Remains> remainsSet =  new HashSet();

        remainsSet.add(Remains.create("Грибочки", 100));
        remainsSet.add(Remains.create("Селедочка", 200));
        
        service.doInventory(remainsSet);
        
        assertEquals(0, remainsDAO.findByGoodsName("Помидорчики").get().getValue());
        assertEquals(100, remainsDAO.findByGoodsName("Грибочки").get().getValue());
        assertEquals(200, remainsDAO.findByGoodsName("Селедочка").get().getValue());
    }

    @Test
    public void inventoryShouldModifyRemains_XML() throws IOException, SAXException, ParserConfigurationException {
        final ClassLoader classLoader = RemainsServiceTest.class.getClassLoader();
        
        try (final InputStream inventoryInputStream = classLoader.getResourceAsStream("testInventory.xml")) {
            service.doInventory(inventoryInputStream);
            
            assertEquals(0, remainsDAO.findByGoodsName("Помидорчики").get().getValue());
            assertEquals(100, remainsDAO.findByGoodsName("Грибочки").get().getValue());
            assertEquals(200, remainsDAO.findByGoodsName("Селедочка").get().getValue());
        }
    }
}
