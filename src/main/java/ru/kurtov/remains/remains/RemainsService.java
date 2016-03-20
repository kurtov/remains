package ru.kurtov.remains.remains;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RemainsService {
    
    private final RemainsDAO remainsDAO;
    
    public RemainsService(DataSource dataSource) {
        this.remainsDAO = new RemainsSpringJDBCDAO(dataSource); //TODO: IoC
    }
    
    public void doInventory(InputStream xml) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xml);
        
        Set<Remains> remainsSet = xml2remainsSet(document);
        doInventory(remainsSet);
    }

    
    //|Товар есть в БД|Товар есть в инвенторизации|Что делать
    //|Да             |Да                         |Проапдейтить value
    //|Да             |Нет                        |Занулить value
    //|Нет            |Да                         |Заинсертить товар
    //|Нет            |Нет                        |Ничего не далать
    public void doInventory(Set<Remains> remainsSet) {
        merge(remainsSet);
        setValueToZeroIfNotContainsInSet(remainsSet);
    }
    
    public Optional<Remains> findByGoodsName(String goodsName) {
        return remainsDAO.findByGoodsName(goodsName);
    }

    public void update(Remains remains) {
        remainsDAO.update(remains);
    }
    
    private Set<Remains> xml2remainsSet(Document document) {
        Set<Remains> remainsSet = new HashSet<>();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                String goodsName = getNodeValueFromInventory(elem, "GoodsName");
                Integer value = Integer.parseInt(getNodeValueFromInventory(elem, "Value"));

                remainsSet.add(Remains.create(goodsName, value));
            }
        }
        
        return remainsSet;
    }
    
    private String getNodeValueFromInventory(Element elem, String name) {
        return elem.getElementsByTagName(name).item(0).getChildNodes().item(0).getNodeValue();
    }

    
    //Аналог этого: http://www.h2database.com/html/grammar.html#merge
    private void merge(Set<Remains> remainsList) {
        remainsList.stream().forEach((remains) -> {
            Optional<Remains> optRemainsFromDB = remainsDAO.findByGoodsName(remains.getGoodsName());
            if(optRemainsFromDB.isPresent()) {
                remains.setId(optRemainsFromDB.get().getId());
                remainsDAO.update(remains);
            } else {
                remainsDAO.insert(remains);
            }
        });
    }
    
    private void setValueToZeroIfNotContainsInSet(Set<Remains> remainsSet) {
        Set<String> goodsNameSet = remainsSet2goodsNameSet(remainsSet);
        
        remainsDAO.getAll().stream().
            filter((remains) -> (!goodsNameSet.contains(remains.getGoodsName()))).
            map((remains) -> {
                remains.setValue(0);
                return remains;
            }).
            forEach((remains) -> {
                remainsDAO.update(remains);
            });
    }
    

    
    private Set<String> remainsSet2goodsNameSet(Set<Remains> remainsSet) {        
        return remainsSet.
                stream().
                map((remains) -> {
                    return remains.getGoodsName();
                }).
                collect(Collectors.toSet());
    }
}