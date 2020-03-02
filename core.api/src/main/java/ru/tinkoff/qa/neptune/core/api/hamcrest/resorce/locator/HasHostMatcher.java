package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;

public final class HasHostMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasHostMatcher(Matcher<? super String> matcher, Function<T, String> conversion) {
        super("Host", matcher, conversion);
    }

    /**
     * Creates matcher that verifier the host of an URL
     *
     * @param hostMather that checks the host value
     * @return new {@link HasHostMatcher}
     */
    public static HasHostMatcher<URL> urlHasHost(Matcher<? super String> hostMather) {
        return new HasHostMatcher<>(hostMather, URL::getHost);
    }

    /**
     * Creates matcher that verifier the host of an URL
     *
     * @param host is the expected value of the host
     * @return new {@link HasHostMatcher}
     */
    public static HasHostMatcher<URL> urlHasHost(String host) {
        return new HasHostMatcher<>(equalTo(host), URL::getHost);
    }

    /**
     * Creates matcher that verifier the host of an URI
     *
     * @param hostMather that checks the host value
     * @return new {@link HasHostMatcher}
     */
    public static HasHostMatcher<URI> uriHasHost(Matcher<? super String> hostMather) {
        return new HasHostMatcher<>(hostMather, URI::getHost);
    }

    /**
     * Creates matcher that verifier the host of an URI
     *
     * @param host is the expected value of the host
     * @return new {@link HasHostMatcher}
     */
    public static HasHostMatcher<URI> uriHasHost(String host) {
        return new HasHostMatcher<>(equalTo(host), URI::getHost);
    }
}
