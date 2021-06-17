package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.KEY_MATCHER_MASK;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.VALUE_MATCHER_MASK;

@Description("has query parameter {" + KEY_MATCHER_MASK + "} {" + VALUE_MATCHER_MASK + "}")
public final class HasQueryParameters<T> extends MappedDiagnosticFeatureMatcher<T, String, Iterable<String>> {

    private final Function<T, Map<String, Iterable<String>>> getParameterMap;

    protected HasQueryParameters(Class<T> expectedType, Matcher<? super String> nameParameterMatcher, Matcher<Iterable<String>> valueParameterMatcher, Function<T, Map<String, Iterable<String>>> getParameterMap) {
        super(true, nameParameterMatcher, valueParameterMatcher, expectedType);
        checkNotNull(getParameterMap);
        this.getParameterMap = getParameterMap;
    }

    /**
     * Creates matcher that verifies query parameters of an URI. It is expected that query part has a parameter whose name
     * meets {@code paramNameMatcher} and value that meets criteria defined by {@code valueMatchers}
     *
     * @param paramNameMatcher      matcher to find and validate query parameter by name
     * @param valueParameterMatcher matcher to check value of the query parameter
     * @return a new matcher
     */
    public static Matcher<URI> uriHasQueryParameter(Matcher<? super String> paramNameMatcher,
                                                    Matcher<Iterable<String>> valueParameterMatcher) {
        return new HasQueryParameters<>(URI.class,
                paramNameMatcher,
                valueParameterMatcher,
                uri -> new GetQueryMapFunc().apply(uri.getQuery()));
    }

    /**
     * Creates matcher that verifies query parameters of an URI. It is expected that query part has defined name
     * and value that meets criteria defined by {@code valueMatchers}
     *
     * @param paramName             expected name of the parameter
     * @param valueParameterMatcher matcher to check value of the query parameter
     * @return a new matcher
     */
    public static Matcher<URI> uriHasQueryParameter(String paramName,
                                                    Matcher<Iterable<String>> valueParameterMatcher) {
        return uriHasQueryParameter(equalTo(paramName), valueParameterMatcher);
    }

    /**
     * Creates matcher that verifies query parameters of an URL. It is expected that query part has a parameter whose name
     * meets {@code paramNameMatcher} and value that meets criteria defined by {@code valueMatchers}
     *
     * @param paramNameMatcher      matcher to find and validate query parameter by name
     * @param valueParameterMatcher matcher to check value of the query parameter
     * @return a new matcher
     */
    public static Matcher<URL> urlHasQueryParameter(Matcher<? super String> paramNameMatcher,
                                                    Matcher<Iterable<String>> valueParameterMatcher) {
        return new HasQueryParameters<>(URL.class,
                paramNameMatcher,
                valueParameterMatcher,
                url -> new GetQueryMapFunc().apply(url.getQuery()));
    }

    /**
     * Creates matcher that verifies query parameters of an URL. It is expected that query part has defined name
     * and value that meets criteria defined by {@code valueMatchers}
     *
     * @param paramName             expected name of the parameter
     * @param valueParameterMatcher matcher to check value of the query parameter
     * @return a new matcher
     */
    public static Matcher<URL> urlHasQueryParameter(String paramName,
                                                    Matcher<Iterable<String>> valueParameterMatcher) {
        return urlHasQueryParameter(equalTo(paramName), valueParameterMatcher);
    }

    @Override
    protected Map<String, Iterable<String>> getMap(T t) {
        return getParameterMap.apply(t);
    }

    @Override
    protected String getDescriptionOnKeyAbsence() {
        return new QueryParameter(null).toString();
    }

    @Override
    protected String getDescriptionOnValueMismatch(String key) {
        return new QueryParameter(key).toString();
    }

    private static final class GetQueryMapFunc implements Function<String, Map<String, Iterable<String>>> {

        @Override
        public Map<String, Iterable<String>> apply(String s) {
            if (isBlank(s)) {
                return null;
            }

            var result = new LinkedHashMap<String, Iterable<String>>();
            stream(s.split("&")).forEach(s1 -> {
                var keyValue = s1.split("=");
                var list = result.computeIfAbsent(keyValue[0], s2 -> new LinkedList<>());
                ((List<String>) list).add(keyValue[1]);
            });

            return result;
        }
    }
}
