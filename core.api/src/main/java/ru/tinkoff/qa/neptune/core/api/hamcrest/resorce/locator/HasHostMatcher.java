package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("Host is {" + MATCHER_FRAGMENT + "}")
public final class HasHostMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasHostMatcher(Class<T> cls, Matcher<? super String> matcher, Function<T, String> conversion) {
        super(cls, matcher, conversion);
    }

    /**
     * Creates matcher that verifies the host of an URL
     *
     * @param hostMather that checks the host value
     * @return new {@link HasHostMatcher}
     */
    public static HasHostMatcher<URL> urlHasHost(Matcher<? super String> hostMather) {
        return new HasHostMatcher<>(URL.class, hostMather, URL::getHost);
    }

    /**
     * Creates matcher that verifies the host of an URL
     *
     * @param host is the expected value of the host
     * @return new {@link HasHostMatcher}
     */
    public static HasHostMatcher<URL> urlHasHost(String host) {
        return new HasHostMatcher<>(URL.class, equalTo(host), URL::getHost);
    }

    /**
     * Creates matcher that verifies the host of an URI
     *
     * @param hostMather that checks the host value
     * @return new {@link HasHostMatcher}
     */
    public static HasHostMatcher<URI> uriHasHost(Matcher<? super String> hostMather) {
        return new HasHostMatcher<>(URI.class, hostMather, URI::getHost);
    }

    /**
     * Creates matcher that verifies the host of an URI
     *
     * @param host is the expected value of the host
     * @return new {@link HasHostMatcher}
     */
    public static HasHostMatcher<URI> uriHasHost(String host) {
        return new HasHostMatcher<>(URI.class, equalTo(host), URI::getHost);
    }
}
