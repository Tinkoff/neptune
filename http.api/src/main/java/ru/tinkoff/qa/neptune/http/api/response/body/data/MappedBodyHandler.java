package ru.tinkoff.qa.neptune.http.api.response.body.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;

import javax.xml.parsers.DocumentBuilder;
import java.net.http.HttpResponse;
import java.util.function.Function;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.net.http.HttpResponse.BodySubscribers.mapping;
import static org.glassfish.jersey.internal.guava.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.FromJson.getFromJson;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.GetDocument.getDocument;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.GetMapped.getMapped;

public class MappedBodyHandler<S, T> implements HttpResponse.BodyHandler<T> {

    private final HttpResponse.BodyHandler<S> upstreamBodyHandler;
    private final Function<S, T> mapper;

    private MappedBodyHandler(HttpResponse.BodyHandler<S> upstreamBodyHandler, Function<S, T> mapper) {
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

    /**
     * Creates a new body handler to convert a response body to an object deserialized by {@link com.google.gson.Gson}.
     * It is considered that response has a body of json format.
     *
     * @param toReturn is a class of an object to be deserialized from json
     * @param builder  an instance of {@link GsonBuilder}
     * @param <T>      is a type of an object to be deserialized from json
     * @return a new {@link MappedBodyHandler}
     */
    public static <T> MappedBodyHandler<String, T> json(Class<T> toReturn, GsonBuilder builder) {
        return mapped(ofString(), getFromJson(toReturn, builder));
    }

    /**
     * Creates a new body handler to convert a response body to an object deserialized by {@link com.google.gson.Gson}.
     * It is considered that response has a body of json format.
     *
     * @param toReturn is a class of an object to be deserialized from json
     * @param <T>      is a type of an object to be deserialized from json
     * @return a new {@link MappedBodyHandler}
     */
    public static <T> MappedBodyHandler<String, T> json(Class<T> toReturn) {
        return mapped(ofString(), getFromJson(toReturn));
    }

    /**
     * Creates a new body handler to convert a response body to a {@link org.w3c.dom.Document}.
     * It is considered that response has a body of xml or html format.
     *
     * @param documentBuilder an instance of {@link DocumentBuilder}
     * @return a new {@link MappedBodyHandler}
     */
    public static MappedBodyHandler<String, org.w3c.dom.Document> document(DocumentBuilder documentBuilder) {
        return mapped(ofString(), getDocument(documentBuilder));
    }

    /**
     * Creates a new body handler to convert a response body to a {@link org.jsoup.nodes.Document}.
     * It is considered that response has a body of xml or html format.
     *
     * @return a new {@link MappedBodyHandler}
     */
    public static MappedBodyHandler<String, org.jsoup.nodes.Document> document() {
        return mapped(ofString(), getDocument());
    }

    /**
     * Creates a new body handler to convert a response body to an object deserialized by {@link ObjectMapper}
     * It is considered that response has a body of xml/json/any other format readable by defined object of
     * {@link ObjectMapper}.
     *
     * @param toReturn is a class of an object to be deserialized from the response body
     * @param mapper   is an instance of {@link ObjectMapper}
     * @param <T>      is a type of an object to be deserialized from the response body
     * @return a new {@link MappedBodyHandler}
     */
    public static <T> MappedBodyHandler<String, T> mappedByJackson(Class<T> toReturn, ObjectMapper mapper) {
        return mapped(ofString(), getMapped(toReturn, mapper));
    }

    /**
     * Creates a new body handler to convert a response body to an object deserialized by {@link ObjectMapper}
     * It is considered that response has a body of xml/json/any other format readable by by default object of
     * {@link ObjectMapper}.
     *
     * @param toReturn is a class of an object to be deserialized from the response body
     * @param <T>      is a type of an object to be deserialized from the response body
     * @return a new {@link MappedBodyHandler}
     */
    public static <T> MappedBodyHandler<String, T> mappedByJackson(Class<T> toReturn) {
        return mapped(ofString(), getMapped(toReturn));
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
        return mapping(upstreamBodyHandler.apply(responseInfo), mapper);
    }
}
