package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;

public final class HasPathMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasPathMatcher(Matcher<? super String> matcher, Function<T, String> conversion) {
        super("Path", matcher, conversion);
    }

    /**
     * Creates matcher that verifier the path of an URL
     *
     * @param pathMather that checks the path value
     * @return new {@link HasPathMatcher}
     */
    public static HasPathMatcher<URL> urlHasPath(Matcher<? super String> pathMather) {
        return new HasPathMatcher<>(pathMather, URL::getPath);
    }

    /**
     * Creates matcher that verifier the path of an URL
     *
     * @param path is the expected value of the path
     * @return new {@link HasPathMatcher}
     */
    public static HasPathMatcher<URL> urlHasPath(String path) {
        return new HasPathMatcher<>(equalTo(path), URL::getPath);
    }

    /**
     * Creates matcher that verifier the path of an URI
     *
     * @param pathMather that checks the path value
     * @return new {@link HasPathMatcher}
     */
    public static HasPathMatcher<URI> uriHasPath(Matcher<? super String> pathMather) {
        return new HasPathMatcher<>(pathMather, URI::getPath);
    }

    /**
     * Creates matcher that verifier the path of an URI
     *
     * @param path is the expected value of the path
     * @return new {@link HasPathMatcher}
     */
    public static HasPathMatcher<URI> uriHasPath(String path) {
        return new HasPathMatcher<>(equalTo(path), URI::getPath);
    }
}
