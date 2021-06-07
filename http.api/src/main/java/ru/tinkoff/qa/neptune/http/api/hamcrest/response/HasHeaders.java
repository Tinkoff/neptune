package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.net.http.HttpResponse;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.AnyThingMatcher.anything;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.entryKey;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntryValue;

/**
 * This matcher is for the checking of headers of a response.
 */
@Description("response header name: <{nameMatcher}> and value <{valueMatcher}>")
public final class HasHeaders extends NeptuneFeatureMatcher<HttpResponse<?>> {

    @DescriptionFragment(value = "nameMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super String> nameMatcher;

    @DescriptionFragment(value = "valueMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Iterable<String>> valueMatcher;

    private HasHeaders(Matcher<? super String> nameMatcher, Matcher<Iterable<String>> valueMatcher) {
        super(true);
        this.nameMatcher = nameMatcher;
        this.valueMatcher = valueMatcher;
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
    protected boolean featureMatches(HttpResponse<?> toMatch) {
        var headers = toMatch.headers().map();

        var entryMatcher = entryKey(nameMatcher);
        var foundHeaders = headers
                .entrySet()
                .stream()
                .filter(entryMatcher::matches)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (foundHeaders.size() == 0) {
            appendMismatchDescription(new NoSuchHeaderNameMismatch(nameMatcher));
            return false;
        }

        if (!mapHasEntryValue(valueMatcher).matches(foundHeaders)) {

            foundHeaders.entrySet().forEach(e -> {
                if (!valueMatcher.matches(e)) {
                    appendMismatchDescription(new PropertyValueMismatch(new Header()
                            + "[" + e.getKey() + "]",
                            e.getValue(),
                            valueMatcher));
                }
            });
            return false;
        }

        return true;
    }
}
