package ru.tinkoff.qa.neptune.spring.data.data.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

/**
 * Serializes entities and other objects for different purposes.
 *
 * @param <T>      is a type of value to serialize
 * @param <RESULT> is a type of serialized value
 */
public abstract class DataSerializer<T, RESULT> {

    final ObjectMapper mapper;

    private DataSerializer(JsonInclude.Include toInclude) {
        this.mapper = new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S"))
                .setSerializationInclusion(toInclude);
    }

    /**
     * Serializes a single object into string
     *
     * @param toInclude which field values should be included
     * @param serialize is an input object
     * @param <T>       is a type of input object
     * @return resulted string
     */
    public static <T> String serializeObject(JsonInclude.Include toInclude, T serialize) {
        return new DataSerializer<T, String>(toInclude) {

            @Override
            public String serialize(T toSerialize) {
                return this.serializeOne(toSerialize);
            }
        }.serialize(serialize);
    }

    /**
     * Transforms {@link Iterable} to {@link Stream} of serialized values
     *
     * @param toInclude which field values should be included
     * @param serialize is an input iterable
     * @param <R>       is a time of item from {@link Iterable}
     * @param <T>       if a type of {@link Iterable}
     * @return stream of serialized values
     */
    public static <R, T extends Iterable<R>> Stream<String> serializeObjects(JsonInclude.Include toInclude, T serialize) {
        checkNotNull(serialize);
        return new DataSerializer<T, Stream<String>>(toInclude) {
            @Override
            public Stream<String> serialize(T toSerialize) {
                return stream(toSerialize.spliterator(), false)
                        .map(this::serializeOne);
            }
        }.serialize(serialize);
    }

    /**
     * Transforms an array to {@link Stream} of serialized values
     *
     * @param toInclude which field values should be included
     * @param serialize is an input array
     * @param <T>       is a type of array item
     * @return stream of serialized values
     */
    public static <T> Stream<String> serializeObjects(JsonInclude.Include toInclude, T[] serialize) {
        checkNotNull(serialize);

        return new DataSerializer<T[], Stream<String>>(toInclude) {
            @Override
            public Stream<String> serialize(T[] toSerialize) {
                return Arrays.stream(toSerialize)
                        .map(this::serializeOne);
            }
        }.serialize(serialize);
    }

    String serializeOne(Object o) {
        if (isLoggable(o)) {
            return valueOf(o);
        }

        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "could not serialize value";
        }
    }

    public abstract RESULT serialize(T toSerialize);
}
