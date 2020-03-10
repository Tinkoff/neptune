package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import net.lightbody.bmp.core.har.HarEntry;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.net.http.HttpClient;

import static java.lang.String.format;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static org.hamcrest.Matchers.is;

public final class RequestHasVersion extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super HttpClient.Version> versionMatcher;

    private RequestHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        this.versionMatcher = versionMatcher;
    }

    /**
     * Creates matcher that checks HTTP version of the request.
     *
     * @param versionMatcher criteria that describes expected HTTP version
     * @return a new instance of {@link RequestHasVersion}
     */
    public static RequestHasVersion requestHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        return new RequestHasVersion(versionMatcher);
    }

    /**
     * Creates matcher that checks HTTP version of the request.
     *
     * @param version is the expected HTTP version
     * @return a new instance of {@link RequestHasVersion}
     */
    public static RequestHasVersion requestHasVersion(HttpClient.Version version) {
        return new RequestHasVersion(is(version));
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var requestVersion = item.getRequest().getHttpVersion().equals("HTTP/1.1") ? HTTP_1_1 : HTTP_2;
        var result = versionMatcher.matches(requestVersion);

        if (!result) {
            versionMatcher.describeMismatch(requestVersion, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("request has HTTP version %s", versionMatcher);
    }
}
