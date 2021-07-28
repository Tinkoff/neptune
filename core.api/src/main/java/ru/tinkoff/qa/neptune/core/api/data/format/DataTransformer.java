package ru.tinkoff.qa.neptune.core.api.data.format;

/**
 * This interface allows to not force users to use specific serialization / deserialization library.
 * It wraps serialization / deserialization to make use it fluently.
 */
public interface DataTransformer {

    /**
     * Deserializes string to java object.
     *
     * @param string is original formatted string
     * @param cls    is a class of java object to deserialize string
     * @param <T>    is a type of resulted object
     * @return result of deserialization
     */
    <T> T deserialize(String string, Class<T> cls);

    /**
     * Deserializes string to java object.
     *
     * @param string is original formatted string
     * @param type   is a reference to type of java object to deserialize string
     * @param <T>    is a type of resulted object
     * @return result of deserialization
     */
    <T> T deserialize(String string, TypeRef<T> type);

    /**
     * Serializes java object to formatted string.
     *
     * @param obj is original java object
     * @return result of serialization
     */
    String serialize(Object obj);
}
