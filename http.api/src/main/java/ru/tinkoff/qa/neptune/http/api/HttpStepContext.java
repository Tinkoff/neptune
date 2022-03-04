package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.response.*;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.net.HttpCookie.parse;
import static java.net.http.HttpResponse.BodyHandlers.discarding;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;
import static ru.tinkoff.qa.neptune.http.api.cookies.AddHttpCookiesActionSupplier.addHttpCookies;
import static ru.tinkoff.qa.neptune.http.api.cookies.DeleteHttpCookiesActionSupplier.deleteCookies;
import static ru.tinkoff.qa.neptune.http.api.cookies.GetHttpCookiesSupplier.httpCookies;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

@CreateWith(provider = HttpStepsParameterProvider.class)
public class HttpStepContext extends Context<HttpStepContext> {

    private final HttpClient client;

    public HttpStepContext(HttpClient.Builder clientBuilder) {
        this.client = clientBuilder.build();
    }

    public static HttpStepContext http() {
        return getCreatedContextOrCreate(HttpStepContext.class);
    }

    public HttpClient getCurrentClient() {
        return client;
    }

    /**
     * Sends http request and receives a response with body
     *
     * @param requestBuilder is a builder of http request
     * @param bodyHandler    is a handler of a response body
     * @param <T>            is a type of response body
     * @return an instance of {@link HttpResponse}
     */
    public <T> HttpResponse<T> responseOf(RequestBuilder requestBuilder, HttpResponse.BodyHandler<T> bodyHandler) {
        return get(response(requestBuilder, bodyHandler));
    }

    /**
     * Sends http request and receives a response with no body
     *
     * @param requestBuilder is a builder of http request
     * @return an instance of {@link HttpResponse}
     */
    public HttpResponse<Void> responseOf(RequestBuilder requestBuilder) {
        return responseOf(requestBuilder, discarding());
    }

    /**
     * Extracts some object from http response body
     *
     * @param object is description how to get an object from a body of http response
     * @param <T>    is a type of response body
     * @param <R>    is a type of an object to get
     * @return an instance of {@code <R>}
     */
    public <T, R> R bodyData(GetObjectFromBodyStepSupplier<T, R, ?, ?> object) {
        return get(object);
    }


    /**
     * Extracts some object from http response body. Firstly it extracts an array of same type as
     * expected object and then it returns one of array items.
     *
     * @param oneOfArray is description how to get an array from a body of http response
     * @param <T>        is a type of response body
     * @param <R>        is a type of an item of array which is extracted from a body of http response
     * @return an instance of {@code <R>}
     */
    public <T, R> R bodyData(GetObjectFromArrayBodyStepSupplier<T, R, ?, ?> oneOfArray) {
        return get(oneOfArray);
    }

    /**
     * Extracts some object from http response body. Firstly it extracts an iterable that consists of elements
     * of same type as expected object and then it returns one of elements.
     *
     * @param oneOfIterable is description how to get an {@link Iterable} from a body of http response
     * @param <T>           is a type of response body
     * @param <R>           is a type of an element of {@link Iterable} which is extracted from a body of http response
     * @return an instance of {@code <R>}
     */
    public <T, R> R bodyData(GetObjectFromIterableBodyStepSupplier<T, R, ?, ?> oneOfIterable) {
        return get(oneOfIterable);
    }

    /**
     * Extracts some array from http response body. Firstly it extracts an array
     * and then it returns resulted array or sub array.
     *
     * @param array is description how to get an array from a body of http response
     * @param <T>   is a type of response body
     * @param <R>   is a type of an item of array which is extracted from a body of http response
     * @return an array of {@code <R>}
     */
    public <T, R> R[] bodyData(GetObjectsFromArrayBodyStepSupplier<T, R, ?, ?> array) {
        return get(array);
    }

    /**
     * Extracts some {@link Iterable} from http response body. Firstly it extracts an iterable
     * and then it returns resulted object or sub iterable.
     *
     * @param iterable is description how to get an {@link Iterable} from a body of http response
     * @param <T>      is a type of response body
     * @param <R>      is a type of an element of {@link Iterable} which is extracted from a body of http response
     * @param <S>      is a type of {@link Iterable}
     * @return an instance of {@code <S>}
     */
    public <T, R, S extends Iterable<R>> List<R> bodyData(GetObjectsFromIterableBodyStepSupplier<T, R, S, ?, ?> iterable) {
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
