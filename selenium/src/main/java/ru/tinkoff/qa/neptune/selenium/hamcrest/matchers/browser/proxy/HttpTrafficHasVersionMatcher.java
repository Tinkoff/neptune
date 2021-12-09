package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedResponse;

import java.net.http.HttpClient;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.Matchers.equalTo;

@Description("{getFrom} HTTP version {httpVersion}")
public final class HttpTrafficHasVersionMatcher extends NeptuneFeatureMatcher<HttpTraffic> {

    @DescriptionFragment(value = "getFrom")
    private final Object getFrom;

    @DescriptionFragment(value = "httpVersion")
    private final Matcher<? super HttpClient.Version> versionMatcher;

    private HttpTrafficHasVersionMatcher(Object getFrom, Matcher<? super HttpClient.Version> versionMatcher) {
        super(true);
        this.getFrom = getFrom;
        this.versionMatcher = versionMatcher;
    }

    /**
     * Creates matcher that checks HTTP version of the response.
     *
     * @param versionMatcher criteria that describes expected HTTP version
     * @return a new instance of {@link HttpTrafficHasVersionMatcher}
     */
    public static Matcher<HttpTraffic> responseHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        return new HttpTrafficHasVersionMatcher(new RecordedResponse(), versionMatcher);
    }

    /**
     * Creates matcher that checks HTTP version of the response.
     *
     * @param version is the expected HTTP version
     * @return a new instance of {@link HttpTrafficHasVersionMatcher}
     */
    public static Matcher<HttpTraffic> responseHasVersion(HttpClient.Version version) {
        return responseHasVersion(equalTo(version));
    }

    @Override
    protected boolean featureMatches(HttpTraffic toMatch) {
        String version = toMatch.getResponse().getResponse().getProtocol().orElse(EMPTY);

        var httpVersion = version.equalsIgnoreCase("HTTP/1.1") ? HTTP_1_1 : HTTP_2;
        var result = versionMatcher.matches(httpVersion);

        if (!result) {
            appendMismatchDescription(versionMatcher, httpVersion);
        }

        return result;
    }
}
