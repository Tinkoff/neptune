package ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("Port {" + MATCHER_FRAGMENT + "}")
public final class HasPortMatcher<T> extends ResourceLocatorMatcher<T, Integer> {

    private HasPortMatcher(Class<T> cls, Matcher<? super Integer> matcher, Function<T, Integer> conversion) {
        super(cls, matcher, conversion);
    }

    /**
     * Creates matcher that verifies the port of an URL
     *
     * @param portMather that checks the port value
     * @return new {@link HasPortMatcher}
     */
    public static HasPortMatcher<URL> urlHasPort(Matcher<? super Integer> portMather) {
        return new HasPortMatcher<>(URL.class, portMather, URL::getPort);
    }

    /**
     * Creates matcher that verifies the port of an URL
     *
     * @param port is the expected value of the port
     * @return new {@link HasPortMatcher}
     */
    public static HasPortMatcher<URL> urlHasPort(int port) {
        return new HasPortMatcher<>(URL.class, equalTo(port), URL::getPort);
    }

    /**
     * Creates matcher that verifies the port of an URI
     *
     * @param portMather that checks the port value
     * @return new {@link HasPortMatcher}
     */
    public static HasPortMatcher<URI> uriHasPort(Matcher<? super Integer> portMather) {
        return new HasPortMatcher<>(URI.class, portMather, URI::getPort);
    }

    /**
     * Creates matcher that verifies the port of an URI
     *
     * @param port is the expected value of the port
     * @return new {@link HasPortMatcher}
     */
    public static HasPortMatcher<URI> uriHasPort(int port) {
        return new HasPortMatcher<>(URI.class, equalTo(port), URI::getPort);
    }
}
