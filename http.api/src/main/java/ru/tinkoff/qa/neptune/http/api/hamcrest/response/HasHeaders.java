package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.AnyThingMatcher.anything;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.KEY_MATCHER_MASK;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.VALUE_MATCHER_MASK;

/**
 * This matcher is for the checking of headers of a response.
 */
@Description("response header name <{" + KEY_MATCHER_MASK + "}> <{" + VALUE_MATCHER_MASK + "}>")
public final class HasHeaders extends MappedDiagnosticFeatureMatcher<HttpResponse<?>, String, List<String>> {

    private HasHeaders(Matcher<? super String> nameMatcher, Matcher<Iterable<String>> valueMatcher) {
        super(true, nameMatcher, valueMatcher);
    }

    private static HasHeaders hasHeaders(Matcher<? super String> nameMatcher, Matcher<Iterable<String>> valueMatcher) {
        return new HasHeaders(nameMatcher, valueMatcher);
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not.
     * Also it checks value of the header
     *
     * @param key   is the expected header name
     * @param value is expected value of the key
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeader(String key, String... value) {
        return hasHeaders(equalTo(key), iterableInOrder(value));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not.
     * Also it checks value of the header
     *
     * @param keyMatcher is criteria that describes expected header name
     * @param value      is expected value of the key
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeader(Matcher<? super String> keyMatcher, String... value) {
        return hasHeaders(keyMatcher, iterableInOrder(value));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not.
     * Also it checks value of the header
     *
     * @param key          is the expected header name
     * @param valueMatcher is criteria that describes expected value
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeader(String key, Matcher<Iterable<String>> valueMatcher) {
        return hasHeaders(equalTo(key), valueMatcher);
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not.
     * Also it checks value of the header
     *
     * @param keyMatcher   is criteria that describes expected header name
     * @param valueMatcher is criteria that describes expected value
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeader(Matcher<? super String> keyMatcher, Matcher<Iterable<String>> valueMatcher) {
        return hasHeaders(keyMatcher, valueMatcher);
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not
     *
     * @param headerMatcher criteria that describes expected header name
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeaderName(Matcher<? super String> headerMatcher) {
        return hasHeaders(headerMatcher, anything());
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not
     *
     * @param name expected header name
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeaderName(String name) {
        return hasHeaderName(equalTo(name));
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (value) is present or not
     *
     * @param valueMatcher criteria that describes expected header value
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeaderValue(Matcher<Iterable<String>> valueMatcher) {
        return hasHeaders(anything(), valueMatcher);
    }

    /**
     * Creates a matcher that checks headers of a response. It verifies whenever header (name) is present or not
     *
     * @param value expected header value
     * @return a new instance of {@link HasHeaders}
     */
    public static HasHeaders hasHeaderValue(String... value) {
        return hasHeaderValue(iterableInOrder(value));
    }

    @Override
    protected Map<String, List<String>> getMap(HttpResponse<?> httpResponse) {
        return httpResponse.headers().map();
    }

    @Override
    protected String getDescriptionOnKeyAbsence() {
        return new Header().toString();
    }

    @Override
    protected String getDescriptionOnValueMismatch(String s) {
        return new Header() + "[" + s + "]";
    }
}
