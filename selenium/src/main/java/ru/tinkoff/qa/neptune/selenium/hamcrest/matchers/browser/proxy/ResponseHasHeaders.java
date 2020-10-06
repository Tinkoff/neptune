package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarHeader;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasItem;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.HasHarHeader.hasHarHeader;

public final class ResponseHasHeaders extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super List<HarHeader>> headerMatcher;

    private ResponseHasHeaders(Matcher<? super List<HarHeader>> headerMatcher) {
        this.headerMatcher = headerMatcher;
    }

    private static ResponseHasHeaders responseHasHeaders(Matcher<? super List<HarHeader>> headerMatcher) {
        return new ResponseHasHeaders(headerMatcher);
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name  is the expected header name
     * @param value is the expected header value
     * @return a new instance of {@link ResponseHasHeaders}
     */
    public static ResponseHasHeaders responseHasHeader(String name, String value) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(name, value)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher criteria that describes header name
     * @param value       is the expected header value
     * @return a new instance of {@link ResponseHasHeaders}
     */
    public static ResponseHasHeaders responseHasHeader(Matcher<? super String> nameMatcher, String value) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(nameMatcher, value)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name         is the expected header name
     * @param valueMatcher criteria that describes header value
     * @return a new instance of {@link ResponseHasHeaders}
     */
    public static ResponseHasHeaders responseHasHeader(String name, Matcher<? super String> valueMatcher) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(name, valueMatcher)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher  criteria that describes header name
     * @param valueMatcher criteria that describes header value
     * @return a new instance of {@link ResponseHasHeaders}
     */
    public static ResponseHasHeaders responseHasHeader(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(nameMatcher, valueMatcher)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher criteria that describes header name
     * @return a new instance of {@link ResponseHasHeaders}
     */
    public static ResponseHasHeaders responseHasHeaderName(Matcher<? super String> nameMatcher) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(nameMatcher, anything())));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name is the expected header name
     * @return a new instance of {@link ResponseHasHeaders}
     */
    public static ResponseHasHeaders responseHasHeaderName(String name) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(name, anything())));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param valueMatcher criteria that describes header value
     * @return a new instance of {@link ResponseHasHeaders}
     */
    public static ResponseHasHeaders responseHasHeaderValue(Matcher<? super String> valueMatcher) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(anything(), valueMatcher)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param value is the expected header value
     * @return a new instance of {@link ResponseHasHeaders}
     */
    public static ResponseHasHeaders responseHasHeaderValue(String value) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(anything(), value)));
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var responseHeaders = item.getResponse().getHeaders();
        var result = headerMatcher.matches(responseHeaders);

        if (!result) {
            headerMatcher.describeMismatch(responseHeaders, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("response has headers %s", headerMatcher);
    }
}
