package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.url;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import java.net.MalformedURLException;
import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window.IsWindowPresentMatcher.windowIsPresent;

@Description("URL of loaded page {urlMatcher}")
public final class AtThePageMatcher extends NeptuneFeatureMatcher<Window> {

    @DescriptionFragment(value = "urlMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<?> urlMatcher;
    private final boolean toCheckURL;

    private AtThePageMatcher(Matcher<?> urlMatcher, boolean toCheckURL) {
        super(true);
        this.toCheckURL = toCheckURL;
        checkArgument(nonNull(urlMatcher), "Criteria for the matching of an URL should be defined");
        this.urlMatcher = urlMatcher;
    }

    /**
     * Creates an instance of {@link AtThePageMatcher} that checks URL of a page that currently loaded.
     *
     * @param urlMatcher criteria for an URL under the matching.
     * @return instance of {@link AtThePageMatcher}
     */
    public static AtThePageMatcher pageURL(Matcher<String> urlMatcher) {
        return new AtThePageMatcher(urlMatcher, false);
    }

    /**
     * Creates an instance of {@link AtThePageMatcher} that checks URL of a page that currently loaded.
     *
     * @param urlMatcher criteria for an URL under the matching.
     * @return instance of {@link AtThePageMatcher}
     */
    public static AtThePageMatcher url(Matcher<URL> urlMatcher) {
        return new AtThePageMatcher(urlMatcher, true);
    }

    /**
     * Creates an instance of {@link AtThePageMatcher} that checks URL of a page that currently loaded.
     *
     * @param url expected URL of a page.
     * @return instance of {@link AtThePageMatcher}
     */
    public static AtThePageMatcher pageURL(String url) {
        return pageURL(equalTo(url));
    }

    @Override
    protected boolean featureMatches(Window toMatch) {
        var windowPresent = windowIsPresent();
        if (!windowPresent.matches(toMatch)) {
            appendMismatchDescription(windowPresent, toMatch);
            return false;
        }

        var currentUrlString = toMatch.getCurrentUrl();
        URL url;
        try {
            url = new URL(currentUrlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        var result = (toCheckURL) ?
                urlMatcher.matches(url) :
                urlMatcher.matches(currentUrlString);

        if (!result) {
            if (toCheckURL) {
                appendMismatchDescription(urlMatcher, url);
            } else {
                appendMismatchDescription(urlMatcher, currentUrlString);
            }
        }

        return result;
    }
}
