package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("Query is {" + MATCHER_FRAGMENT + "}")
public final class HasQueryMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasQueryMatcher(Class<T> cls, Matcher<? super String> matcher, Function<T, String> conversion) {
        super(cls, matcher, conversion);
    }

    /**
     * Creates matcher that verifies the query of an URL
     *
     * @param queryMather that checks the query value
     * @return new {@link HasQueryMatcher}
     */
    public static HasQueryMatcher<URL> urlHasQuery(Matcher<? super String> queryMather) {
        return new HasQueryMatcher<>(URL.class, queryMather, URL::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URL
     *
     * @param query is the expected value of the query
     * @return new {@link HasQueryMatcher}
     */
    public static HasQueryMatcher<URL> urlHasQuery(String query) {
        return new HasQueryMatcher<>(URL.class, equalTo(query), URL::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URI
     *
     * @param queryMather that checks the query value
     * @return new {@link HasQueryMatcher}
     */
    public static HasQueryMatcher<URI> uriHasQuery(Matcher<? super String> queryMather) {
        return new HasQueryMatcher<>(URI.class, queryMather, URI::getQuery);
    }

    /**
     * Creates matcher that verifies the query of an URI
     *
     * @param query is the expected value of the query
     * @return new {@link HasQueryMatcher}
     */
    public static HasQueryMatcher<URI> uriHasQuery(String query) {
        return new HasQueryMatcher<>(URI.class, equalTo(query), URI::getQuery);
    }
}
