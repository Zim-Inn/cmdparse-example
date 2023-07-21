//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectUtil {
    public ReflectUtil() {
    }

    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);
        if (field != null) {
            try {
                field.set(obj, convert(value, field.getType()));
            } catch (IllegalAccessException var5) {
            }

        }
    }

    public static Object convert(Object object, Class<?> type) {
        if (object instanceof Number) {
            Number number = (Number)object;
            if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
                return number.byteValue();
            }

            if (type.equals(Short.TYPE) || type.equals(Short.class)) {
                return number.shortValue();
            }

            if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
                return number.intValue();
            }

            if (type.equals(Long.TYPE) || type.equals(Long.class)) {
                return number.longValue();
            }

            if (type.equals(Float.TYPE) || type.equals(Float.class)) {
                return number.floatValue();
            }

            if (type.equals(Double.TYPE) || type.equals(Double.class)) {
                return number.doubleValue();
            }
        }

        if (type.equals(String.class)) {
            return object == null ? "" : object.toString();
        } else if (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)) {
            return object;
        } else {
            return Boolean.parseBoolean(object == null ? "" : object.toString());
        }
    }

    public static Field getAccessibleField(final Object obj, final String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException("object can't be null");
        } else if (CheckUtil.isBlank(fieldName)) {
            throw new IllegalArgumentException("fieldName can't be blank");
        } else {
            Class<?> superClass = obj.getClass();

            while(superClass != Object.class) {
                try {
                    Field field = superClass.getDeclaredField(fieldName);
                    makeAccessible(field);
                    return field;
                } catch (NoSuchFieldException var4) {
                    superClass = superClass.getSuperclass();
                }
            }

            return null;
        }
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }
}
