/**
 * @author kourosh
 * @since 2019-11-04
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class Serializer {
    private Element root;
    private ArrayList<String> objects;
    private static XMLOutputter outputter = new XMLOutputter();

    public org.jdom2.Document serialize(Object obj) {

        Document document = initialize();
        serializeSubObject(obj.getClass(), obj);

        return document;
    }

    private Document initialize() {
        root = new Element("serialized");
        Document document = new Document(root);
        objects = new ArrayList<>();
        return document;
    }

    private Element serializeSubObject(Class objClass, Object obj) {
        Element rootObject;

        if (obj == null) { //is Null
            rootObject = new Element("value");
            rootObject.setText("null");
            return rootObject;

        } else if (objClass.isPrimitive()) { //is Primitive
            rootObject = new Element("value");
            rootObject.setText(obj.toString());
            return rootObject;

        } else if (objClass.isArray()) { //is Array
            Element reference = new Element("reference");
            reference.setText(getHashCode(obj));

            if (!objects.contains(getHashCode(obj))) {
                objects.add(getHashCode(obj)); //Add it to the hash code
                rootObject = createRootObject(objClass, obj);

                Object childObject;
                Class childObjectClass;
                for (int i = 0; i < Array.getLength(obj); i++) {
                    childObject = Array.get(obj, i);
                    childObjectClass = objClass.getComponentType();
                    rootObject.addContent(serializeSubObject(childObjectClass, childObject));
                }
            }
            return reference;

        } else { //is reference
            Element reference = new Element("reference");
            reference.setText(getHashCode(obj));

            if (!objects.contains(getHashCode(obj))) { //If we haven't create it make the object and make sure you added it to the root
                objects.add(getHashCode(obj));
                rootObject = createRootObject(objClass, obj);

                Object childObject;
                Class childObjectClass;
                for (Field field : objClass.getDeclaredFields()) {
                    Element fieldXML = new Element("field");
                    fieldXML.setAttribute("declaringClass", field.getDeclaringClass().getSimpleName());

                    fieldXML.setAttribute("name", field.getName());
                    field.setAccessible(true);
                    try {
                        childObject = field.get(obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new IllegalStateException(e);
                    }
                    childObjectClass = field.getType().isPrimitive() ? field.getType() : childObject.getClass();
                    fieldXML.addContent(serializeSubObject(childObjectClass, childObject));

                    rootObject.addContent(fieldXML);
                }
            }
            return reference;
        }

    }

    private Element createRootObject(Class objClass, Object obj) {
        Element rootObject;
        rootObject = new Element("object");
        rootObject.setAttribute("id", getHashCode(obj));
        rootObject.setAttribute("class", objClass.getName());
        if (objClass.isArray()) {
            rootObject.setAttribute("length", Array.getLength(obj) + "");
        }

        root.addContent(rootObject);
        return rootObject;
    }

    private String getHashCode(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    public static String toString(Document document) {
        outputter.setFormat(Format.getRawFormat());
        return outputter.outputString(document);
    }

    public static void toFile(Document document, String fileName) {
        try {
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(document, new FileOutputStream(new File(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
