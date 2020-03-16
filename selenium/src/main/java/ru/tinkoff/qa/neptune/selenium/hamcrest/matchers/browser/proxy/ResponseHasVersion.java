package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.net.http.HttpClient;

import static java.lang.String.format;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static org.hamcrest.Matchers.is;

public final class ResponseHasVersion extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super HttpClient.Version> versionMatcher;

    private ResponseHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        this.versionMatcher = versionMatcher;
    }

    /**
     * Creates matcher that checks HTTP version of the response.
     *
     * @param versionMatcher criteria that describes expected HTTP version
     * @return a new instance of {@link ResponseHasVersion}
     */
    public static ResponseHasVersion responseHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        return new ResponseHasVersion(versionMatcher);
    }

    /**
     * Creates matcher that checks HTTP version of the response.
     *
     * @param version is the expected HTTP version
     * @return a new instance of {@link ResponseHasVersion}
     */
    public static ResponseHasVersion responseHasVersion(HttpClient.Version version) {
        return new ResponseHasVersion(is(version));
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var responseVersion = item.getResponse().getHttpVersion().equals("HTTP/1.1") ? HTTP_1_1 : HTTP_2;
        var result = versionMatcher.matches(responseVersion);

        if (!result) {
            versionMatcher.describeMismatch(responseVersion, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("response has HTTP version %s", versionMatcher);
    }
}
