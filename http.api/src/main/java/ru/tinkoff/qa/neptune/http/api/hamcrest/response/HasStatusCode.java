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
 * This matcher is for the checking of status code of a response.
 */
@Description("status code {status}")
public final class HasStatusCode extends NeptuneFeatureMatcher<HttpResponse<?>> {

    @DescriptionFragment(value = "status",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super Integer> statusMatcher;

    private HasStatusCode(Matcher<? super Integer> statusMatcher) {
        super(true);
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
    protected boolean featureMatches(HttpResponse<?> toMatch) {
        var status = toMatch.statusCode();
        var result = statusMatcher.matches(status);

        if (!result) {
            appendMismatchDescription(statusMatcher, status);
        }

        return result;
    }
}
