package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("Url reference {" + MATCHER_FRAGMENT + "}")
public final class HasReferenceMatcher extends ResourceLocatorMatcher<URL, String> {

    private HasReferenceMatcher(Matcher<? super String> matcher) {
        super(URL.class, matcher, URL::getRef);
    }

    /**
     * Creates matcher that verifies the reference of an URL
     *
     * @param refMather that checks the reference value
     * @return new {@link HasReferenceMatcher}
     */
    public static HasReferenceMatcher urlHasReference(Matcher<? super String> refMather) {
        return new HasReferenceMatcher(refMather);
    }

    /**
     * Creates matcher that verifies the reference of an URL
     *
     * @param ref is the expected value of the reference
     * @return new {@link HasReferenceMatcher}
     */
    public static HasReferenceMatcher urlHasReference(String ref) {
        return new HasReferenceMatcher(equalTo(ref));
    }
}
