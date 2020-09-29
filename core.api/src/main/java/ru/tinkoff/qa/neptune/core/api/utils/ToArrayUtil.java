package ru.tinkoff.qa.neptune.core.api.utils;

import java.util.Arrays;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ArrayUtils.toObject;

public final class ToArrayUtil {

    private ToArrayUtil() {
        super();
    }

    /**
     * Accepts and object and return an array of {@code Object} when the object is an array.
     * {@code null} is returned otherwise
     *
     * @param o is an object whisch is supposed to be an array
     * @return an array of {@code Object} when given object is an array. {@code null} is returned otherwise
     */
    public static Object[] toArray(Object o) {
        if (o == null) {
            return null;
        }

        var cls = o.getClass();
        if (cls.isArray()) {
            Object[] result;
            if (cls.getComponentType().isPrimitive()) {
                if (byte[].class.equals(cls)) {
                    result = toObject((byte[]) o);
                } else if (short[].class.equals(cls)) {
                    result = toObject((short[]) o);
                } else if (int[].class.equals(cls)) {
                    result = toObject((int[]) o);
                } else if (long[].class.equals(cls)) {
                    result = toObject((long[]) o);
                } else if (float[].class.equals(cls)) {
                    result = toObject((float[]) o);
                } else if (double[].class.equals(cls)) {
                    result = toObject((double[]) o);
                } else if (boolean[].class.equals(cls)) {
                    result = toObject((boolean[]) o);
                } else {
                    result = toObject((char[]) o);
                }

            } else {
                result = (Object[]) o;
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * Returns string value of an array or an object.
     *
     * @param o is an object to get string value from
     * @return string value of an array or of an object
     */
    public static String stringValueOfObjectOrArray(Object o) {
        var array = toArray(o);
        if (array == null) {
            return valueOf(o);
        } else {
            return Arrays.toString(array);
        }
    }
}
