package ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("Query {" + MATCHER_FRAGMENT + "}")
public final class HasQueryStringMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasQueryStringMatcher(Class<T> cls, Matcher<? super String> matcher, Function<T, String> conversion) {
        super(cls, matcher, conversion);
    }

    /**
     * Creates matcher that verifies the query of an URL
     *
     * @param queryMather that checks the query value
     * @return new {@link HasQueryStringMatcher}
     */
    public static HasQueryStringMatcher<URL> urlHasQueryString(Matcher<? super String> queryMather) {
        return new HasQueryStringMatcher<>(URL.class, queryMather, URL::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URL
     *
     * @param query is the expected value of the query
     * @return new {@link HasQueryStringMatcher}
     */
    public static HasQueryStringMatcher<URL> urlHasQueryString(String query) {
        return new HasQueryStringMatcher<>(URL.class, equalTo(query), URL::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URI
     *
     * @param queryMather that checks the query value
     * @return new {@link HasQueryStringMatcher}
     */
    public static HasQueryStringMatcher<URI> uriHasQueryString(Matcher<? super String> queryMather) {
        return new HasQueryStringMatcher<>(URI.class, queryMather, URI::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URI
     *
     * @param query is the expected value of the query
     * @return new {@link HasQueryStringMatcher}
     */
    public static HasQueryStringMatcher<URI> uriHasQueryString(String query) {
        return new HasQueryStringMatcher<>(URI.class, equalTo(query), URI::getQuery);
    }
}
