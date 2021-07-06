package ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("User info {" + MATCHER_FRAGMENT + "}")
public final class HasUserInfoMatcher<T> extends ResourceLocatorMatcher<T, String> {

    private HasUserInfoMatcher(Class<T> cls, Matcher<? super String> matcher, Function<T, String> conversion) {
        super(cls, matcher, conversion);
    }

    /**
     * Creates matcher that verifies the user info of an URL
     *
     * @param userInfoMather that checks the user info value
     * @return new {@link HasUserInfoMatcher}
     */
    public static HasUserInfoMatcher<URL> urlHasUserInfo(Matcher<? super String> userInfoMather) {
        return new HasUserInfoMatcher<>(URL.class, userInfoMather, URL::getUserInfo);
    }

    /**
     * Creates matcher that verifies the user info of an URL
     *
     * @param userInfo is the expected value of the user info
     * @return new {@link HasUserInfoMatcher}
     */
    public static HasUserInfoMatcher<URL> urlHasUserInfo(String userInfo) {
        return new HasUserInfoMatcher<>(URL.class, equalTo(userInfo), URL::getUserInfo);
    }

    /**
     * Creates matcher that verifies the user info of an URI
     *
     * @param userInfoMather that checks the user info value
     * @return new {@link HasUserInfoMatcher}
     */
    public static HasUserInfoMatcher<URI> uriHasUserInfo(Matcher<? super String> userInfoMather) {
        return new HasUserInfoMatcher<>(URI.class, userInfoMather, URI::getUserInfo);
    }

    /**
     * Creates matcher that verifies the user info of an URI
     *
     * @param userInfo is the expected value of the user info
     * @return new {@link HasUserInfoMatcher}
     */
    public static HasUserInfoMatcher<URI> uriHasUserInfo(String userInfo) {
        return new HasUserInfoMatcher<>(URI.class, equalTo(userInfo), URI::getUserInfo);
    }
}
