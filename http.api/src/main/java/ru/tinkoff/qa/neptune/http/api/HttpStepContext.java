package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.response.*;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.net.HttpCookie.parse;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;
import static ru.tinkoff.qa.neptune.http.api.cookies.AddHttpCookiesActionSupplier.addHttpCookies;
import static ru.tinkoff.qa.neptune.http.api.cookies.DeleteHttpCookiesActionSupplier.deleteCookies;
import static ru.tinkoff.qa.neptune.http.api.cookies.GetHttpCookiesSupplier.httpCookies;
import static ru.tinkoff.qa.neptune.http.api.properties.authentification.DefaultHttpAuthenticatorProperty.DEFAULT_HTTP_AUTHENTICATOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.cookies.DefaultHttpCookieManagerProperty.DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.executor.DefaultHttpExecutorProperty.DEFAULT_HTTP_EXECUTOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.priority.DefaultHttpPriorityProperty.DEFAULT_HTTP_PRIORITY_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.protocol.version.DefaultHttpProtocolVersionProperty.DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.proxy.DefaultHttpProxySelectorProperty.DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.redirect.DefaultHttpRedirectProperty.DEFAULT_HTTP_REDIRECT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslContextProperty.DEFAULT_HTTP_SSL_CONTEXT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslParametersProperty.DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.DefaultConnectTimeOutProperty.DEFAULT_CONNECT_TIME_OUT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

public class HttpStepContext extends Context<HttpStepContext> {

    private CookieStore cookieStore = new CookieManager().getCookieStore();

    public static HttpStepContext http() {
        return getCreatedContextOrCreate(HttpStepContext.class);
    }

    CookieStore getCookieStore() {
        return cookieStore;
    }

    public HttpClient getCurrentClient() {
        final var builder = HttpClient.newBuilder();
        ofNullable(DEFAULT_CONNECT_TIME_OUT_PROPERTY.get())
            .ifPresent(builder::connectTimeout);

        ofNullable(DEFAULT_HTTP_AUTHENTICATOR_PROPERTY.get()).ifPresent(builder::authenticator);

        var cookieManager = ofNullable(DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY.get())
            .orElseGet(CookieManager::new);

        var allCookies = cookieStore.getCookies();
        var uris = cookieStore.getURIs();
        var newStore = cookieManager.getCookieStore();

        uris.forEach(uri -> {
            var indexedCookies = cookieStore.get(uri);
            indexedCookies.forEach(c -> newStore.add(uri, c));
            allCookies.removeAll(indexedCookies);
        });

        allCookies.forEach(c -> newStore.add(null, c));

        builder.cookieHandler(cookieManager);

        ofNullable(DEFAULT_HTTP_EXECUTOR_PROPERTY.get()).ifPresent(builder::executor);
        ofNullable(DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY.get()).ifPresent(builder::version);
        ofNullable(DEFAULT_HTTP_PRIORITY_PROPERTY.get()).ifPresent(builder::priority);
        ofNullable(DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY.get()).ifPresent(builder::proxy);
        ofNullable(DEFAULT_HTTP_REDIRECT_PROPERTY.get()).ifPresent(builder::followRedirects);
        ofNullable(DEFAULT_HTTP_SSL_CONTEXT_PROPERTY.get()).ifPresent(builder::sslContext);
        ofNullable(DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY.get()).ifPresent(builder::sslParameters);

        var client = builder.build();
        this.cookieStore = newStore;
        return client;
    }

    /**
     * Sends http request and receives a response
     *
     * @param requestBuilder is a builder of http request
     * @param <T>            is a type of response body
     * @return an instance of {@link HttpResponse}
     */
    public <T> HttpResponse<T> responseOf(RequestBuilder<T> requestBuilder) {
        return get(response(requestBuilder, t -> t));
    }

    /**
     * Extracts some object from http response body
     *
     * @param object is description how to get an object from a body of http response
     * @param <T>    is a type of response body
     * @param <R>    is a type of an object to get
     * @return an instance of {@code <R>}
     */
    public <T, R> R responseOf(GetObjectFromBodyStepSupplier<T, R> object) {
        return get(object);
    }

    /**
     * Extracts some object from http response body. Firstly it extracts an array of same type as
     * expected object and then it returns one of array items.
     *
     * @param oneOfArray is description how to get an array from a body of http response
     * @param <T>        is a type of response body
     * @param <R>        is a type of item of array which is extracted from a body of http response
     * @return an instance of {@code <R>}
     */
    public <T, R> R responseOf(GetObjectFromArrayBodyStepSupplier<T, R> oneOfArray) {
        return get(oneOfArray);
    }

