package ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URI;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("Scheme {" + MATCHER_FRAGMENT + "}")
public final class HasSchemeMatcher extends ResourceLocatorMatcher<URI, String> {

    private HasSchemeMatcher(Matcher<? super String> matcher, Function<URI, String> conversion) {
        super(URI.class, matcher, conversion);
    }

    /**
     * Creates matcher that verifies the scheme of an URI
     *
     * @param schemeMather that checks the scheme value
     * @return new {@link HasSchemeMatcher}
     */
    public static HasSchemeMatcher uriHasScheme(Matcher<? super String> schemeMather) {
        return new HasSchemeMatcher(schemeMather, URI::getScheme);
    }

    /**
     * Creates matcher that verifies the scheme of an URI
     *
     * @param scheme is the expected value of the scheme
     * @return new {@link HasSchemeMatcher}
     */
    public static HasSchemeMatcher uriHasScheme(String scheme) {
        return new HasSchemeMatcher(equalTo(scheme), URI::getScheme);
    }
}
