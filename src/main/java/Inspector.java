import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class Inspector {
    private ArrayList<Integer> visited = new ArrayList<>();

    public void inspect(Object obj) {
        System.out.println("Printing out the contents of what was sent over");
        Class c = obj.getClass();
        inspectClass(c, obj, true, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        String prefix = getPrefixString(depth);
        System.out.println(prefix + "Object Hash code is " + Integer.toHexString(System.identityHashCode(obj)));
        if (visited.contains(System.identityHashCode(obj))) {
            System.out.println(prefix + "Already visited this object");
            return;
        }
        visited.add(System.identityHashCode(obj));

        if (c.isArray()) {
            array(c, obj, recursive, depth, prefix);
        }
        System.out.println(prefix + "-----------------Start--------------------------");
        //Name
        System.out.println(prefix + "The class name is " + c.getSimpleName());

        //Declared Fields
        fields(c, obj, recursive, depth, prefix);

        System.out.println(prefix + "-----------------End--------------------------");

    }

    private void array(Class c, Object obj, boolean recursive, int depth, String prefix) {
        System.out.println(prefix + "This class is an array type " + c.getSimpleName());
        System.out.println(prefix + "This is an array of " + c.getComponentType().getSimpleName());
        System.out.println(prefix + "This array length is  " + Array.getLength(obj));
        System.out.println(prefix + "The contents of the array is : " + objectToString(obj, c));
        for (int i = 0; i < Array.getLength(obj); i++) {
            if (!c.getComponentType().isPrimitive() && recursive && Array.get(obj, i) != null) {
                inspectClass(getChildClass(c.getComponentType(), Array.get(obj, i)), Array.get(obj, i), recursive, depth + 1);
            }
        }
    }

    private void fields(Class c, Object obj, boolean recursive, int depth, String prefix) {
        if (c.getDeclaredFields().length > 0) {
            System.out.println();
            System.out.println(prefix + "The fields are:");
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(prefix + "===========================================");
                System.out.println(prefix + " The name is " + field.getName());
                System.out.println(prefix + " The type is " + field.getType().getSimpleName());
                System.out.println(prefix + " The modifiers are " + Modifier.toString(field.getModifiers()));
                try {
                    if (obj != null) {
                        Object fieldObject = field.get(obj);
                        System.out.println(prefix + " The value of the field is " + objectToString(fieldObject, field.getType()));
                        if (recursive && !field.getType().isPrimitive() && fieldObject != null) {
                            inspectClass(getChildClass(field.getType(), fieldObject), fieldObject, recursive, depth + 1);
                        }
                    } else {
                        System.out.println(prefix + "Object is null, thus no field value can be accessed");
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    String getPrefixString(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("\t");
        }
        return stringBuilder.toString();
    }

    String objectToString(Object obj, Class<?> type) {
        if (obj == null) {
            return "null";
        } else if (type.isPrimitive()) {
            return obj.toString();

        } else if (type.isArray()) {
            StringBuilder result = new StringBuilder();
            result.append("[");
            for (int i = 0; i < Array.getLength(obj); i++) {
                result.append(objectToString(Array.get(obj, i), type.getComponentType()));
                if (i + 1 != Array.getLength(obj)) {
                    result.append(",");
                }
            }
            result.append("]");
            return result.toString();

        } else {
            return type.getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
        }
    }

    private Class getChildClass(Class objClass, Object childObject) {
        Class childObjectClass;
        if (childObject == null) {
            childObjectClass = null;
        } else if (objClass.isPrimitive()) {
            childObjectClass = objClass;
        } else {
            childObjectClass = childObject.getClass();
        }
        return childObjectClass;
    }
}
