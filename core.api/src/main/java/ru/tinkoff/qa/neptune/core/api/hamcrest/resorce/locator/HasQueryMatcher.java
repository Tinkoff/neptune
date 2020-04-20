package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;

public final class HasQueryMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasQueryMatcher(Matcher<? super String> matcher, Function<T, String> conversion) {
        super("Query", matcher, conversion);
    }

    /**
     * Creates matcher that verifies the query of an URL
     *
     * @param queryMather that checks the query value
     * @return new {@link HasQueryMatcher}
     */
    public static HasQueryMatcher<URL> urlHasQuery(Matcher<? super String> queryMather) {
        return new HasQueryMatcher<>(queryMather, URL::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URL
     *
     * @param query is the expected value of the query
     * @return new {@link HasQueryMatcher}
     */
    public static HasQueryMatcher<URL> urlHasQuery(String query) {
        return new HasQueryMatcher<>(equalTo(query), URL::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URI
     *
     * @param queryMather that checks the query value
     * @return new {@link HasQueryMatcher}
     */
    public static HasQueryMatcher<URI> uriHasQuery(Matcher<? super String> queryMather) {
        return new HasQueryMatcher<>(queryMather, URI::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URI
     *
     * @param query is the expected value of the query
     * @return new {@link HasQueryMatcher}
     */
    public static HasQueryMatcher<URI> uriHasQuery(String query) {
        return new HasQueryMatcher<>(equalTo(query), URI::getQuery);
    }
}