    /**
     * Extracts some object from http response body. Firstly it extracts an iterable that consists of elements
     * of same type as expected object and then it returns one of elements.
     *
     * @param oneOfIterable is description how to get an {@link Iterable} from a body of http response
     * @param <T>           is a type of response body
     * @param <R>           is a type of element of {@link Iterable} which is extracted from a body of http response
     * @return an instance of {@code <R>}
     */
    public <T, R> R responseOf(GetObjectFromIterableBodyStepSupplier<T, R> oneOfIterable) {
        return get(oneOfIterable);
    }

    /**
     * Extracts some array from http response body. Firstly it extracts an array
     * and then it returns resulted array or sub array.
     *
     * @param array is description how to get an array from a body of http response
     * @param <T>   is a type of response body
     * @param <R>   is a type of item of array which is extracted from a body of http response
     * @return an array of {@code <R>}
     */
    public <T, R> R[] responseOf(GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return get(array);
    }

    /**
     * Extracts some {@link Iterable} from http response body. Firstly it extracts an iterable,
     * and then it returns resulted object or sub iterable as a list.
     *
     * @param iterable is description how to get an {@link Iterable} from a body of http response
     * @param <T>      is a type of response body
     * @param <R>      is a type of element of {@link Iterable} which is extracted from a body of http response
     * @param <S>      is a type of {@link Iterable}
     * @return an instance of {@code <S>}
     */
    public <T, R, S extends Iterable<R>> List<R> responseOf(GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return get(iterable);
    }

    /**
     * Gets not expired cookies which are stored by current http client.
     *
     * @param cookieCriteria criteria to get desired cookies
     * @return list of http cookies
     */
    @SafeVarargs
    public final List<HttpCookie> getCookies(Criteria<HttpCookie>... cookieCriteria) {
        var getCookies = httpCookies();
        stream(cookieCriteria).forEach(getCookies::criteria);
        return get(getCookies);
    }

    /**
     * Gets not expired cookies which are stored by current http client.
     *
     * @param uri            is an {@link URI} that associated with resulted cookies
     * @param cookieCriteria criteria to get desired cookies
     * @return list of http cookies
     */
    @SafeVarargs
    public final List<HttpCookie> getCookies(URI uri, Criteria<HttpCookie>... cookieCriteria) {
        var getCookies = httpCookies(uri);
        stream(cookieCriteria).forEach(getCookies::criteria);
        return get(getCookies);
    }

    /**
     * Gets not expired cookies which are stored by current http client.
     *
     * @param uriStr         is an a string value of an {@link URI} that associated with resulted cookies
     * @param cookieCriteria criteria to get desired cookies
     * @return list of http cookies
     */
    @SafeVarargs
    public final List<HttpCookie> getCookies(String uriStr, Criteria<HttpCookie>... cookieCriteria) {
        return getCookies(URI.create(uriStr), cookieCriteria);
    }

