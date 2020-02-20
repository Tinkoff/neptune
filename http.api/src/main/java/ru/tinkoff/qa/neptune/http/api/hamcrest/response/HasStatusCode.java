package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * This matcher is for the checking of status code of a response.
 */
public final class HasStatusCode extends TypeSafeDiagnosingMatcher<HttpResponse<?>> {

    private final Matcher<? super Integer> statusMatcher;

    private HasStatusCode(Matcher<? super Integer> statusMatcher) {
        checkNotNull(statusMatcher, "Matcher of a status is not defined");
        this.statusMatcher = statusMatcher;
    }

    /**
     * Creates a matcher that checks status code of a response.
     *
     * @param statusMatcher criteria that describes expected status code
     * @return a new instance of {@link HasStatusCode}
     */
    public static HasStatusCode hasStatusCode(Matcher<? super Integer> statusMatcher) {
        return new HasStatusCode(statusMatcher);
    }

    /**
     * Creates a matcher that checks status code of a response.
     *
     * @param code is the expected status of a response
     * @return a new instance of {@link HasStatusCode}
     */
    public static HasStatusCode hasStatusCode(int code) {
        return hasStatusCode(equalTo(code));
    }

    @Override
    protected boolean matchesSafely(HttpResponse<?> item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("null-response");
            return false;
        }

        var status = item.statusCode();
        var result = statusMatcher.matches(status);

        if (!result) {
            statusMatcher.describeMismatch(status, mismatchDescription);
        }

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Response has status code ")
                .appendDescriptionOf(statusMatcher);
    }
}
