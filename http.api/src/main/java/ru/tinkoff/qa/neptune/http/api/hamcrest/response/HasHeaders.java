package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.*;

/**
 * This matcher is for the checking of headers of a response.
 */
public final class HasHeaders extends TypeSafeDiagnosingMatcher<HttpResponse<?>> {

    private final Matcher<?> headerMatcher;

    private HasHeaders(Matcher<?> headerMatcher) {
        checkNotNull(headerMatcher, "Matcher of headers is not defined");
        this.headerMatcher = headerMatcher;
    }


    private static HasHeaders hasHeaders(Matcher<?> headerMatcher) {
        return new HasHeaders(headerMatcher);
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not.
     * Also it checks value of the header
     *
     * @param key   is the expected header name
     * @param value is expected value of the key
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeader(String key, List<String> value) {
        return hasHeaders(hasEntry(key, value));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not.
     * Also it checks value of the header
     *
     * @param keyMatcher is criteria that describes expected header name
     * @param value      is expected value of the key
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeader(Matcher<? super String> keyMatcher, List<String> value) {
        return hasHeaders(hasEntry(keyMatcher, equalTo(value)));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not.
     * Also it checks value of the header
     *
     * @param key          is the expected header name
     * @param valueMatcher is criteria that describes expected value
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeader(String key, Matcher<? super Collection<? super String>> valueMatcher) {
        return hasHeaders(hasEntry(equalTo(key), valueMatcher));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not.
     * Also it checks value of the header
     *
     * @param keyMatcher   is criteria that describes expected header name
     * @param valueMatcher is criteria that describes expected value
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeader(Matcher<? super String> keyMatcher, Matcher<? super Collection<? super String>> valueMatcher) {
        return hasHeaders(hasEntry(keyMatcher, valueMatcher));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not
     *
     * @param headerMatcher criteria that describes expected header name
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeaderName(Matcher<? super String> headerMatcher) {
        return hasHeaders(hasKey(headerMatcher));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not
     *
     * @param name expected header name
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeaderName(String name) {
        return hasHeaders(hasKey(equalTo(name)));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (value) is present or not
     *
     * @param valueMatcher criteria that describes expected header value
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeaderValue(Matcher<? super Collection<? super String>> valueMatcher) {
        return hasHeaders(hasValue(valueMatcher));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not
     *
     * @param value expected header value
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeaderValue(List<String> value) {
        return hasHeaders(hasValue(value));
    }


    @Override
    protected boolean matchesSafely(HttpResponse<?> item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("null-response");
            return false;
        }

        var headers = item.headers().map();
        var result = headerMatcher.matches(headers);

        if (!result) {
            headerMatcher.describeMismatch(headers, mismatchDescription);
        }

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Response has header(s) ")
                .appendDescriptionOf(headerMatcher);
    }
}
