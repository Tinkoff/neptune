package ru.tinkoff.qa.neptune.http.api.response;

import com.google.common.base.Preconditions;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.net.URI.create;
import static java.util.Objects.nonNull;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.glassfish.jersey.internal.guava.Preconditions.checkArgument;
import static org.glassfish.jersey.internal.guava.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class ResponseCriteria {

    private ResponseCriteria() {
        super();
    }

    /**
     * Builds criteria to match status code of a response.
     *
     * @param code is the expected status code.
     * @param <T>  is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> statusCode(int code) {
        return condition(format("status code is %s", code), r -> r.statusCode() == code);
    }

    /**
     * Builds criteria to match status code of a response fluently.
     *
     * @param description description of the expectation
     * @param predicate   is the expectation that response body should meet
     * @param <T>         is a type of response body
     * @return criteria
     */
    public static <T> Criteria<HttpResponse<T>> bodyMatches(String description, Predicate<? super T> predicate) {
        checkArgument(nonNull(predicate), "Predicate that checks response body should be defined");
        return condition(description, r -> predicate.test(r.body()));
    }

    /**
     * Builds criteria to match uri of a response.
     *
     * @param stringURI expected uri of a response defined as string.
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURI(String stringURI) {
        return responseURI(create(stringURI));
    }

    /**
     * Builds criteria to match uri of a response.
     *
     * @param url expected uri of a response defined as {@link URL}.
     * @param <T> is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURI(URL url) {
        checkNotNull(url, "Expected URL should not be defined as a null value");
        try {
            return responseURI(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Builds criteria to match uri of a response.
     *
     * @param uri expected uri of a response.
     * @param <T> is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURI(URI uri) {
        checkNotNull(uri, "Expected URI should not be defined as a null value");
        return condition(format("response URI is %s", uri), r -> uri.equals(r.uri()));
    }

    /**
     * Builds criteria to match uri of a response.
     *
     * @param expression is a substring that uri of a response is supposed to have.
     *                   It is possible to pass reg exp pattern that uri should fit.
     * @param <T>        is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> uriMatches(String expression) {
        Preconditions.checkArgument(isNotBlank(expression), "Http response URI expression should be defined");

        return condition(format("response URI contains '%s' or meets regExp pattern '%s'", expression, expression), r -> {
            var uri = valueOf(r.uri());
            if (uri.contains(expression)) {
                return true;
            }

            try {
                var pattern = compile(expression);
                var m = pattern.matcher(uri);
                return m.matches() || m.find();
            } catch (Throwable t) {
                t.printStackTrace();
                return false;
            }
        });
    }

    private static <T> Criteria<HttpResponse<T>> uriPartStringCriteria(String description, String expected, Function<URI, String> getPart) {
        checkArgument(isNotBlank(expected), format("Expected URI %s should not be defined as a blank/null string", description));
        return condition(format("Response URI %s is '%s'", description, expected), r -> {
            try {
                return Objects.equals(getPart.apply(r.uri()), expected);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    private static <T> Criteria<HttpResponse<T>> uriPartRegExpCriteria(String description, String expression, Function<URI, String> getPart) {
        checkArgument(isNotBlank(expression), format("expression of URI %s should not be defined as a blank/null string", description));
        return condition(format("Response URI %s contains '%s' or meets regExp pattern '%s'", description, expression, expression), r -> {
            try {
                var part = getPart.apply(r.uri());

                if (part.contains(expression)) {
                    return true;
                }

                try {
                    var pattern = compile(expression);
                    var m = pattern.matcher(part);
                    return m.matches() || m.find();
                } catch (Throwable t) {
                    t.printStackTrace();
                    return false;
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }


    /**
     * Builds criteria to match http response uri by host value.
     *
     * @param host is a host value
     * @param <T>  is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIHost(String host) {
        return uriPartStringCriteria("host", host, URI::getHost);
    }

    /**
     * Builds criteria to match http response uri by host value.
     *
     * @param expression is a substring that host is supposed to have.
     *                   It is possible to pass reg exp pattern that host should fit.
     * @param <T>        is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIHostMatches(String expression) {
        return uriPartRegExpCriteria("host", expression, URI::getHost);
    }

    /**
     * Builds criteria to match http response uri by scheme value.
     *
     * @param scheme is a scheme value
     * @param <T>    is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIScheme(String scheme) {
        return uriPartStringCriteria("scheme", scheme, URI::getScheme);
    }

    /**
     * Builds criteria to match http response uri by scheme value.
     *
     * @param expression is a substring that scheme is supposed to have.
     *                   It is possible to pass reg exp pattern that scheme should fit.
     * @param <T>        is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURISchemeMatches(String expression) {
        return uriPartRegExpCriteria("scheme", expression, URI::getScheme);
    }

    /**
     * Builds criteria to match http response uri by path value.
     *
     * @param path is a path value
     * @param <T>  is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIPath(String path) {
        return uriPartStringCriteria("path", path, URI::getPath);
    }

    /**
     * Builds criteria to match http response uri by path value.
     *
     * @param expression is a substring that path is supposed to have.
     *                   It is possible to pass reg exp pattern that path should fit.
     * @param <T>        is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIPathMatches(String expression) {
        return uriPartRegExpCriteria("path", expression, URI::getPath);
    }

    /**
     * Builds criteria to match http response uri by query value.
     *
     * @param query is a query value
     * @param <T>   is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIQuery(String query) {
        return uriPartStringCriteria("query", query, URI::getQuery);
    }

    /**
     * Builds criteria to match http response uri by query value.
     *
     * @param expression is a substring that query is supposed to have.
     *                   It is possible to pass reg exp pattern that query should fit.
     * @param <T>        is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIQueryMatches(String expression) {
        return uriPartRegExpCriteria("query", expression, URI::getQuery);
    }

    /**
     * Builds criteria to match http response uri by user info value.
     *
     * @param userInfo is an user info value
     * @param <T>      is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIUserInfo(String userInfo) {
        return uriPartStringCriteria("user info", userInfo, URI::getUserInfo);
    }

    /**
     * Builds criteria to match http response uri by user info value.
     *
     * @param expression is a substring that user info is supposed to have.
     *                   It is possible to pass reg exp pattern that user info should fit.
     * @param <T>        is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIUserInfoMatches(String expression) {
        return uriPartRegExpCriteria("user info", expression, URI::getUserInfo);
    }

    /**
     * Builds criteria to match http response uri by port value.
     *
     * @param port is a port value
     * @param <T>  is a type of response body
     * @return criteria.
     */
    public static <T> Criteria<HttpResponse<T>> responseURIPort(int port) {
        Preconditions.checkArgument(port > 0, "Port value should be greater than 0");
        return condition(format("Response URI port is '%s'", port), r -> {
            try {
                return r.uri().getPort() == port;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
}
