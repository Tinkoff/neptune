package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * This matcher is for the checking of a previous response.
 */
public final class HasPreviousResponse<T> extends TypeSafeDiagnosingMatcher<HttpResponse<T>> {

    public final Matcher<? super HttpResponse<T>> responseMatcher;

    private HasPreviousResponse(Matcher<? super HttpResponse<T>> responseMatcher) {
        checkNotNull(responseMatcher, "Matcher of a previous response is not defined");
        this.responseMatcher = responseMatcher;
    }

    /**
     * Creates a matcher that checks the previous response.
     *
     * @param responseMatcher criteria that describes previous response
     * @return a new instance of {@link HasPreviousResponse}
     */
    public static <T> HasPreviousResponse<T> hasPreviousResponse(Matcher<? super HttpResponse<T>> responseMatcher)  {
        return new HasPreviousResponse<>(responseMatcher);
    }

    /**
     * Creates a matcher that checks whenever previous response is present or not.
     *
     * @return a new instance of {@link HasPreviousResponse}
     */
    public static <T> HasPreviousResponse<T> hasPreviousResponse()  {
        return hasPreviousResponse(not(nullValue()));
    }

    @Override
    protected boolean matchesSafely(HttpResponse<T> item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("null-response");
            return false;
        }

        var response = item.previousResponse().orElse(null);
        var result = responseMatcher.matches(response);

        if (!result) {
            responseMatcher.describeMismatch(response, mismatchDescription);
        }

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Response previous <")
                .appendDescriptionOf(responseMatcher)
                .appendText(">");
    }
}
