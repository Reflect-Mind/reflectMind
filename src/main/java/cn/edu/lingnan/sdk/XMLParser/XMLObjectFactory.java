package cn.edu.lingnan.sdk.XMLParser;

import cn.edu.lingnan.sdk.CGLibProxy.Filter;
import cn.edu.lingnan.sdk.CGLibProxy.FilterAdapter;
import cn.edu.lingnan.sdk.CGLibProxy.ProxyFactory;
import cn.edu.lingnan.sdk.controller.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 */
public class XMLObjectFactory implements Serializable{
    private HashMap<Class, Object> hashMap = new HashMap<>();
    private Object targetObject;
    private Object elementObject;
    private Node node;
    public XMLObjectFactory(){}
    public XMLObjectFactory(String path){
        File file = new File(this.getClass().getResource(path).getPath());
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/package";
            this.node = (Node) xPath.compile(expression).evaluate(document, XPathConstants.NODE);
            this.ergodicChildElements(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Node> transform(NodeList list){
        List<Node> nodes = new ArrayList<>();
        for (int count = 0; count < list.getLength(); count++){
            Node node = list.item(count);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    //遍历子元素
    private void ergodicChildElements(Node node){
        List<Node> nodes = this.transform(node.getChildNodes());
        for (int count = 0; count < nodes.size(); count++){
            Node element = nodes.get(count);
            try {
                switch (element.getNodeName()) {
                    case "class":
                        this.ergodicChildElements(element);
                        break;
                    case "controller":
                        this.ergodicAttribute(element);//遍历属性
                        this.ergodicChildElements(element);//遍历直接子的元素
                        break;
                    case "filter":
                        Class clz = Class.forName(element.getTextContent());
                        Constructor<Filter<Controller>> constructor = clz
                                .getConstructor(Object.class, Filter.class);
                        if (this.elementObject == null)
                            this.elementObject = constructor.newInstance(this.targetObject, null);
                        else
                            this.elementObject = constructor.newInstance(this.targetObject, this.elementObject);
                        if (count == nodes.size() - 1){
                            //代理
                            FilterAdapter filterAdapter = new FilterAdapter(targetObject, (Filter) this.elementObject);
                            Object object = ProxyFactory.getProxyInstance(this.targetObject, filterAdapter);
                            this.hashMap.put(targetObject.getClass(), object);
                        }
                        break;
                    default:
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //遍历子属性
    private void ergodicAttribute(Node node){
        NamedNodeMap map = node.getAttributes();
        for (int count = 0; count < map.getLength(); count++){
            Node element = map.item(count);
            try {
                switch (element.getNodeName()) {
                    case "class":
                        this.targetObject = Class.forName(element.getNodeValue()).newInstance();
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public <T> T getObject(Class<?> clazz){
        T instance = (T) this.hashMap.get(clazz);
        return instance;
    }

//    public static void main(String[] args) throws FileNotFoundException {
//        XMLObjectFactory factory = new XMLObjectFactory("/xml/default.xml");
//        BirthdayStatisticController controller = factory.getObject(BirthdayStatisticController.class);
//        controller.update(null,null);
//    }
}
