package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * This matcher is for the checking of body of a response.
 */
@Description("response body {bodyMatcher}")
public final class HasBody<T> extends NeptuneFeatureMatcher<HttpResponse<T>> {

    @DescriptionFragment(value = "bodyMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super T> bodyMatcher;

    private HasBody(Matcher<? super T> bodyMatcher) {
        super(true);
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
        return new HasBody<>(bodyMatcher);
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
    protected boolean featureMatches(HttpResponse<T> toMatch) {
        var body = toMatch.body();
        var result = bodyMatcher.matches(body);

        if (!result) {
            appendMismatchDescription(bodyMatcher, body);
        }

        return result;
    }
}
