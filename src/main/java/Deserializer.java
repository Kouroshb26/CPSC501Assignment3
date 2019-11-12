import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

/**
 * @author kourosh
 * @since 2019-11-10
 */
public class Deserializer {

    private HashMap<String, Object> objectReference = new HashMap<>();
    private Element root;

    public static void main(String[] args) {
        Serializer serializer = new Serializer();
        Object object = new Deserializer().deserialize(serializer.serialize("12343"));

    }

    public Object deserialize(org.jdom2.Document document) {
        root = document.getRootElement();
        return deserialize(root.getChildren().get(0));
    }

    public Object deserialize(Element element) {
        try {
            if (element.getName().equals("object")) {
                Class objectClass = Class.forName(element.getAttributeValue("class"));
                if (objectClass.isArray()) { //Object that is array
                    int length = element.getAttribute("length").getIntValue();
                    Object object = Array.newInstance(objectClass.getComponentType(), length);
                    objectReference.put(element.getAttributeValue("id"), object);
                    for (int i = 0; i < length; i++) {
                        Object fieldObject;
                        if (objectClass.getComponentType().isPrimitive()) {
                            fieldObject = deserializePrimitive(element.getChildren().get(i), objectClass.getComponentType());
                        } else {
                            fieldObject = deserialize(element.getChildren().get(i));
                        }

                        Array.set(object, i, fieldObject);
                    }
                    return object;
                } else { //Object
                    Object object = objectClass.newInstance();
                    objectReference.put(element.getAttributeValue("id"), object);

                    for (Element xmlField : element.getChildren()) {
                        Field field = objectClass.getDeclaredField(xmlField.getAttributeValue("name"));
                        field.setAccessible(true);
                        Object fieldObject;

                        if (!Modifier.isFinal(field.getModifiers())) {
                            if (field.getType().isPrimitive()) {
                                fieldObject = deserializePrimitive(xmlField.getChildren().get(0), field.getType());
                            } else {
                                fieldObject = deserialize(xmlField.getChildren().get(0));
                            }
                            field.set(object, fieldObject);
                        } else {
                            System.out.println();
                        }
                    }
                    return object;
                }

            } else if (element.getName().equals("reference")) {
                if (objectReference.containsKey(element.getValue())) { // Has already been created
                    return objectReference.get(element.getValue());

                } else { //Has not been created
                    if (element.getValue().equals("null")) {
                        return null;
                    }
                    for (Element xmlObject : root.getChildren()) {
                        if (xmlObject.getName().equals("object") &&
                            xmlObject.getAttributeValue("id").equals(element.getValue())) {
                            return deserialize(xmlObject);
                        }

                    }
                }

            } else if (element.getName().equals("value")) {
                if (element.getValue().equals("null")) {
                    return null;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | DataConversionException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error creating object");
        }
        throw new IllegalStateException("Could not create object");
    }

    public Object deserializePrimitive(Element element, Class objectClass) {
        String field = element.getValue();
        if (int.class.equals(objectClass)) {
            return Integer.parseInt(field);
        } else if (boolean.class.equals(objectClass)) {
            return Boolean.parseBoolean(field);
        } else if (double.class.equals(objectClass)) {
            return Double.parseDouble(field);
        } else if (char.class.equals(objectClass)) {
            return field.charAt(0);
        } else if (long.class.equals(objectClass)) {
            return Long.parseLong(field);
        }
        throw new IllegalStateException("Could not parse primitive");
    }

}
