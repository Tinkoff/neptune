package ru.tinkoff.qa.neptune.http.api.response.body.data;

import com.google.gson.GsonBuilder;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

/**
 *
 * @param <T>
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
     *
     * @param toReturn
     * @param builder
     * @param <T>
     * @return
     */
    public static <T> FromJson<T> getFromJson(Class<T> toReturn, GsonBuilder builder) {
        return new FromJson<>(toReturn, builder);
    }

    /**
     *
     * @param toReturn
     * @param <T>
     * @return
     */
    public static <T> FromJson<T> getFromJson(Class<T> toReturn) {
        return getFromJson(toReturn, new GsonBuilder());
    }

    @Override
    public T apply(String s) {
        return builder.create().fromJson(s, toGet);
    }
}
