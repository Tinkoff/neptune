package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;

import java.net.URL;

import static org.hamcrest.Matchers.equalTo;

public final class HasReferenceMatcher extends ResourceLocatorMatcher<URL, String> {

    private HasReferenceMatcher(Matcher<? super String> matcher) {
        super("Url reference", matcher, URL::getRef);
    }

    /**
     * Creates matcher that verifies the reference of an URL
     *
     * @param refMather that checks the reference value
     * @return new {@link HasReferenceMatcher}
     */
    public static HasReferenceMatcher urReference(Matcher<? super String> refMather) {
        return new HasReferenceMatcher(refMather);
    }

    /**
     * Creates matcher that verifies the reference of an URL
     *
     * @param ref is the expected value of the reference
     * @return new {@link HasReferenceMatcher}
     */
    public static HasReferenceMatcher urlReference(String ref) {
        return new HasReferenceMatcher(equalTo(ref));
    }
}
