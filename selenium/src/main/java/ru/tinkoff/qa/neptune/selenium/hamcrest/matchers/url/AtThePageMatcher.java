package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.url;

import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;

public final class AtThePageMatcher extends TypeSafeDiagnosingMatcher<WrapsDriver> {

    private final Matcher<String> urlMatcher;

    private AtThePageMatcher(Matcher<String> urlMatcher) {
        checkArgument(urlMatcher != null, "Criteria for the matching of an URL should be defined");
        this.urlMatcher = urlMatcher;
    }

    /**
     * Creates an instance of {@link AtThePageMatcher} which checks URL of a page that currently loaded.
     *
     * @param urlMatcher criteria for an URL under the matching.
     * @return instance of {@link AtThePageMatcher}
     */
    public static AtThePageMatcher atThePage(Matcher<String> urlMatcher) {
        return new AtThePageMatcher(urlMatcher);
    }

    /**
     * Creates an instance of {@link AtThePageMatcher} which checks URL of a page that currently loaded.
     *
     * @param url expected URL of a page.
     * @return instance of {@link AtThePageMatcher}
     */
    public static AtThePageMatcher atThePage(String url) {
        return atThePage(equalTo(url));
    }

    @Override
    protected boolean matchesSafely(WrapsDriver item, Description mismatchDescription) {
        boolean result;
        WebDriver driver;
        if ((driver = item.getWrappedDriver()) == null) {
            mismatchDescription.appendText("Wrapped webDriver is null. It is not possible to check current url");
            return false;
        }

        String currentUrl = driver.getCurrentUrl();
        result = urlMatcher.matches(currentUrl);
        if (!result) {
            urlMatcher.describeMismatch(currentUrl, mismatchDescription);
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    @Override
    public String toString() {
        return format("URL of a loaded page %s", urlMatcher.toString());
    }
}
