package ru.tinkoff.qa.neptune.retrofit2.criteria;

import okhttp3.Response;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.retrofit2.steps.RequestExecutionResult;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.checkByStringContainingOrRegExp;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class ResponseCriteria {

    private ResponseCriteria() {
        super();
    }

    /**
     * Builds criteria to match status code of a response.
     *
     * @param code is the expected status code.
     * @return criteria.
     */
    @Description("status code is {code}")
    public static Criteria<Response> statusCode(@DescriptionFragment("code") int code) {
        return condition(r -> r.code() == code);
    }

    /**
     * Builds criteria to match status code of a response fluently.
     *
     * @param description description of the expectation
     * @param predicate   is the expectation that response body should meet
     * @param <T>         is a type of response body
     * @return criteria
     */
    @Description("Response body: {description}")
    public static <T> Criteria<RequestExecutionResult<T>> bodyMatches(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            Predicate<? super T> predicate) {
        checkArgument(nonNull(predicate), "Predicate that checks response body should be defined");
        checkArgument(isNotBlank(description), "Description should not be defined as a blank or null string");
        return condition(r -> predicate.test(r.getResult()));
    }

    /**
     * Builds criteria to match url of a response.
     *
     * @param stringURL expected url of a response defined as string.
     * @return criteria.
     */
    public static Criteria<Response> responseUrl(String stringURL) {
        return condition(r -> stringURL.equals(r.request().url().toString()));
    }

    /**
     * Builds criteria to match url of a response.
     *
     * @param url expected url of a response defined as {@link URL}.
     * @return criteria.
     */
    public static Criteria<Response> responseUrl(URL url) {
        return responseUrl(url.toString());
    }

    /**
     * Builds criteria to match url of a response.
     *
     * @param expression is a substring that url of a response is supposed to have.
     *                   It is possible to pass reg exp pattern that url should fit.
     * @return criteria.
     */
    @Description("response URL contains '{expression}' or meets regExp pattern '{expression}'")
    public static Criteria<Response> urlMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Http response URL expression should be defined");
        return condition(r -> checkByStringContainingOrRegExp(expression).test(valueOf(r.request().url())));
    }

    @Description("Response URL {description} is '{expected}'")
    private static <T> Criteria<Response> urlPartStringCriteria(@DescriptionFragment("description") String description,
                                                                @DescriptionFragment("expected") String expected,
                                                                Function<URL, String> getPart) {
        checkArgument(isNotBlank(expected), format("Expected URL %s should not be defined as a blank/null string", description));
        return condition(r -> {
            try {
                return Objects.equals(getPart.apply(new URL(r.request().url().toString())), expected);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    @Description("Response URL {description} contains '{expression}' or meets regExp pattern '{expression}'")
    private static Criteria<Response> urlPartRegExpCriteria(@DescriptionFragment("description") String description,
                                                            @DescriptionFragment("expression") String expression,
                                                            Function<URL, String> getPart) {
        checkArgument(isNotBlank(expression), format("expression of URL %s should not be defined as a blank/null string", description));
        return condition(r -> {
            try {
                return checkByStringContainingOrRegExp(expression).test(getPart
                        .apply(new URL(r.request().url().toString())));
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }


    /**
     * Builds criteria to match http response url by host value.
     *
     * @param host is a host value
     * @return criteria.
     */
    public static Criteria<Response> responseURLHost(String host) {
        return urlPartStringCriteria("host", host, URL::getHost);
    }

    /**
     * Builds criteria to match http response url by host value.
     *
     * @param expression is a substring that host is supposed to have.
     *                   It is possible to pass reg exp pattern that host should fit.
     * @return criteria.
     */
    public static Criteria<Response> responseURLHostMatches(String expression) {
        return urlPartRegExpCriteria("host", expression, URL::getHost);
    }

    /**
     * Builds criteria to match http response url by protocol value.
     *
     * @param protocol is a protocol value
     * @return criteria.
     */
    public static Criteria<Response> responseURLProtocol(String protocol) {
        return urlPartStringCriteria("protocol", protocol, URL::getProtocol);
    }

    /**
     * Builds criteria to match http response url by protocol value.
     *
     * @param expression is a substring that protocol is supposed to have.
     *                   It is possible to pass reg exp pattern that scheme should fit.
     * @return criteria.
     */
    public static Criteria<Response> responseURLSchemeMatches(String expression) {
        return urlPartRegExpCriteria("protocol", expression, URL::getProtocol);
    }

    /**
     * Builds criteria to match http response url by path value.
     *
     * @param path is a path value
     * @return criteria.
     */
    public static Criteria<Response> responseURLPath(String path) {
        return urlPartStringCriteria("path", path, URL::getPath);
    }

    /**
     * Builds criteria to match http response url by path value.
     *
     * @param expression is a substring that path is supposed to have.
     *                   It is possible to pass reg exp pattern that path should fit.
     * @return criteria.
     */
    public static Criteria<Response> responseURLPathMatches(String expression) {
        return urlPartRegExpCriteria("path", expression, URL::getPath);
    }

    /**
     * Builds criteria to match http response url by query value.
     *
     * @param query is a query value
     * @return criteria.
     */
    public static Criteria<Response> responseURLQuery(String query) {
        return urlPartStringCriteria("query", query, URL::getQuery);
    }

    /**
     * Builds criteria to match http response url by query value.
     *
     * @param expression is a substring that query is supposed to have.
     *                   It is possible to pass reg exp pattern that query should fit.
     * @return criteria.
     */
    public static Criteria<Response> responseURLQueryMatches(String expression) {
        return urlPartRegExpCriteria("query", expression, URL::getQuery);
    }

    /**
     * Builds criteria to match http response url by user info value.
     *
     * @param userInfo is an user info value
     * @return criteria.
     */
    public static Criteria<Response> responseURLUserInfo(String userInfo) {
        return urlPartStringCriteria("user info", userInfo, URL::getUserInfo);
    }

    /**
     * Builds criteria to match http response url by user info value.
     *
     * @param expression is a substring that user info is supposed to have.
     *                   It is possible to pass reg exp pattern that user info should fit.
     * @return criteria.
     */
    public static Criteria<Response> responseURLUserInfoMatches(String expression) {
        return urlPartRegExpCriteria("user info", expression, URL::getUserInfo);
    }

    /**
     * Builds criteria to match http response url by port value.
     *
     * @param port is a port value
     * @return criteria.
     */
    @Description("Response URL port is '{port}'")
    public static Criteria<Response> responseURLPort(@DescriptionFragment("port") int port) {
        checkArgument(port > 0, "Port value should be greater than 0");
        return condition(r -> {
            try {
                return new URL(r.request().url().toString()).getPort() == port;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    /**
     * Builds criteria to match http response by prior response.
     *
     * @param responseCriteria is criteria for prior response
     * @return criteria.
     */
    @Description("Prior response: {criteria}")
    public static Criteria<Response> priorResponse(@DescriptionFragment("criteria") Criteria<Response> responseCriteria) {
        return condition(r -> responseCriteria.get().test(r.priorResponse()));
    }

    /**
     * Builds criteria to match http response by presence of prior response.
     *
     * @return criteria.
     */
    @Description("Has prior response")
    public static Criteria<Response> priorResponse() {
        return condition(r -> nonNull(r.priorResponse()));
    }

    @Description("Response message is '{expected}'")
    private static Criteria<Response> message(@DescriptionFragment("expected") String expected) {
        checkArgument(isNotBlank(expected), "Message should not be defined as blank or null string");
        return condition(response -> Objects.equals(response.message(), expected));
    }

    @Description("Response URL {description} contains '{expression}' or meets regExp pattern '{expression}'")
    private static Criteria<Response> messageMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Expression for the checking of " +
                "response message should not be defined as blank or null string");
        return condition(r -> checkByStringContainingOrRegExp(expression).test(r.message()));
    }

    /**
     * Checks header of response. It is expected that response has defined header with value that contains
     * defined string.
     *
     * @param name  is the name of header response is supposed to have
     * @param value is the value of header response is supposed to have
     * @return criteria
     */
    @Description("response has header '{name}' with value '{value}'")
    public static Criteria<Response> headerValue(String name, String value) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(value), "Response header value should be defined");

        return condition(r -> r.headers().toMultimap()
                .entrySet()
                .stream()
                .filter(e -> Objects.equals(e.getKey(), name))
                .map(Map.Entry::getValue)
                .anyMatch(strings -> strings.contains(value)));
    }

    /**
     * Checks header of response. It is expected that response has defined header with value that
     * contains string with defined substring / regex.
     *
     * @param name            is the name of header response is supposed to have
     * @param valueExpression is the substring the header value is supposed to have or
     *                        the RegExp the header value is predicted to match
     * @return criteria
     */
    @Description("response has header '{name}' with value contains/matches RegExp pattern '{valueExpression}'")
    public static Criteria<Response> recordedResponseHeaderMatches(@DescriptionFragment("name") String name,
                                                                   @DescriptionFragment("valueExpression") String valueExpression) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Response header value substring/RegExp should be defined");

        return condition(r -> r.headers().toMultimap()
                .entrySet()
                .stream()
                .filter(e -> Objects.equals(e.getKey(), name))
                .map(Map.Entry::getValue)
                .anyMatch(strings -> strings.stream().anyMatch(checkByStringContainingOrRegExp(valueExpression))));
    }
}
