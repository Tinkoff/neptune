package ru.tinkoff.qa.neptune.core.api.data.format;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This class is designed to encapsulate a reference to {@link Type}.
 *
 * @param <T> is a type of a value to deserialize a string to.
 *            <p>WARNING!!!!!</p>
 *            No wildcard is allowed.
 */
public abstract class TypeRef<T> {

    private final Type type;

    public TypeRef() {
        type = getSuperclassTypeParameter(this.getClass());
    }

    private static Type getSuperclassTypeParameter(Class<?> clazz) {
        if (clazz.equals(Object.class)) {
            throw new IllegalArgumentException("No type parameter.");
        }

        Type superclass = clazz.getGenericSuperclass();
        if (superclass instanceof Class) {
            return getSuperclassTypeParameter(clazz.getSuperclass());
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public final Type getType() {
        return type;
    }

    /**
     * The auxiliary method that creates a {@link TypeReference} if com.fasterxml.jackson.* is used.
     * Jackson's {@link com.fasterxml.jackson.databind.ObjectMapper} doesn't work with types directly.
     *
     * @return an instance of {@link TypeReference}
     */
    public final TypeReference<T> jacksonTypeReference() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
    }

    @Override
    public final String toString() {
        return type.toString();
    }
}
