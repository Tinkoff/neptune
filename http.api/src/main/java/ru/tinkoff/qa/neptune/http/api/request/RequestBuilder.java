package ru.tinkoff.qa.neptune.http.api.request;

import ru.tinkoff.qa.neptune.http.api.properties.end.point.DefaultEndPointOfTargetAPIProperty;
import ru.tinkoff.qa.neptune.http.api.request.body.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.response.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

import static com.google.common.base.Preconditions.*;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.net.URI.create;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.*;
import static ru.tinkoff.qa.neptune.http.api.properties.end.point.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;

public abstract class RequestBuilder<T> implements RequestSettings<RequestBuilder<T>> {
    final HttpRequest.Builder builder;
    private final QueryBuilder queryBuilder = new QueryBuilder();
    final RequestBody<?> body;
    private final TreeMap<String, List<String>> headersMap = new TreeMap<>(CASE_INSENSITIVE_ORDER);
    private URI baseURI;
    private String path = EMPTY;
    HttpResponse.BodyHandler<T> bodyHandler;

    RequestBuilder(RequestBody<?> body) {
        builder = HttpRequest.newBuilder();
        this.body = body;
        defineRequestMethodAndBody();
    }

    private static URI toURI(URL url) {
        checkNotNull(url);
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    abstract void defineRequestMethodAndBody();

    @Override
    public RequestBuilder<T> expectContinue(boolean enable) {
        builder.expectContinue(enable);
        return this;
    }

    @Override
    public RequestBuilder<T> version(HttpClient.Version version) {
        builder.version(version);
        return this;
    }

    @Override
    public RequestBuilder<T> header(String name, String... values) {
        var list = headersMap.computeIfAbsent(name, k -> new ArrayList<>());
        list.addAll(List.of(values));
        return this;
    }

    @Override
    public RequestBuilder<T> timeout(Duration duration) {
        builder.timeout(duration);
        return this;
    }

    @Override
    public RequestBuilder<T> queryParam(String name, FormValueDelimiters delimiter, boolean allowReserved, Object... values) {
        queryBuilder.addParameter(name, false, delimiter, allowReserved, values);
        return this;
    }

    @Override
    public RequestBuilder<T> queryParam(String name, boolean allowReserved, Object... values) {
        queryBuilder.addParameter(name, true, null, allowReserved, values);
        return this;
    }

    /**
     * Sets path relative to URI which is defined by {@link #baseURI(URI)} or {@link #baseURI(URL)}
     * or {@link #baseURI(String)} or {@link DefaultEndPointOfTargetAPIProperty#DEFAULT_END_POINT_OF_TARGET_API_PROPERTY}
     *
     * @param path a path relative to request URI
     * @return self-reference
     */
    public RequestBuilder<T> relativePath(String path) {
        checkArgument(isNotBlank(path), "Path should not be defined as a null/empty string");
        this.path = path;
        return this;
    }

    /**
     * Defines algorithms of the preparing of http requests.
     * These algorithms are wrapped/implemented by {@link RequestTuner}
     *
     * @param tuners objects that help to prepare resulted http-request
     * @return self-reference
     */
    public RequestBuilder<T> tuneWith(RequestTuner... tuners) {
        checkNotNull(tuners);
        checkArgument(tuners.length > 0, "There is nothing that helps to prepare http request");
        var thisBuilder = this;
        Arrays.stream(tuners).forEach(t -> t.setUp(thisBuilder));
        return this;
    }

    /**
     * Defines algorithms of the preparing of http requests.
     * These algorithms are wrapped/implemented by {@link RequestTuner}
     *
     * @param tuners objects that help to prepare resulted http-request
     * @return self-reference
     */
    @SafeVarargs
    public final RequestBuilder<T> tuneWith(Class<? extends RequestTuner>... tuners) {
        checkNotNull(tuners);
        checkArgument(tuners.length > 0, "There is nothing that helps to prepare http request");
        return tuneWith(Arrays.stream(tuners)
            .distinct()
            .map(c -> {
                try {
                    var constructor = c.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).toArray(RequestTuner[]::new));
    }

    /**
     * Defines algorithms of the preparing of http requests.
     * These algorithms are wrapped/implemented by {@link RequestTuner}
     *
     * @param tuners classes of objects that help to prepare resulted http request.
     *               Defined classes should declare no constructor or these classes should have
     *               a constructor with no parameter.
     * @return self-reference
     */
    public RequestBuilder<T> tuneWith(Iterable<RequestTuner> tuners) {
        checkNotNull(tuners);
        return tuneWith(stream(tuners.spliterator(), false).distinct().toArray(RequestTuner[]::new));
    }


    public HttpRequest build() {
        var newBuilder = builder.copy();

        var uri = ofNullable(baseURI).orElseGet(() -> {
            try {
                var basesURL = DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.get();
                checkState(nonNull(basesURL), "Base end point URI and value of the property "
                    + DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getName()
                    + " are not defined");
                return basesURL.toURI();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        });


        headersMap.forEach((s, strings) -> {
            var valueList = new ArrayList<>(strings);
            if (equalsIgnoreCase(s, "Content-Type")
                && !valueList.isEmpty()
                && (body != null && body instanceof MultiPartBody)) {
                var val = valueList.get(0);
                valueList.set(0, val + ";boundary=" + ((MultiPartBody) body).getBoundary());
            }

            valueList.forEach(s1 -> newBuilder.header(s, s1));
        });

        var requestURI = new URIBuilder(uri, queryBuilder, path).build();
        return new NeptuneHttpRequestImpl(newBuilder.uri(requestURI).build(), body);
    }

    /**
     * Defines base endpoint by URI. There should be URI that consists of scheme, host, port(optionally)
     * and context path (optionally).
     *
     * @param uri base URI of endpoint that consists of scheme, host, port(optionally)
     *            and context path (optionally).
     * @return self-reference
     */
    public RequestBuilder<T> baseURI(URI uri) {
        checkArgument(nonNull(uri), "URI is not defined");
        this.baseURI = uri;
        return this;
    }

    /**
     * Defines base endpoint by URL. There should be URL that consists of scheme, host, port(optionally)
     * and context path (optionally).
     *
     * @param url base URI of endpoint that consists of scheme, host, port(optionally)
     *            and context path (optionally).
     * @return self-reference
     */
    public RequestBuilder<T> baseURI(URL url) {
        return baseURI(toURI(url));
    }

    /**
     * Defines base endpoint URI by a string. There should be a string
     * which could be read as fully-qualified URI with scheme, host, port(optionally)
     * and context path (optionally).
     *
     * @param uriStr a string which could be read as fully-qualified URI with scheme, host, port(optionally)
     *               and context path (optionally)
     * @return self-reference
     * @see DefaultEndPointOfTargetAPIProperty
     */
    public RequestBuilder<T> baseURI(String uriStr) {
        return baseURI(create(uriStr));
    }

    /**
     * Defines a handler of response body.
     *
     * @param bodyHandler is a handler of response body
     * @param <R>         is a type of handled response body
     * @return self-reference
     */
    @SuppressWarnings("unchecked")
    public <R> RequestBuilder<R> responseBodyHandler(HttpResponse.BodyHandler<R> bodyHandler) {
        var result = (RequestBuilder<R>) this;
        result.bodyHandler = bodyHandler;
        return result;
    }

    /**
     * Creates an instance of {@link GetObjectFromBodyStepSupplier}. It builds a step-function that tries
     * to get an object from http response body.
     *
     * @param description is a description of resulted object
     * @param f           is a function that describes how to get resulted object from a body of http response
     * @param <R>         is a type of resulted object
     * @return an instance of {@link GetObjectFromBodyStepSupplier}
     */
    public abstract <R> GetObjectFromBodyStepSupplier<T, R> tryToReturn(String description, Function<T, R> f);

    /**
     * Creates an instance of {@link GetObjectFromBodyStepSupplier}. It builds a step-function that tries to get
     * a body of http response.
     *
     * @return an instance of {@link GetObjectFromBodyStepSupplier}
     */
    public abstract GetObjectFromBodyStepSupplier<T, T> tryToReturnBody();

    /**
     * Creates an instance of {@link GetObjectsFromIterableBodyStepSupplier}. It builds a step-function that tries
     * to get a list from http response body.
     *
     * @param description is a description of resulted {@link Iterable}
     * @param f           is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <R>         is a type of item of resulted list
     * @param <S>         is a type of calculated iterable
     * @return an instance of {@link GetObjectsFromIterableBodyStepSupplier}
     */
    public abstract <R, S extends Iterable<R>> GetObjectsFromIterableBodyStepSupplier<T, R, S> tryToReturnList(String description,
                                                                                                               Function<T, S> f);

    /**
     * Creates an instance of {@link GetObjectsFromArrayBodyStepSupplier}. It builds a step-function that tries
     * to get an array from http response body.
     *
     * @param description is a description of resulted array
     * @param f           is a function that describes how to get an array from a body of http response
     * @param <R>         is a type of item of resulted array
     * @return an instance of {@link GetObjectsFromArrayBodyStepSupplier}
     */
    public abstract <R> GetObjectsFromArrayBodyStepSupplier<T, R> tryToReturnArray(String description,
                                                                                   Function<T, R[]> f);

    /**
     * Creates an instance of {@link GetObjectFromIterableBodyStepSupplier}. It builds a step-function that tries to get
     * an object from some {@link Iterable} which is calculated by http response body.
     *
     * @param description is a description of resulted object
     * @param f           is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <R>         is a type of resulted object
     * @param <S>         if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableBodyStepSupplier}
     */
    public abstract <R, S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier<T, R> tryToReturnItem(String description,
                                                                                                           Function<T, S> f);

    /**
     * Creates an instance of {@link GetObjectFromArrayBodyStepSupplier}. It builds a step-function that tries to get an
     * object from some  array which is calculated by http response body.
     *
     * @param description is a description of resulted object
     * @param f           is a function that describes how to get an array from a body of http response
     * @param <R>         is a type of resulted object
     * @return an instance of {@link GetObjectFromArrayBodyStepSupplier}
     */
    public abstract <R> GetObjectFromArrayBodyStepSupplier<T, R> tryToReturnArrayItem(String description,
                                                                                      Function<T, R[]> f);
}
