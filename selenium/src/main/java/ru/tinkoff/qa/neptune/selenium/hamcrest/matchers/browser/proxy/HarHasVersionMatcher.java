package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedRequest;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedResponse;

import java.net.http.HttpClient;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static org.hamcrest.Matchers.equalTo;

@Description("{getFrom} HTTP version {httpVersion}")
public final class HarHasVersionMatcher extends NeptuneFeatureMatcher<HarEntry> {

    @DescriptionFragment(value = "getFrom")
    private final Object getFrom;

    @DescriptionFragment(value = "httpVersion")
    private final Matcher<? super HttpClient.Version> versionMatcher;

    private HarHasVersionMatcher(Object getFrom, Matcher<? super HttpClient.Version> versionMatcher) {
        super(true);
        this.getFrom = getFrom;
        this.versionMatcher = versionMatcher;
    }

    /**
     * Creates matcher that checks HTTP version of the response.
     *
     * @param versionMatcher criteria that describes expected HTTP version
     * @return a new instance of {@link HarHasVersionMatcher}
     */
    public static Matcher<HarEntry> responseHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        return new HarHasVersionMatcher(new RecordedResponse(), versionMatcher);
    }

    /**
     * Creates matcher that checks HTTP version of the response.
     *
     * @param version is the expected HTTP version
     * @return a new instance of {@link HarHasVersionMatcher}
     */
    public static Matcher<HarEntry> responseHasVersion(HttpClient.Version version) {
        return responseHasVersion(equalTo(version));
    }

    /**
     * Creates matcher that checks HTTP version of the request.
     *
     * @param versionMatcher criteria that describes expected HTTP version
     * @return a new instance of {@link HarHasVersionMatcher}
     */
    public static Matcher<HarEntry> requestHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        return new HarHasVersionMatcher(new RecordedRequest(), versionMatcher);
    }

    /**
     * Creates matcher that checks HTTP version of the request.
     *
     * @param version is the expected HTTP version
     * @return a new instance of {@link HarHasVersionMatcher}
     */
    public static Matcher<HarEntry> requestHasVersion(HttpClient.Version version) {
        return requestHasVersion(equalTo(version));
    }


    @Override
    protected boolean featureMatches(HarEntry toMatch) {
        String version;
        if (getFrom instanceof RecordedResponse) {
            version = toMatch.getResponse().getHttpVersion();
        } else {
            version = toMatch.getRequest().getHttpVersion();
        }

        var httpVersion = version.equalsIgnoreCase("HTTP/1.1") ? HTTP_1_1 : HTTP_2;
        var result = versionMatcher.matches(httpVersion);

        if (!result) {
            appendMismatchDescription(versionMatcher, httpVersion);
        }

        return result;
    }
}
