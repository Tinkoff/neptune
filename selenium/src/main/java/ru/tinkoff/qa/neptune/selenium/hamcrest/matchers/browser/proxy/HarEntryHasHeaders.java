package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarHeader;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.HarRecordHeader;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.NoSuchHarHeaderNameMismatch;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.anything;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.entryKey;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntryValue;

@Description("{getFrom} has header [{nameMatcher}] {valueMatcher}")
public class HarEntryHasHeaders extends NeptuneFeatureMatcher<HarEntry> {

    @DescriptionFragment(value = "getFrom", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Object getFrom;

    @DescriptionFragment(value = "nameMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super String> nameMatcher;

    @DescriptionFragment(value = "valueMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super String> valueMatcher;

    protected HarEntryHasHeaders(Object getFrom, Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        super(true);
        checkNotNull(getFrom);
        this.getFrom = getFrom;
        this.nameMatcher = nameMatcher;
        this.valueMatcher = valueMatcher;
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name  is the expected header name
     * @param value is the expected header value
     * @return a new Matcher
     */
    public static ResponseHasHeaders responseHasHeader(String name, String value) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(name, value)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher criteria that describes header name
     * @param value       is the expected header value
     * @return a new Matcher
     */
    public static ResponseHasHeaders responseHasHeader(Matcher<? super String> nameMatcher, String value) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(nameMatcher, value)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name         is the expected header name
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    public static ResponseHasHeaders responseHasHeader(String name, Matcher<? super String> valueMatcher) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(name, valueMatcher)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher   criteria that describes header name
     * @param valueMatchers criteria that describes header value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HarEntry> responseHasHeader(Matcher<? super String> nameMatcher, Matcher<? super String>... valueMatchers) {
        return new HarEntryHasHeaders(new RecordedResponse(), nameMatcher, all(valueMatchers));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher  criteria that describes header name
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HarEntry> responseHasHeader(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        return responseHasHeader(nameMatcher, new Matcher[]{valueMatcher});
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher criteria that describes header name
     * @return a new Matcher
     */
    public static ResponseHasHeaders responseHasHeaderName(Matcher<? super String> nameMatcher) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(nameMatcher, anything())));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name is the expected header name
     * @return a new Matcher
     */
    public static ResponseHasHeaders responseHasHeaderName(String name) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(name, anything())));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    public static ResponseHasHeaders responseHasHeaderValue(Matcher<? super String> valueMatcher) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(anything(), valueMatcher)));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param value is the expected header value
     * @return a new Matcher
     */
    public static ResponseHasHeaders responseHasHeaderValue(String value) {
        return new ResponseHasHeaders(hasItem(hasHarHeader(anything(), value)));
    }

    @Override
    protected boolean featureMatches(HarEntry toMatch) {
        List<HarHeader> headers;

        if (getFrom instanceof RecordedResponse) {
            headers = toMatch.getResponse().getHeaders();
        } else {
            headers = toMatch.getRequest().getHeaders();
        }

        var headerMap = headers.stream().collect(Collectors.toMap(HarHeader::getName, HarHeader::getValue));

        var entryMatcher = entryKey(nameMatcher);
        var foundHeaders = headerMap
                .entrySet()
                .stream()
                .filter(entryMatcher::matches)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (foundHeaders.size() == 0) {
            appendMismatchDescription(new NoSuchHarHeaderNameMismatch(nameMatcher));
            return false;
        }

        if (!mapHasEntryValue(valueMatcher).matches(foundHeaders)) {

            foundHeaders.entrySet().forEach(e -> {
                if (!valueMatcher.matches(e)) {
                    appendMismatchDescription(new PropertyValueMismatch(new HarRecordHeader()
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
