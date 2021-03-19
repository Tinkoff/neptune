package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

public final class ResponseHasStatusCode extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super Integer> statusCodeMatcher;

    private ResponseHasStatusCode(Matcher<? super Integer> statusCodeMatcher) {
        checkNotNull(statusCodeMatcher, "Status code matcher is not defined");
        this.statusCodeMatcher = statusCodeMatcher;
    }

    /**
     * Creates matcher that checks status code of the response.
     *
     * @param statusCodeMatcher criteria that describes expected status code
     * @return a new instance of {@link ResponseHasStatusCode}
     */
    public static ResponseHasStatusCode responseHasStatusCode(Matcher<? super Integer> statusCodeMatcher) {
        return new ResponseHasStatusCode(statusCodeMatcher);
    }

    /**
     * Creates matcher that checks status code of the response.
     *
     * @param statusCode is the expected status of the response
     * @return a new instance of {@link ResponseHasStatusCode}
     */
    public static ResponseHasStatusCode responseHasStatusCode(int statusCode) {
        return new ResponseHasStatusCode(is(statusCode));
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var responseStatus = item.getResponse().getStatus();
        var result = statusCodeMatcher.matches(responseStatus);

        if (!result) {
            statusCodeMatcher.describeMismatch(responseStatus, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("response has status %s", statusCodeMatcher);
    }
}
