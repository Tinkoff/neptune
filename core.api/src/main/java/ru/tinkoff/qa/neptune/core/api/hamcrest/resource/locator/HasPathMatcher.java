package ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("Path {" + MATCHER_FRAGMENT + "}")
public final class HasPathMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasPathMatcher(Class<T> cls, Matcher<? super String> matcher, Function<T, String> conversion) {
        super(cls, matcher, conversion);
    }

    /**
     * Creates matcher that verifies the path of an URL
     *
     * @param pathMather that checks the path value
     * @return new {@link HasPathMatcher}
     */
    public static HasPathMatcher<URL> urlHasPath(Matcher<? super String> pathMather) {
        return new HasPathMatcher<>(URL.class, pathMather, URL::getPath);
    }

    /**
     * Creates matcher that verifies the path of an URL
     *
     * @param path is the expected value of the path
     * @return new {@link HasPathMatcher}
     */
    public static HasPathMatcher<URL> urlHasPath(String path) {
        return new HasPathMatcher<>(URL.class, equalTo(path), URL::getPath);
    }

    /**
     * Creates matcher that verifies the path of an URI
     *
     * @param pathMather that checks the path value
     * @return new {@link HasPathMatcher}
     */
    public static HasPathMatcher<URI> uriHasPath(Matcher<? super String> pathMather) {
        return new HasPathMatcher<>(URI.class, pathMather, URI::getPath);
    }

    /**
     * Creates matcher that verifies the path of an URI
     *
     * @param path is the expected value of the path
     * @return new {@link HasPathMatcher}
     */
    public static HasPathMatcher<URI> uriHasPath(String path) {
        return new HasPathMatcher<>(URI.class, equalTo(path), URI::getPath);
    }
}
