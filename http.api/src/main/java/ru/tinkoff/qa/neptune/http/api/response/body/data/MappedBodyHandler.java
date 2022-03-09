package ru.tinkoff.qa.neptune.http.api.response.body.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.net.http.HttpResponse.BodySubscribers.mapping;
import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;
import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.XML;

public class MappedBodyHandler<S, T> implements HttpResponse.BodyHandler<T> {

    private final HttpResponse.BodyHandler<S> upstreamBodyHandler;
    private final Function<S, T> mapper;

    protected MappedBodyHandler(HttpResponse.BodyHandler<S> upstreamBodyHandler, Function<S, T> mapper) {
        checkNotNull(upstreamBodyHandler, "Upstream body handler should be defined");
        checkNotNull(mapper, "Mapping function should be defined");

        this.upstreamBodyHandler = upstreamBodyHandler;
        this.mapper = mapper;
    }

    /**
     * Creates a new body handler to convert a response body to desired value
     *
     * @param upstreamBodyHandler is handler of a response body
     * @param mapper              is a function that converts response body to desired value
     * @param <S>                 is an original type of a response body
     * @param <T>                 is a type of a final value of the response body
     * @return a new {@link MappedBodyHandler}
     */
    public static <S, T> MappedBodyHandler<S, T> mapped(HttpResponse.BodyHandler<S> upstreamBodyHandler,
                                                        Function<S, T> mapper) {
        return new MappedBodyHandler<>(upstreamBodyHandler, mapper);
    }


    public static <T> MappedBodyHandler<String, T> deserialized(Class<T> toReturn, ObjectMapper mapper) {
        return mapped(ofString(), new Deserialized<>(toReturn, mapper));
    }

    public static <T> MappedBodyHandler<String, T> deserialized(TypeReference<T> toReturn, ObjectMapper mapper) {
        return mapped(ofString(), new Deserialized<>(toReturn, mapper));
    }

    public static <T> MappedBodyHandler<String, T> json(Class<T> toReturn) {
        return deserialized(toReturn, JSON.getMapper());
    }

    public static <T> MappedBodyHandler<String, T> json(TypeReference<T> toReturn) {
        return deserialized(toReturn, JSON.getMapper());
    }

    public static <T> MappedBodyHandler<String, T> xml(Class<T> toReturn) {
        return deserialized(toReturn, XML.getMapper());
    }

    public static <T> MappedBodyHandler<String, T> xml(TypeReference<T> toReturn) {
        return deserialized(toReturn, XML.getMapper());
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
        return mapping(upstreamBodyHandler.apply(responseInfo), mapper);
    }
}
