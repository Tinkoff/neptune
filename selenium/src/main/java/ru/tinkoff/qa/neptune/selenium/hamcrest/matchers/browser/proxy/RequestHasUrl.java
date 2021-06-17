package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.net.MalformedURLException;
import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

@Description("Request URL {urlMatcher}")
public final class RequestHasUrl extends NeptuneFeatureMatcher<HarEntry> {

    @DescriptionFragment(value = "urlMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<?> urlMatcher;
    private final boolean toCheckURL;

    private RequestHasUrl(Matcher<?> urlMatcher, boolean toCheckURL) {
        super(true);
        this.toCheckURL = toCheckURL;
        checkNotNull(urlMatcher, "URL matcher is not defined");
        this.urlMatcher = urlMatcher;
    }

    /**
     * Creates matcher that checks URL of the request.
     *
     * @param urlMatcher criteria that describes expected URL
     * @return a new instance of {@link RequestHasUrl}
     */
    public static RequestHasUrl requestHasStringUrl(Matcher<? super String> urlMatcher) {
        return new RequestHasUrl(urlMatcher, false);
    }

    /**
     * Creates matcher that checks URL of the request.
     *
     * @param url is the expected URL of the request
     * @return a new instance of {@link RequestHasUrl}
     */
    public static RequestHasUrl requestHasStringUrl(String url) {
        return requestHasUrl(equalTo(url));
    }

    /**
     * Creates matcher that checks URL of the request.
     *
     * @param urlMatcher criteria that describes expected URL
     * @return a new instance of {@link RequestHasUrl}
     */
    public static RequestHasUrl requestHasUrl(Matcher<? super URL> urlMatcher) {
        return new RequestHasUrl(urlMatcher, true);
    }

    /**
     * Creates matcher that checks URL of the request.
     *
     * @param url is the expected URL of the request
     * @return a new instance of {@link RequestHasUrl}
     */
    public static RequestHasUrl requestHasUrl(URL url) {
        return requestHasUrl(equalTo(url));
    }

    @Override
    protected boolean featureMatches(HarEntry toMatch) {
        var requestUrl = toMatch.getRequest().getUrl();

        URL url;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        var result = (toCheckURL) ?
                urlMatcher.matches(url) :
                urlMatcher.matches(requestUrl);

        if (!result) {
            if (toCheckURL) {
                appendMismatchDescription(urlMatcher, url);
            } else {
                appendMismatchDescription(urlMatcher, requestUrl);
            }
        }

        return result;
    }
}
