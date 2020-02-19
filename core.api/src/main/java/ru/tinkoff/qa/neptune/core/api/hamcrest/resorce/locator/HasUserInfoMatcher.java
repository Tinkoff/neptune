package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;

public class HasUserInfoMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasUserInfoMatcher(Matcher<? super String> matcher, Function<T, String> conversion) {
        super("User info", matcher, conversion);
    }

    /**
     * Creates matcher that verifier the user info of an URL
     *
     * @param userInfoMather that checks the user info value
     * @return new {@link HasUserInfoMatcher}
     */
    public static HasUserInfoMatcher<URL> urlHasUserInfo(Matcher<? super String> userInfoMather) {
        return new HasUserInfoMatcher<>(userInfoMather, URL::getUserInfo);
    }

    /**
     * Creates matcher that verifier the user info of an URL
     *
     * @param userInfo is the expected value of the user info
     * @return new {@link HasUserInfoMatcher}
     */
    public static HasUserInfoMatcher<URL> urlHasUserInfo(String userInfo) {
        return new HasUserInfoMatcher<>(equalTo(userInfo), URL::getUserInfo);
    }

    /**
     * Creates matcher that verifier the user info of an URI
     *
     * @param userInfoMather that checks the user info value
     * @return new {@link HasUserInfoMatcher}
     */
    public static HasUserInfoMatcher<URI> uriHasUserInfo(Matcher<? super String> userInfoMather) {
        return new HasUserInfoMatcher<>(userInfoMather, URI::getUserInfo);
    }

    /**
     * Creates matcher that verifier the user info of an URI
     *
     * @param userInfo is the expected value of the user info
     * @return new {@link HasUserInfoMatcher}
     */
    public static HasUserInfoMatcher<URI> uriHasUserInfo(String userInfo) {
        return new HasUserInfoMatcher<>(equalTo(userInfo), URI::getUserInfo);
    }
}
