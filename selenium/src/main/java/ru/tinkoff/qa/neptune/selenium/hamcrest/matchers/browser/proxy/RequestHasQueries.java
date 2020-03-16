package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarQueryParam;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasItem;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.HasHarPair.hasHarPair;

public final class RequestHasQueries extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super List<HarQueryParam>> queryMatcher;

    private RequestHasQueries(Matcher<? super List<HarNameValuePair>> queryMatcher) {
        this.queryMatcher = queryMatcher;
    }

    private static RequestHasQueries requestHasQueries(Matcher<? super List<HarNameValuePair>> queryMatcher) {
        return new RequestHasQueries(queryMatcher);
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param name  is the expected query parameter name
     * @param value is the expected query parameter value
     * @return a new instance of {@link RequestHasQueries}
     */
    public static RequestHasQueries requestHasQuery(String name, String value) {
        return new RequestHasQueries(hasItem(hasHarPair(name, value)));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param nameMatcher criteria that describes query parameter name
     * @param value       is the expected query parameter value
     * @return a new instance of {@link RequestHasQueries}
     */
    public static RequestHasQueries requestHasQuery(Matcher<? super String> nameMatcher, String value) {
        return new RequestHasQueries(hasItem(hasHarPair(nameMatcher, value)));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param name         is the expected query parameter name
     * @param valueMatcher criteria that describes query parameter value
     * @return a new instance of {@link RequestHasQueries}
     */
    public static RequestHasQueries requestHasQuery(String name, Matcher<? super String> valueMatcher) {
        return new RequestHasQueries(hasItem(hasHarPair(name, valueMatcher)));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param nameMatcher  criteria that describes query parameter name
     * @param valueMatcher criteria that describes query parameter value
     * @return a new instance of {@link RequestHasQueries}
     */
    public static RequestHasQueries requestHasQuery(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        return new RequestHasQueries(hasItem(hasHarPair(nameMatcher, valueMatcher)));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param nameMatcher criteria that describes query parameter name
     * @return a new instance of {@link RequestHasQueries}
     */
    public static RequestHasQueries requestHasQueryName(Matcher<? super String> nameMatcher) {
        return new RequestHasQueries(hasItem(hasHarPair(nameMatcher, anything())));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param name is the expected query parameter name
     * @return a new instance of {@link RequestHasQueries}
     */
    public static RequestHasQueries requestHasQueryName(String name) {
        return new RequestHasQueries(hasItem(hasHarPair(name, anything())));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param valueMatcher criteria that describes query parameter value
     * @return a new instance of {@link RequestHasQueries}
     */
    public static RequestHasQueries requestHasQueryValue(Matcher<? super String> valueMatcher) {
        return new RequestHasQueries(hasItem(hasHarPair(anything(), valueMatcher)));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param value is the expected query parameter value
     * @return a new instance of {@link RequestHasQueries}
     */
    public static RequestHasQueries requestHasQueryValue(String value) {
        return new RequestHasQueries(hasItem(hasHarPair(anything(), value)));
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var requestQueryParams = item.getRequest().getQueryString();
        var result = queryMatcher.matches(requestQueryParams);

        if (!result) {
            queryMatcher.describeMismatch(requestQueryParams, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("request has query parameters %s", queryMatcher);
    }
}
