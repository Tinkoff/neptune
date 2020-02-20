package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;

public final class HasSchemeMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasSchemeMatcher(Matcher<? super String> matcher, Function<T, String> conversion) {
        super("Scheme", matcher, conversion);
    }

    /**
     * Creates matcher that verifier the scheme of an URI
     *
     * @param schemeMather that checks the scheme value
     * @return new {@link HasSchemeMatcher}
     */
    public static HasSchemeMatcher<URI> uriHasScheme(Matcher<? super String> schemeMather) {
        return new HasSchemeMatcher<>(schemeMather, URI::getScheme);
    }

    /**
     * Creates matcher that verifier the scheme of an URI
     *
     * @param scheme is the expected value of the scheme
     * @return new {@link HasSchemeMatcher}
     */
    public static HasSchemeMatcher<URI> uriHasScheme(String scheme) {
        return new HasSchemeMatcher<>(equalTo(scheme), URI::getScheme);
    }
}