    /**
     * Gets not expired cookies which are stored by current http client.
     *
     * @param url            is an {@link URL} which is transformed to an {@link URI} that associated with resulted cookies
     * @param cookieCriteria criteria to get desired cookies
     * @return list of http cookies
     */
    @SafeVarargs
    public final List<HttpCookie> getCookies(URL url, Criteria<HttpCookie>... cookieCriteria) {
        try {
            return getCookies(url.toURI(), cookieCriteria);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param uri     the uri these cookies associated with.
     *                if {@code null}, then cookies are not associated
     *                with an URI
     * @param cookies cookies to be added
     * @return self-reference
     */
    public HttpStepContext addCookies(URI uri, List<HttpCookie> cookies) {
        return perform(addHttpCookies(uri, cookies));
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param uriStr  is an a string value of an {@link URI} these cookies associated with.
     * @param cookies cookies to be added
     * @return self-reference
     */
    public HttpStepContext addCookies(String uriStr, List<HttpCookie> cookies) {
        return addCookies(URI.create(uriStr), cookies);
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param url     is an {@link URL} which is transformed to an {@link URI} these cookies associated with.
     * @param cookies cookies to be added
     * @return self-reference
     */
    public HttpStepContext addCookies(URL url, List<HttpCookie> cookies) {
        try {
            return addCookies(url.toURI(), cookies);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param cookies cookies to be added
     * @return self-reference
     */
    public HttpStepContext addCookies(List<HttpCookie> cookies) {
        return addCookies((URI) null, cookies);
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param uri     the uri these cookies associated with.
     *                if {@code null}, then cookies are not associated
     *                with an URI
     * @param cookies cookies to be added
     * @return self-reference
     */
    public HttpStepContext addCookies(URI uri, HttpCookie... cookies) {
        return addCookies(uri, ofNullable(cookies).map(Arrays::asList).orElseGet(List::of));
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param uriStr  is an a string value of an {@link URI} these cookies associated with.
     * @param cookies cookies to be added
     * @return self-reference
     */
    public HttpStepContext addCookies(String uriStr, HttpCookie... cookies) {
        return addCookies(URI.create(uriStr), cookies);
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param url     is an {@link URL} which is transformed to an {@link URI} these cookies associated with.
     * @param cookies cookies to be added
     * @return self-reference
     */
    public HttpStepContext addCookies(URL url, HttpCookie... cookies) {
        try {
            return addCookies(url.toURI(), cookies);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param cookies cookies to be added
     * @return self-reference
     */
    public HttpStepContext addCookies(HttpCookie... cookies) {
        return addCookies((URI) null, cookies);
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param uri    the uri these cookies associated with.
     *               if {@code null}, then cookies are not associated
     *               with an URI
     * @param header is a full header string. It is supposed to be parsed and turned into a list of cookies.
     *               RFC 2965 section 3.2.2 set-cookie2 syntax indicates that one header line
     *               may contain more than one cookie definitions, so this is a static
     *               utility method instead of another constructor.
     * @return self-reference
     */
    public HttpStepContext addCookies(URI uri, String header) {
        return addCookies(uri, parse(header));
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param uriStr is an a string value of an {@link URI} cookies associated with.
     * @param header is a full header string. It is supposed to be parsed and turned into a list of cookies.
     *               RFC 2965 section 3.2.2 set-cookie2 syntax indicates that one header line
     *               may contain more than one cookie definitions, so this is a static
     *               utility method instead of another constructor.
     * @return self-reference
     */
    public HttpStepContext addCookies(String uriStr, String header) {
        return addCookies(URI.create(uriStr), parse(header));
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param url    is an {@link URL} which is transformed to an {@link URI} cookies associated with.
     * @param header is a full header string. It is supposed to be parsed and turned into a list of cookies.
     *               RFC 2965 section 3.2.2 set-cookie2 syntax indicates that one header line
     *               may contain more than one cookie definitions, so this is a static
     *               utility method instead of another constructor.
     * @return self-reference
     */
    public HttpStepContext addCookies(URL url, String header) {
        try {
            return addCookies(url.toURI(), parse(header));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds all the defined cookies to cookie jar.
     *
     * @param header is a full header string. It is supposed to be parsed and turned into a list of cookies.
     *               RFC 2965 section 3.2.2 set-cookie2 syntax indicates that one header line
     *               may contain more than one cookie definitions, so this is a static
     *               utility method instead of another constructor.
     * @return self-reference
     */
    public HttpStepContext addCookies(String header) {
        return addCookies((URI) null, parse(header));
    }

    /**
     * Cleans a cookie jar.
     *
     * @return self-reference
     */
    public HttpStepContext removeCookies() {
        return perform(deleteCookies());
    }

    /**
     * Cleans a cookie jar.
     *
     * @param uri         is an {@link URI} that associated with cookies to remove
     * @param toBeRemoved which cookies should be deleted
     * @return self-reference
     */
    @SafeVarargs
    public final HttpStepContext removeCookies(URI uri,
                                               Criteria<HttpCookie>... toBeRemoved) {
        return perform(deleteCookies(uri, toBeRemoved));
    }

    /**
     * Cleans a cookie jar.
     *
     * @param toBeRemoved which cookies should be deleted
     * @return self-reference
     */
    @SafeVarargs
    public final HttpStepContext removeCookies(Criteria<HttpCookie>... toBeRemoved) {
        return removeCookies(null, toBeRemoved);
    }

    /**
     * Cleans a cookie jar.
     *
     * @param cookies cookies that should be deleted
     * @return self-reference
     */
    public HttpStepContext removeCookies(Collection<HttpCookie> cookies) {
        return perform(deleteCookies(cookies));
    }

    /**
     * Cleans a cookie jar.
     *
     * @param cookies cookies that should be deleted
     * @return self-reference
     */
    public HttpStepContext removeCookies(HttpCookie... cookies) {
        return removeCookies(asList(cookies));
    }

    /**
     * Cleans a cookie jar.
     *
     * @param header is a full header string. It is supposed to be parsed and turned into a list of cookies.
     *               RFC 2965 section 3.2.2 set-cookie2 syntax indicates that one header line
     *               may contain more than one cookie definitions, so this is a static
     *               utility method instead of another constructor.
     * @return self-reference
     */
    public HttpStepContext removeCookies(String header) {
        return removeCookies(parse(header));
    }
}
