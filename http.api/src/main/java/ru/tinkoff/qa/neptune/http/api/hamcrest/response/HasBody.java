package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * This matcher is for the checking of body of a response.
 */
public final class HasBody<T> extends TypeSafeDiagnosingMatcher<HttpResponse<T>> {

    private final Matcher<? super T> bodyMatcher;

    private HasBody(Matcher<? super T> bodyMatcher) {
        checkNotNull(bodyMatcher, "Matcher of a body is not defined");
        this.bodyMatcher = bodyMatcher;
    }

    /**
     * Creates a matcher that checks body of a response.
     *
     * @param bodyMatcher criteria that describes expected body
     * @return a new instance of {@link HasBody}
     */
    public static <T> HasBody<T> hasBody(Matcher<? super T> bodyMatcher) {
        return new HasBody<T>(bodyMatcher);
    }

    /**
     * Creates a matcher that checks body of a response.
     *
     * @param body is the expected body of a response
     * @return a new instance of {@link HasBody}
     */
    public static <T> HasBody<T> hasBody(T body) {
        return hasBody(equalTo(body));
    }

    @Override
    protected boolean matchesSafely(HttpResponse<T> item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("null-response");
            return false;
        }

        var body = item.body();
        var result = bodyMatcher.matches(body);

        if (!result) {
            bodyMatcher.describeMismatch(body, mismatchDescription);
        }

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Response has body <")
                .appendDescriptionOf(bodyMatcher)
                .appendText(">");
    }
}
