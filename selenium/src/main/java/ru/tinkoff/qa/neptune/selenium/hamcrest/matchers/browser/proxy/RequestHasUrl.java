package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

public final class RequestHasUrl extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super String> urlMatcher;

    private RequestHasUrl(Matcher<? super String> urlMatcher) {
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
    public String toString() {
        return format("request has URL %s", urlMatcher);
    }
}
