package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * This matcher is for the checking of a response version.
 */
@Description("protocol version {version}")
public final class HasVersion extends NeptuneFeatureMatcher<HttpResponse<?>> {

    @DescriptionFragment(value = "version")
    private final Matcher<? super HttpClient.Version> versionMatcher;

    private HasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        super(true);
        checkNotNull(versionMatcher, "Matcher of a version is not defined");
        this.versionMatcher = versionMatcher;
    }

    /**
     * Creates a matcher that checks a response version.
     *
     * @param versionMatcher criteria that describes expected response version
     * @return a new instance of {@link HasVersion}
     */
    public static HasVersion hasVersion(Matcher<? super HttpClient.Version> versionMatcher) {
        return new HasVersion(versionMatcher);
    }

    /**
     * Creates a matcher that checks a response version.
     *
     * @param version is the expected version
     * @return a new instance of {@link HasVersion}
     */
    public static HasVersion hasVersion(HttpClient.Version version) {
        return hasVersion(equalTo(version));
    }

    @Override
    protected boolean featureMatches(HttpResponse<?> toMatch) {
        var version = toMatch.version();
        var result = versionMatcher.matches(version);

        if (!result) {
            appendMismatchDescription(versionMatcher, version);
        }

        return result;
    }
}
