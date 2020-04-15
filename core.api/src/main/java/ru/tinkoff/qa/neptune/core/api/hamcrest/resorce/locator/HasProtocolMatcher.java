package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;

import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;

public final class HasProtocolMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasProtocolMatcher(Matcher<? super String> matcher, Function<T, String> conversion) {
        super("Protocol", matcher, conversion);
    }

    /**
     * Creates matcher that verifies the path of an URL
     *
     * @param pathMather that checks the path value
     * @return new {@link HasProtocolMatcher}
     */
    public static HasProtocolMatcher<URL> urlHasProtocol(Matcher<? super String> pathMather) {
        return new HasProtocolMatcher<>(pathMather, URL::getProtocol);
    }

    /**
     * Creates matcher that verifies the path of an URL
     *
     * @param path is the expected value of the path
     * @return new {@link HasProtocolMatcher}
     */
    public static HasProtocolMatcher<URL> urlHasProtocol(String path) {
        return new HasProtocolMatcher<>(equalTo(path), URL::getProtocol);
    }
}
