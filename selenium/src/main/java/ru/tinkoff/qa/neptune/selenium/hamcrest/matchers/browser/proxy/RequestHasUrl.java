package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;

@Description("Request URL {urlMatcher}")
public final class RequestHasUrl extends NeptuneFeatureMatcher<HarEntry> {

    private final Matcher<?> urlMatcher;

    private RequestHasUrl(Matcher<? super String> urlMatcher) {
        super(true);
        checkNotNull(urlMatcher, "URL matcher is not defined");
        this.urlMatcher = urlMatcher;
    }

    /**
     * Creates matcher that checks URL of the request.
     *
     * @param urlMatcher criteria that describes expected URL
     * @return a new instance of {@link RequestHasUrl}
     */
    public static RequestHasUrl requestHasUrl(Matcher<? super String> urlMatcher) {
        return new RequestHasUrl(urlMatcher);
    }

    /**
     * Creates matcher that checks URL of the request.
     *
     * @param url is the expected URL of the request
     * @return a new instance of {@link RequestHasUrl}
     */
    public static RequestHasUrl requestHasUrl(String url) {
        return new RequestHasUrl(is(url));
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var requestUrl = item.getRequest().getUrl();
        var result = urlMatcher.matches(requestUrl);

        if (!result) {
            urlMatcher.describeMismatch(requestUrl, mismatchDescription);
        }

        return result;
    }

    @Override
    protected boolean featureMatches(HarEntry toMatch) {
        return false;
    }
}
