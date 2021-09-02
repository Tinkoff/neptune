package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.net.URI;
import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * This matcher is for the checking of an URI of a response.
 */
@Description("URI {uri}")
public final class HasURI extends NeptuneFeatureMatcher<HttpResponse<?>> {

    @DescriptionFragment(value = "uri",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super URI> uriMatcher;

    private HasURI(Matcher<? super URI> uriMatcher) {
        super(true);
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
    protected boolean featureMatches(HttpResponse<?> toMatch) {
        var uri = toMatch.uri();
        var result = uriMatcher.matches(uri);

        if (!result) {
            appendMismatchDescription(uriMatcher, uri);
        }

        return result;
    }
}
