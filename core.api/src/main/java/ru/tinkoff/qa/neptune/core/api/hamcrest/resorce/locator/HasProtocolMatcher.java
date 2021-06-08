package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.ResourceLocatorMatcher.MATCHER_FRAGMENT;

@Description("Protocol {" + MATCHER_FRAGMENT + "}")
public final class HasProtocolMatcher extends ResourceLocatorMatcher<URL, String> {

    private HasProtocolMatcher(Matcher<? super String> matcher, Function<URL, String> conversion) {
        super(URL.class, matcher, conversion);
    }

    /**
     * Creates matcher that verifies the path of an URL
     *
     * @param pathMather that checks the path value
     * @return new {@link HasProtocolMatcher}
     */
    public static HasProtocolMatcher urlHasProtocol(Matcher<? super String> pathMather) {
        return new HasProtocolMatcher(pathMather, URL::getProtocol);
    }

    /**
     * Creates matcher that verifies the path of an URL
     *
     * @param path is the expected value of the path
     * @return new {@link HasProtocolMatcher}
     */
    public static HasProtocolMatcher urlHasProtocol(String path) {
        return new HasProtocolMatcher(equalTo(path), URL::getProtocol);
    }
}
