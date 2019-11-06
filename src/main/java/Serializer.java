/**
 * @author kourosh
 * @since 2019-11-04
 */

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Serializer {
    private Document document;
    private Element root;
    private ArrayList<String> objects = new ArrayList<>();

    public static void main (String[] args) throws ParserConfigurationException, TransformerException, IllegalAccessException {

        Serializer serializer = new Serializer();
        ClassB object = new ClassB(9);
        ClassB object2 = new ClassB(10);
        object.setSwag(object2);
        object2.setSwag(object);
        serializer.serialize(object);

    }

    public Serializer() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        root = document.createElement("serialized");
    }


    public void serialize(Object obj) throws TransformerException, IllegalAccessException {

        serializeSubObject(obj.getClass(), obj);
        document.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("hello.txt"));
        transformer.transform(domSource, streamResult);

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
                rootObject = document.createElement("object");
                rootObject.setAttribute("class", objClass.getSimpleName());
                rootObject.setAttribute("length", Array.getLength(obj) + "");
                rootObject.setAttribute("id", getHashCode(obj));

                for (int i = 0; i < Array.getLength(obj); i++) {
                    rootObject.appendChild(serializeSubObject(objClass.getComponentType(), Array.get(obj, i)));
                }
                root.appendChild(rootObject);
            }
            return reference;

        } else { //is reference
            Element reference = document.createElement("reference");
            reference.setTextContent(getHashCode(obj));

            if (!objects.contains(getHashCode(obj))) { //If we haven't create it make the object and make sure you added it to the root
                objects.add(getHashCode(obj));
                rootObject = document.createElement("object");
                rootObject.setAttribute("id", getHashCode(obj));
                rootObject.setAttribute("class", objClass.getSimpleName());

                for (Field field : objClass.getDeclaredFields()) {
                    Element fieldXML = document.createElement("field");
                    fieldXML.setAttribute("declaringClass", field.getType().getSimpleName());
                    fieldXML.setAttribute("name", field.getName());
                    field.setAccessible(true);
                    fieldXML.appendChild(serializeSubObject(field.getType(), field.get(obj)));

                    rootObject.appendChild(fieldXML);
                }
                root.appendChild(rootObject);
            }

            return reference;
        }

    }

    private String getHashCode(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }
}
