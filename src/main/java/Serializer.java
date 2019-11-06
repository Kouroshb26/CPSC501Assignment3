/**
 * @author kourosh
 * @since 2019-11-04
 */

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Serializer {
    private Document document;
    private Element root;
    private ArrayList<String> objects;
    private Transformer transformer;

    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IllegalAccessException {

        Serializer serializer = new Serializer();
        ClassB object = new ClassB(9);
        ClassB object2 = new ClassB(10);
        object.setSwag(object2);
        object2.setSwag(object);
        serializer.serialize(object);
        serializer.serialize(Arrays.asList(object, object2));
    }

    public void serialize(Object obj) throws TransformerException, IllegalAccessException, ParserConfigurationException {

        initialize();
        serializeSubObject(obj.getClass(), obj);
        document.appendChild(root);

        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("hello.txt"));
        transformer.transform(domSource, streamResult);
    }

    private void initialize() throws ParserConfigurationException, TransformerConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        root = document.createElement("serialized");
        objects = new ArrayList<>();

        transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    }

    private Element serializeSubObject(Class objClass, Object obj) throws IllegalAccessException {
        Element rootObject;

        if (obj == null) {
            rootObject = document.createElement("value");
            rootObject.setTextContent("null");
            return rootObject;

        } else if (objClass.isPrimitive()) { //is Primitive
            rootObject = document.createElement("value");
            rootObject.setTextContent(obj.toString());
            return rootObject;

        } else if (objClass.isArray()) { //is Array
            Element reference = document.createElement("reference");
            reference.setTextContent(getHashCode(obj));

            if (!objects.contains(getHashCode(obj))) {
                objects.add(getHashCode(obj)); //Add it to the hash code
                rootObject = createRootObject(objClass, obj);

                Object childObject;
                Class childObjectClass;
                for (int i = 0; i < Array.getLength(obj); i++) {
                    childObject = Array.get(obj, i);
                    childObjectClass = objClass.getComponentType().isPrimitive() ? objClass.getComponentType() : childObject.getClass();
                    rootObject.appendChild(serializeSubObject(childObjectClass, childObject));
                }
            }
            return reference;

        } else { //is reference
            Element reference = document.createElement("reference");
            reference.setTextContent(getHashCode(obj));

            if (!objects.contains(getHashCode(obj))) { //If we haven't create it make the object and make sure you added it to the root
                objects.add(getHashCode(obj));
                rootObject = createRootObject(objClass, obj);

                Object childObject;
                Class childObjectClass;
                for (Field field : objClass.getDeclaredFields()) {
                    Element fieldXML = document.createElement("field");
                    fieldXML.setAttribute("declaringClass", field.getType().getSimpleName());
                    fieldXML.setAttribute("name", field.getName());
                    field.setAccessible(true);
                    childObject = field.get(obj);
                    childObjectClass = field.getType().isPrimitive() ? field.getType() : childObject.getClass();
                    fieldXML.appendChild(serializeSubObject(childObjectClass, childObject));

                    rootObject.appendChild(fieldXML);
                }
            }

            return reference;
        }

    }

    private Element createRootObject(Class objClass, Object obj) {
        Element rootObject;
        rootObject = document.createElement("object");
        rootObject.setAttribute("id", getHashCode(obj));
        rootObject.setAttribute("class", objClass.getName());
        if (objClass.isArray()) {
            rootObject.setAttribute("length", Array.getLength(obj) + "");
        }

        root.appendChild(rootObject);
        return rootObject;
    }

    private String getHashCode(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    //This is for debugging only
    public String printElement(Node element) {
        try {
            StringWriter buffer = new StringWriter();
            transformer.transform(new DOMSource(element),
                new StreamResult(buffer));
            return buffer.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
