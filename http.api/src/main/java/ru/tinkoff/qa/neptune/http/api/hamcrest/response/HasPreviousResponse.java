package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;

/**
 * This matcher is for the checking of a previous response.
 */
@Description("response from redirect: {previousResponse}")
public final class HasPreviousResponse<T> extends NeptuneFeatureMatcher<HttpResponse<T>> {

    @DescriptionFragment(value = "previousResponse",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    public final Matcher<? super HttpResponse<T>> responseMatcher;

    private HasPreviousResponse(Matcher<? super HttpResponse<T>> responseMatcher) {
        super(true);
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
        return hasPreviousResponse(notOf(nullValue()));
    }

    @Override
    protected boolean featureMatches(HttpResponse<T> toMatch) {
        var response = toMatch.previousResponse().orElse(null);
        var result = responseMatcher.matches(response);

        if (!result) {
            appendMismatchDescription(responseMatcher, toMatch);
        }

        return result;
    }
}
