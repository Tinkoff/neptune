package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;

public class HasPortMatcher<T> extends ResourceLocatorMatcher<T, Integer> {

    private HasPortMatcher(Matcher<? super Integer> matcher, Function<T, Integer> conversion) {
        super("Port", matcher, conversion);
    }

    /**
     * Creates matcher that verifier the port of an URL
     *
     * @param portMather that checks the port value
     * @return new {@link HasPortMatcher}
     */
    public static HasPortMatcher<URL> urlHasPort(Matcher<? super Integer> portMather) {
        return new HasPortMatcher<>(portMather, URL::getPort);
    }

    /**
     * Creates matcher that verifier the port of an URL
     *
     * @param port is the expected value of the port
     * @return new {@link HasPortMatcher}
     */
    public static HasPortMatcher<URL> urlHasPort(int port) {
        return new HasPortMatcher<>(equalTo(port), URL::getPort);
    }

    /**
     * Creates matcher that verifier the port of an URI
     *
     * @param portMather that checks the port value
     * @return new {@link HasPortMatcher}
     */
    public static HasPortMatcher<URI> uriHasPort(Matcher<? super Integer> portMather) {
        return new HasPortMatcher<>(portMather, URI::getPort);
    }

    /**
     * Creates matcher that verifier the port of an URI
     *
     * @param port is the expected value of the port
     * @return new {@link HasPortMatcher}
     */
    public static HasPortMatcher<URI> uriHasPort(int port) {
        return new HasPortMatcher<>(equalTo(port), URI::getPort);
    }
}
