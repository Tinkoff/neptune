package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

@Description("Response status code {statusCodeMatcher}")
public final class ResponseHasStatusCode extends NeptuneFeatureMatcher<HarEntry> {

    @DescriptionFragment(value = "statusCodeMatcher")
    private final Matcher<? super Integer> statusCodeMatcher;

    private ResponseHasStatusCode(Matcher<? super Integer> statusCodeMatcher) {
        super(true);
        checkNotNull(statusCodeMatcher, "Status code matcher is not defined");
        this.statusCodeMatcher = statusCodeMatcher;
    }

    /**
     * Creates matcher that checks status code of the response.
     *
     * @param statusCodeMatcher criteria that describes expected status code
     * @return a new instance of {@link ResponseHasStatusCode}
     */
    public static Matcher<HarEntry> responseHasStatusCode(Matcher<? super Integer> statusCodeMatcher) {
        return new ResponseHasStatusCode(statusCodeMatcher);
    }

    /**
     * Creates matcher that checks status code of the response.
     *
     * @param statusCode is the expected status of the response
     * @return a new instance of {@link ResponseHasStatusCode}
     */
    public static Matcher<HarEntry> responseHasStatusCode(int statusCode) {
        return responseHasStatusCode(equalTo(statusCode));
    }

    @Override
    protected boolean featureMatches(HarEntry toMatch) {
        var responseStatus = toMatch.getResponse().getStatus();
        var result = statusCodeMatcher.matches(responseStatus);

        if (!result) {
            appendMismatchDescription(statusCodeMatcher, responseStatus);
        }

        return result;
    }
}
