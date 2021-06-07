package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.net.http.HttpClient;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static org.hamcrest.Matchers.equalTo;

@Description("Response HTTP version {httpVersion}")
public final class ResponseHasVersion extends NeptuneFeatureMatcher<HarEntry> {

    @DescriptionFragment(value = "httpVersion", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super HttpClient.Version> versionMatcher;

    private ResponseHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        super(true);
        this.versionMatcher = versionMatcher;
    }

    /**
     * Creates matcher that checks HTTP version of the response.
     *
     * @param versionMatcher criteria that describes expected HTTP version
     * @return a new instance of {@link ResponseHasVersion}
     */
    public static Matcher<HarEntry> responseHasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        return new ResponseHasVersion(versionMatcher);
    }

    /**
     * Creates matcher that checks HTTP version of the response.
     *
     * @param version is the expected HTTP version
     * @return a new instance of {@link ResponseHasVersion}
     */
    public static Matcher<HarEntry> responseHasVersion(HttpClient.Version version) {
        return responseHasVersion(equalTo(version));
    }

    @Override
    protected boolean featureMatches(HarEntry toMatch) {
        var responseVersion = toMatch.getResponse().getHttpVersion().equalsIgnoreCase("HTTP/1.1") ? HTTP_1_1 : HTTP_2;
        var result = versionMatcher.matches(responseVersion);

        if (!result) {
            appendMismatchDescription(versionMatcher, responseVersion);
        }

        return result;
    }
}
