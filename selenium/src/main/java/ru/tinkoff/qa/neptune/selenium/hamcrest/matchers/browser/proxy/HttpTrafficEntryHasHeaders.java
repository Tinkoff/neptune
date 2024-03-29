package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.HarRecordHeader;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedRequest;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedResponse;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.KEY_MATCHER_MASK;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.VALUE_MATCHER_MASK;

@Description("{getFrom} has header [{" + KEY_MATCHER_MASK + "}] {" + VALUE_MATCHER_MASK + "}")
public class HttpTrafficEntryHasHeaders extends MappedDiagnosticFeatureMatcher<HttpTraffic, String, String> {

    @DescriptionFragment(value = "getFrom")
    private final Object getFrom;

    private HttpTrafficEntryHasHeaders(Object getFrom, Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        super(true, nameMatcher, valueMatcher);
        checkNotNull(getFrom);
        this.getFrom = getFrom;
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name  is the expected header name
     * @param value is the expected header value
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> responseHasHeader(String name, String value) {
        return responseHasHeader(equalTo(name), value);
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher criteria that describes header name
     * @param value       is the expected header value
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> responseHasHeader(Matcher<? super String> nameMatcher, String value) {
        return responseHasHeader(nameMatcher, equalTo(value));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name         is the expected header name
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HttpTraffic> responseHasHeader(String name, Matcher<? super String> valueMatcher) {
        return responseHasHeader(name, new Matcher[]{valueMatcher});
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name          is the expected header name
     * @param valueMatchers criteria that describes header value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HttpTraffic> responseHasHeader(String name, Matcher<? super String>... valueMatchers) {
        return responseHasHeader(equalTo(name), valueMatchers);
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher   criteria that describes header name
     * @param valueMatchers criteria that describes header value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HttpTraffic> responseHasHeader(Matcher<? super String> nameMatcher, Matcher<? super String>... valueMatchers) {
        return new HttpTrafficEntryHasHeaders(new RecordedResponse(), nameMatcher, all(valueMatchers));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher  criteria that describes header name
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HttpTraffic> responseHasHeader(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        return responseHasHeader(nameMatcher, new Matcher[]{valueMatcher});
    }


    /**
     * Creates matcher that checks headers of the response.
     *
     * @param nameMatcher criteria that describes header name
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> responseHasHeaderName(Matcher<? super String> nameMatcher) {
        return responseHasHeader(nameMatcher, anything());
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param name is the expected header name
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> responseHasHeaderName(String name) {
        return responseHasHeaderName(equalTo(name));
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param valueMatchers criteria that describes header value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HttpTraffic> responseHasHeaderValue(Matcher<? super String>... valueMatchers) {
        return responseHasHeader(anything(), valueMatchers);
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HttpTraffic> responseHasHeaderValue(Matcher<? super String> valueMatcher) {
        return responseHasHeaderValue(new Matcher[]{valueMatcher});
    }

    /**
     * Creates matcher that checks headers of the response.
     *
     * @param value is the expected header value
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> responseHasHeaderValue(String value) {
        return responseHasHeaderValue(equalTo(value));
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param name  is the expected header name
     * @param value is the expected header value
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> requestHasHeader(String name, String value) {
        return requestHasHeader(equalTo(name), value);
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param nameMatcher criteria that describes header name
     * @param value       is the expected header value
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> requestHasHeader(Matcher<? super String> nameMatcher, String value) {
        return requestHasHeader(nameMatcher, equalTo(value));
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param name         is the expected header name
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HttpTraffic> requestHasHeader(String name, Matcher<? super String> valueMatcher) {
        return requestHasHeader(name, new Matcher[]{valueMatcher});
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param name          is the expected header name
     * @param valueMatchers criteria that describes header value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HttpTraffic> requestHasHeader(String name, Matcher<? super String>... valueMatchers) {
        return requestHasHeader(equalTo(name), valueMatchers);
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param nameMatcher   criteria that describes header name
     * @param valueMatchers criteria that describes header value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HttpTraffic> requestHasHeader(Matcher<? super String> nameMatcher, Matcher<? super String>... valueMatchers) {
        return new HttpTrafficEntryHasHeaders(new RecordedRequest(), nameMatcher, all(valueMatchers));
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param nameMatcher  criteria that describes header name
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HttpTraffic> requestHasHeader(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        return requestHasHeader(nameMatcher, new Matcher[]{valueMatcher});
    }


    /**
     * Creates matcher that checks headers of the request.
     *
     * @param nameMatcher criteria that describes header name
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> requestHasHeaderName(Matcher<? super String> nameMatcher) {
        return requestHasHeader(nameMatcher, anything());
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param name is the expected header name
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> requestHasHeaderName(String name) {
        return requestHasHeaderName(equalTo(name));
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param valueMatchers criteria that describes header value
     * @return a new Matcher
     */
    @SafeVarargs
    public static Matcher<HttpTraffic> requestHasHeaderValue(Matcher<? super String>... valueMatchers) {
        return requestHasHeader(anything(), valueMatchers);
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param valueMatcher criteria that describes header value
     * @return a new Matcher
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HttpTraffic> requestHasHeaderValue(Matcher<? super String> valueMatcher) {
        return requestHasHeaderValue(new Matcher[]{valueMatcher});
    }

    /**
     * Creates matcher that checks headers of the request.
     *
     * @param value is the expected header value
     * @return a new Matcher
     */
    public static Matcher<HttpTraffic> requestHasHeaderValue(String value) {
        return requestHasHeaderValue(equalTo(value));
    }

    @Override
    protected Map<String, String> getMap(HttpTraffic httpTraffic) {
        Map<String, Object> headers;

        if (getFrom instanceof RecordedResponse) {
            headers = httpTraffic.getResponse().getResponse().getHeaders().toJson();
        } else {
            headers = httpTraffic.getRequest().getRequest().getHeaders().toJson();
        }

        return headers.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> valueOf(entry.getValue())));
    }

    @Override
    protected String getDescriptionOnKeyAbsence() {
        return new HarRecordHeader(null).toString();
    }

    @Override
    protected String getDescriptionOnValueMismatch(String s) {
        return new HarRecordHeader(s).toString();
    }
}
