package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarQueryParam;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.HarRecordQueryParam;

import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.KEY_MATCHER_MASK;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.VALUE_MATCHER_MASK;

@Description("recorded request has query parameter [{" + KEY_MATCHER_MASK + "}] {" + VALUE_MATCHER_MASK + "}")
public final class RequestHasQueries extends MappedDiagnosticFeatureMatcher<HarEntry, String, String> {

    private RequestHasQueries(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        super(true, nameMatcher, valueMatcher);
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param name  is the expected query parameter name
     * @param value is the expected query parameter value
     * @return a new Matcher
     */
    public static Matcher<HarEntry> requestHasQueryParameter(String name, String value) {
        return requestHasQueryParameter(equalTo(name), value);
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param nameMatcher criteria that describes query parameter name
     * @param value       is the expected query parameter value
     * @return a new Matcher
     */
    public static Matcher<HarEntry> requestHasQueryParameter(Matcher<? super String> nameMatcher, String value) {
        return requestHasQueryParameter(nameMatcher, equalTo(value));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param name         is the expected query parameter name
     * @param valueMatcher criteria that describes query parameter value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HarEntry> requestHasQueryParameter(String name, Matcher<? super String> valueMatcher) {
        return requestHasQueryParameter(name, new Matcher[]{valueMatcher});
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param name          is the expected query parameter name
     * @param valueMatchers criteria that describes query parameter value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HarEntry> requestHasQueryParameter(String name, Matcher<? super String>... valueMatchers) {
        return requestHasQueryParameter(equalTo(name), valueMatchers);
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param nameMatcher   criteria that describes query parameter name
     * @param valueMatchers criteria that describes query parameter value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HarEntry> requestHasQueryParameter(Matcher<? super String> nameMatcher, Matcher<? super String>... valueMatchers) {
        return new RequestHasQueries(nameMatcher, all(valueMatchers));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param nameMatcher  criteria that describes query parameter name
     * @param valueMatcher criteria that describes query parameter value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HarEntry> requestHasQueryParameter(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        return requestHasQueryParameter(nameMatcher, new Matcher[]{valueMatcher});
    }


    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param nameMatcher criteria that describes query parameter name
     * @return a new Matcher
     */
    public static Matcher<HarEntry> requestHasQueryParameterName(Matcher<? super String> nameMatcher) {
        return requestHasQueryParameter(nameMatcher, anything());
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param name is the expected query parameter name
     * @return a new Matcher
     */
    public static Matcher<HarEntry> requestHasQueryParameterName(String name) {
        return requestHasQueryParameterName(equalTo(name));
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param valueMatchers criteria that describes query parameter value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HarEntry> requestHasQueryParameterValue(Matcher<? super String>... valueMatchers) {
        return requestHasQueryParameter(anything(), valueMatchers);
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param valueMatcher criteria that describes query parameter value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HarEntry> requestHasQueryParameterValue(Matcher<? super String> valueMatcher) {
        return requestHasQueryParameterValue(new Matcher[]{valueMatcher});
    }

    /**
     * Creates matcher that checks query parameters of the request.
     *
     * @param value is the expected query parameter value
     * @return a new Matcher
     */
    public static Matcher<HarEntry> requestHasQueryParameterValue(String value) {
        return requestHasQueryParameterValue(equalTo(value));
    }

    @Override
    protected Map<String, String> getMap(HarEntry harEntry) {
        return harEntry
                .getRequest()
                .getQueryString()
                .stream().collect(toMap(HarQueryParam::getName, HarQueryParam::getValue));
    }

    @Override
    protected String getDescriptionOnKeyAbsence() {
        return new HarRecordQueryParam().toString();
    }

    @Override
    protected String getDescriptionOnValueMismatch(String s) {
        return new HarRecordQueryParam()
                + "[" + s + "]";
    }
}
