package ru.tinkoff.qa.neptune.http.api.response.body.data;

import com.google.gson.GsonBuilder;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

/**
 * This function is designed to deserialize json string to some object/array of custom objects. Deserialization is
 * performed by {@link com.google.gson.Gson}
 * @param <T> is a type of resulted object
 */
public class FromJson<T> implements Function<String, T> {

    private final Class<T> toGet;
    private final GsonBuilder builder;

    private FromJson(Class<T> toGet, GsonBuilder builder) {
        checkArgument(nonNull(toGet), "Class of an object to get from json string should not be a null value");
        checkArgument(nonNull(builder), "Builder should not be a null value");
        this.builder = builder;
        this.toGet = toGet;
    }

    /**
     * Creates an instance of {@link FromJson}
     *
     * @param toReturn is a type of resulted object to deserialize string body of an http response
     * @param builder is an instance of {@link GsonBuilder}
     * @param <T> is a type of resulted object
     * @return instance of {@link FromJson}
     */
    public static <T> Function<String, T> getFromJson(Class<T> toReturn, GsonBuilder builder) {
        return new FromJson<>(toReturn, builder);
    }

    /**
     * Creates an instance of {@link FromJson}
     *
     * @param toReturn is a type of resulted object to deserialize string body of an http response
     * @param <T> is a type of resulted object
     * @return instance of {@link FromJson}
     */
    public static <T> Function<String, T> getFromJson(Class<T> toReturn) {
        return getFromJson(toReturn, new GsonBuilder());
    }

    @Override
    public T apply(String s) {
        return builder.create().fromJson(s, toGet);
    }
}
