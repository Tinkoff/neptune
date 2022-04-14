package ru.tinkoff.qa.neptune.http.api.request;

import ru.tinkoff.qa.neptune.http.api.properties.end.point.DefaultEndPointOfTargetAPIProperty;
import ru.tinkoff.qa.neptune.http.api.request.body.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.*;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.net.URI.create;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.*;
import static ru.tinkoff.qa.neptune.http.api.properties.end.point.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;

public abstract class RequestBuilder implements RequestSettings<RequestBuilder> {
    final HttpRequest.Builder builder;
    private final QueryBuilder queryBuilder = new QueryBuilder();
    final RequestBody<?> body;
    private final TreeMap<String, List<String>> headersMap = new TreeMap<>(CASE_INSENSITIVE_ORDER);
    private URI baseURI;
    private String path = EMPTY;

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
    public RequestBuilder expectContinue(boolean enable) {
        builder.expectContinue(enable);
        return this;
    }

    @Override
    public RequestBuilder version(HttpClient.Version version) {
        builder.version(version);
        return this;
    }

    @Override
    public RequestBuilder header(String name, String... values) {
        var list = headersMap.computeIfAbsent(name, k -> new ArrayList<>());
        list.addAll(List.of(values));
        return this;
    }

    @Override
    public RequestBuilder timeout(Duration duration) {
        builder.timeout(duration);
        return this;
    }

    @Override
    public RequestBuilder queryParam(String name, FormValueDelimiters delimiter, boolean allowReserved, Object... values) {
        queryBuilder.addParameter(name, false, delimiter, allowReserved, values);
        return this;
    }

    @Override
    public RequestBuilder queryParam(String name, boolean allowReserved, Object... values) {
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
    public RequestBuilder relativePath(String path) {
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
    public RequestBuilder tuneWith(RequestTuner... tuners) {
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
    public final RequestBuilder tuneWith(Class<? extends RequestTuner>... tuners) {
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
    public RequestBuilder tuneWith(Iterable<RequestTuner> tuners) {
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
    public RequestBuilder baseURI(URI uri) {
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
    public RequestBuilder baseURI(URL url) {
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
    public RequestBuilder baseURI(String uriStr) {
        return baseURI(create(uriStr));
    }
}
