package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.URI;
import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * This matcher is for the checking of an URI of a response.
 */
public class HasURI extends TypeSafeDiagnosingMatcher<HttpResponse<?>> {

    private final Matcher<? super URI> uriMatcher;

    private HasURI(Matcher<? super URI> uriMatcher) {
        checkNotNull(uriMatcher, "Matcher of an URI is not defined");
        this.uriMatcher = uriMatcher;
    }

    /**
     * Creates a matcher that checks an URI of a response.
     *
     * @param uriMatcher criteria that describes expected URI
     * @return a new instance of {@link HasURI}
     */
    public static HasURI hasURI(Matcher<? super URI> uriMatcher) {
        return new HasURI(uriMatcher);
    }

    /**
     * Creates a matcher that checks an URI of a response.
     *
     * @param uri is the expected uri of a response
     * @return a new instance of {@link HasURI}
     */
    public static HasURI hasURI(URI uri) {
        return new HasURI(equalTo(uri));
    }

    @Override
    protected boolean matchesSafely(HttpResponse<?> item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("null-response");
            return false;
        }

        var uri = item.uri();
        var result = uriMatcher.matches(uri);

        if (!result) {
            uriMatcher.describeMismatch(uri, mismatchDescription);
        }

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Response has URI ")
                .appendDescriptionOf(uriMatcher);
    }
}
