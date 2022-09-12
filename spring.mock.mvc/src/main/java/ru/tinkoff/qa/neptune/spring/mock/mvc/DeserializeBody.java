package ru.tinkoff.qa.neptune.spring.mock.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.mock.web.MockHttpServletResponse;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

import java.io.UnsupportedEncodingException;
import java.util.function.Function;

import static java.util.Objects.nonNull;

class DeserializeBody<T, R> implements Function<String, R> {

    private final DataTransformer transformer;
    private final Class<T> deserializeToClsRef;
    private final TypeReference<T> deserializeToTypeRef;
    private final Function<T, R> getAs;

    private DeserializeBody(Class<T> deserializeToClsRef, DataTransformer transformer, Function<T, R> getAs) {
        this.transformer = transformer;
        this.deserializeToClsRef = deserializeToClsRef;
        this.getAs = getAs;
        this.deserializeToTypeRef = null;
    }

    private DeserializeBody(TypeReference<T> deserializeToTypeRef, DataTransformer transformer, Function<T, R> getAs) {
        this.transformer = transformer;
        this.getAs = getAs;
        this.deserializeToClsRef = null;
        this.deserializeToTypeRef = deserializeToTypeRef;
    }

    static <T, R> Function<MockHttpServletResponse, T> deserializeBodyAndGet(DataTransformer transformer,
                                                                             Class<R> deserializeTo,
                                                                             Function<R, T> howToGet) {
        return ((Function<MockHttpServletResponse, String>) mockHttpServletResponse -> {
            try {
                return mockHttpServletResponse.getContentAsString();
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException(e);
            }
        }).andThen(new DeserializeBody<>(deserializeTo, transformer, howToGet));
    }

    static <T, R> Function<MockHttpServletResponse, T> deserializeBodyAndGet(DataTransformer transformer,
                                                                             TypeReference<R> deserializeTo,
                                                                             Function<R, T> howToGet) {
        return ((Function<MockHttpServletResponse, String>) mockHttpServletResponse -> {
            try {
                return mockHttpServletResponse.getContentAsString();
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException(e);
            }
        }).andThen(new DeserializeBody<>(deserializeTo, transformer, howToGet));
    }

    @Override
    public R apply(String s) {
        T t;
        if (nonNull(deserializeToClsRef)) {
            t = transformer.deserialize(s, deserializeToClsRef);
        } else {
            t = transformer.deserialize(s, deserializeToTypeRef);
        }

        return getAs.apply(t);
    }
}
