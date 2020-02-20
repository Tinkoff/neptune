package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * This matcher is for the checking of a response version.
 */
public final class HasVersion extends TypeSafeDiagnosingMatcher<HttpResponse<?>> {
    private final Matcher<? super HttpClient.Version> versionMatcher;

    private HasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        checkNotNull(versionMatcher, "Matcher of a version is not defined");
        this.versionMatcher = versionMatcher;
    }

    /**
     * Creates a matcher that checks a response version.
     *
     * @param versionMatcher criteria that describes expected response version
     * @return a new instance of {@link HasVersion}
     */
    public static HasVersion hasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        return new HasVersion(versionMatcher);
    }

    /**
     * Creates a matcher that checks a response version.
     *
     * @param version is the expected version
     * @return a new instance of {@link HasVersion}
     */
    public static HasVersion hasVersion(HttpClient.Version version) {
        return hasVersion(equalTo(version));
    }

    @Override
    protected boolean matchesSafely(HttpResponse<?> item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("null-response");
            return false;
        }

        var version = item.version();
        var result = versionMatcher.matches(version);

        if (!result) {
            versionMatcher.describeMismatch(version, mismatchDescription);
        }

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Response has version ")
                .appendDescriptionOf(versionMatcher);
    }

}
