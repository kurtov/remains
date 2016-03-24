package ru.kurtov.remains;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.SAXException;
import ru.kurtov.remains.orderitems.OrderItem;
import ru.kurtov.remains.orderitems.OrderItemService;
import ru.kurtov.remains.remains.RemainsService;

public class Main {
    
    

    public static void main(final String... args)  throws IOException, SAXException, ParserConfigurationException {
        final ClassLoader classLoader = Main.class.getClassLoader();
        final ApplicationContext applicationContext = createApplicationContext();

        final OrderItemService orderItemService = applicationContext.getBean(OrderItemService.class);
        final RemainsService remainsService = applicationContext.getBean(RemainsService.class);
        
        try (final InputStream inventoryInputStream = classLoader.getResourceAsStream("inventory.xml")) {
            
            //Провести инвентаризацию
            remainsService.doInventory(inventoryInputStream);
            
            //Создать 1-й заказ
            orderItemService.insert(OrderItem.create(1, "Селедочка", 10));
            orderItemService.insert(OrderItem.create(1, "Огурчики", 2));
            
            //Создать 2-й заказ
            orderItemService.insert(OrderItem.create(2, "Селедочка", 20));
            orderItemService.insert(OrderItem.create(2, "Огурчики", 4));
            orderItemService.insert(OrderItem.create(3, "Макарошки", 2));
        }
    } 
    
    private static ApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/ru/kurtov/remains/prod-beans.xml");
    }
}
