package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.url;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.ResourceLocatorMatcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.net.MalformedURLException;
import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;

public final class AtThePageMatcher extends TypeSafeDiagnosingMatcher<WrapsDriver> {

    private final Matcher<?> urlMatcher;

    private AtThePageMatcher(Matcher<?> urlMatcher) {
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
        return new AtThePageMatcher(urlMatcher);
    }

    /**
     * Creates an instance of {@link AtThePageMatcher} that checks URL of a page that currently loaded.
     *
     * @param urlMatcher criteria for an URL under the matching.
     * @return instance of {@link AtThePageMatcher}
     */
    public static AtThePageMatcher url(Matcher<URL> urlMatcher) {
        return new AtThePageMatcher(new PageUrlMatcher(urlMatcher));
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
    protected boolean matchesSafely(WrapsDriver item, Description mismatchDescription) {
        boolean result;
        WebDriver driver;
        if ((driver = item.getWrappedDriver()) == null) {
            mismatchDescription.appendText("Wrapped webDriver is null. It is not possible to check current url");
            return false;
        }

        var currentUrlString = driver.getCurrentUrl();
        URL url;
        try {
            url = new URL(currentUrlString);
        } catch (MalformedURLException e) {
            mismatchDescription.appendText("URl " + currentUrlString + " is malformed");
            return false;
        }

        if (ResourceLocatorMatcher.class.isAssignableFrom(urlMatcher.getClass())) {
            result = urlMatcher.matches(url);
            urlMatcher.describeMismatch(url, mismatchDescription);
        } else {
            result = urlMatcher.matches(currentUrlString);
            urlMatcher.describeMismatch(currentUrlString, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("URL of a loaded page %s", urlMatcher.toString());
    }
}
