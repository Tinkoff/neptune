package ru.tinkoff.qa.neptune.selenium.functions.browser.proxy;

import io.netty.handler.codec.http.HttpMethod;
import org.openqa.selenium.devtools.v102.network.Network;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.checkByStringContainingOrRegExp;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class BrowserProxyCriteria {

    private BrowserProxyCriteria() {
        super();
    }

    /**
     * Checks the url of request.
     *
     * @param url is the url request is supposed to have
     * @return criteria that checks HttpTraffic entry
     */
    @Description("request url equals to '{url}'")
    public static Criteria<HttpTraffic> recordedRequestUrl(@DescriptionFragment("url") String url) {
        checkArgument(isNotBlank(url), "URL should be defined");

        return condition(entry -> {
            var request = entry.getRequest().getRequest();
            String requestUrl = request.getUrl() + request.getUrlFragment().orElse(EMPTY);

            return Objects.equals(requestUrl, url);
        });
    }

    /**
     * Checks the url of request.
     *
     * @param urlExpression is the substring the url is supposed to have or
     *                      the RegExp the url is predicted to match
     * @return criteria that checks HttpTraffic entry
     */
    @Description("request url contains '{urlExpression}' or fits regExp pattern '{urlExpression}'")
    public static Criteria<HttpTraffic> recordedRequestUrlMatches(@DescriptionFragment("urlExpression") String urlExpression) {
        checkArgument(isNotBlank(urlExpression), "URL Substring/RegExp should be defined");

        return condition(entry -> {
            var request = entry.getRequest().getRequest();
            String requestUrl = request.getUrl() + request.getUrlFragment().orElse(EMPTY);

            return Optional.of(requestUrl)
                    .map(s -> checkByStringContainingOrRegExp(urlExpression).test(s))
                    .orElse(false);
        });
    }

    /**
     * Checks HTTP method of request.
     *
     * @param method is the name of HTTP method request is supposed to have
     * @return criteria that checks HttpTraffic entry
     */
    @Description("request with method '{method}'")
    public static Criteria<HttpTraffic> recordedRequestMethod(@DescriptionFragment("method") HttpMethod method) {
        checkArgument(nonNull(method), "Method should be defined");

        return condition(entry -> {
            HttpMethod requestMethod = HttpMethod.valueOf(entry.getRequest().getRequest().getMethod());

            return Objects.equals(requestMethod, method);
        });
    }

    /**
     * Checks HTTP version of response.
     *
     * @param version is the HTTP version response is supposed to have
     * @return criteria that checks HttpTraffic entry
     */
    @Description("response with HTTP version '{version}'")
    public static Criteria<HttpTraffic> recordedResponseHttpVersion(@DescriptionFragment("version") HttpClient.Version version) {
        checkArgument(nonNull(version), "Http version should be defined");

        return condition(entry -> {
            HttpClient.Version httpVersion = entry.getResponse()
                    .getResponse()
                    .getProtocol()
                    .orElse(EMPTY)
                    .equalsIgnoreCase("HTTP/1.1") ? HTTP_1_1 : HTTP_2;

            return Objects.equals(httpVersion, version);
        });
    }

    /**
     * Checks header of request.
     *
     * @param name  is the name of header request is supposed to have
     * @param value is the value of header request is supposed to have
     * @return criteria that checks HttpTraffic entry
     */
    @Description("request has header '{name}' with value '{value}'")
    public static Criteria<HttpTraffic> recordedRequestHeader(@DescriptionFragment("name") String name,
                                                              @DescriptionFragment("value") String value) {
        checkArgument(isNotBlank(name), "Request header name should be defined");
        checkArgument(isNotBlank(value), "Request header value should be defined");

        return condition(entry -> {
            Map<String, Object> requestHeaders = entry.getRequest().getRequest().getHeaders().toJson();

            return ofNullable(requestHeaders)
                    .map(reqHeaders ->
                            requestHeaders.entrySet()
                                    .stream()
                                    .filter(header -> Objects.equals(header.getKey(), name))
                                    .map(header -> (String) header.getValue())
                                    .anyMatch(value::equals))
                    .orElse(false);
        });
    }

    /**
     * Checks header of request.
     *
     * @param name            is the name of header request is supposed to have
     * @param valueExpression is the substring the header value is supposed to have or
     *                        the RegExp the header value is predicted to match
     * @return criteria that checks HttpTraffic entry
     */
    @Description("request has header '{name}' with value contains/matches RegExp pattern '{valueExpression}'")
    public static Criteria<HttpTraffic> recordedRequestHeaderMatches(@DescriptionFragment("name") String name,
                                                                     @DescriptionFragment("valueExpression") String valueExpression) {
        checkArgument(isNotBlank(name), "Request header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Request header value substring/RegExp should be defined");

        return condition(entry -> {
            Map<String, Object> requestHeaders = entry.getRequest().getRequest().getHeaders().toJson();

            return ofNullable(requestHeaders)
                    .map(reqHeaders ->
                            requestHeaders.entrySet()
                                    .stream()
                                    .filter(header -> Objects.equals(header.getKey(), name))
                                    .map(header -> (String) header.getValue())
                                    .anyMatch(checkByStringContainingOrRegExp(valueExpression)))
                    .orElse(false);
        });
    }

    /**
     * Checks header of response.
     *
     * @param name  is the name of header response is supposed to have
     * @param value is the value of header response is supposed to have
     * @return criteria that checks HttpTraffic entry
     */
    @Description("response has header '{name}' with value '{value}'")
    public static Criteria<HttpTraffic> recordedResponseHeader(@DescriptionFragment("name") String name,
                                                               @DescriptionFragment("value") String value) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(value), "Response header value should be defined");

        return condition(entry -> {
            Map<String, Object> responseHeaders = entry.getResponse().getResponse().getHeaders().toJson();

            return ofNullable(responseHeaders)
                    .map(respHeaders ->
                            responseHeaders.entrySet()
                                    .stream()
                                    .filter(header -> Objects.equals(header.getKey(), name))
                                    .map(header -> (String) header.getValue())
                                    .anyMatch(value::equals))
                    .orElse(false);
        });
    }

    /**
     * Checks header of response.
     *
     * @param name            is the name of header response is supposed to have
     * @param valueExpression is the substring the header value is supposed to have or
     *                        the RegExp the header value is predicted to match
     * @return criteria that checks HttpTraffic entry
     */
    @Description("response has header '{name}' with value contains/matches RegExp pattern '{valueExpression}'")
    public static Criteria<HttpTraffic> recordedResponseHeaderMatches(@DescriptionFragment("name") String name,
                                                                      @DescriptionFragment("valueExpression") String valueExpression) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Response header value substring/RegExp should be defined");

        return condition(entry -> {
            Map<String, Object> responseHeaders = entry.getResponse().getResponse().getHeaders().toJson();

            return ofNullable(responseHeaders)
                    .map(respHeaders ->
                            responseHeaders.entrySet()
                                    .stream()
                                    .filter(header -> Objects.equals(header.getKey(), name))
                                    .map(header -> (String) header.getValue())
                                    .anyMatch(checkByStringContainingOrRegExp(valueExpression)))
                    .orElse(false);
        });
    }

    /**
     * Checks body of request.
     *
     * @param body is the body request is supposed to have
     * @return criteria that checks HttpTraffic entry
     */
    @Description("request has body '{body}'")
    public static Criteria<HttpTraffic> recordedRequestBody(@DescriptionFragment("body") String body) {
        checkArgument(isNotBlank(body), "Request body should be defined");

        return condition(entry -> {
            String postData = entry.getRequest().getRequest().getPostData().orElse(null);

            return ofNullable(postData)
                    .map(data -> Objects.equals(data, body))
                    .orElse(false);
        });
    }

    /**
     * Checks body of response.
     *
     * @param body is the body response is supposed to have
     * @return criteria that checks HttpTraffic entry
     */
    @Description("response has body '{body}'")
    public static Criteria<HttpTraffic> recordedResponseBody(@DescriptionFragment("body") String body) {
        checkArgument(isNotBlank(body), "Response body should be defined");

        return condition(entry -> {
            Network.GetResponseBodyResponse responseBodyResponse = entry.getBody();

            return ofNullable(responseBodyResponse)
                    .map(content -> Objects.equals(content.getBody(), body))
                    .orElse(false);
        });
    }

    /**
     * Checks body of request.
     *
     * @param bodyExpression is the substring the request body is supposed to have or
     *                       the RegExp the request body is predicted to match
     * @return criteria that checks HttpTraffic entry
     */
    @Description("request body contains substring/matches RegExp '{bodyExpression}'")
    public static Criteria<HttpTraffic> recordedRequestBodyMatches(@DescriptionFragment("bodyExpression") String bodyExpression) {
        checkArgument(isNotBlank(bodyExpression), "Request body substring/RegExp should be defined");

        return condition(entry ->
                entry.getRequest().getRequest().getPostData()
                        .map(data -> checkByStringContainingOrRegExp(bodyExpression).test(data))
                        .orElse(false));
    }

    /**
     * Checks body of response.
     *
     * @param bodyExpression is the substring the response body is supposed to have or
     *                       the RegExp the response body is predicted to match
     * @return criteria that checks HttpTraffic entry
     */
    @Description("response body contains substring/matches RegExp '{bodyExpression}'")
    public static Criteria<HttpTraffic> recordedResponseBodyMatches(@DescriptionFragment("bodyExpression") String bodyExpression) {
        checkArgument(isNotBlank(bodyExpression), "Response body substring/RegExp should be defined");

        return condition(entry -> {
            Network.GetResponseBodyResponse responseContent = entry.getBody();

            return ofNullable(responseContent)
                    .map(content -> checkByStringContainingOrRegExp(bodyExpression).test(content.getBody()))
                    .orElse(false);
        });
    }

    /**
     * Checks status code of response.
     *
     * @param status is the status code response is supposed to have
     * @return criteria that checks HttpTraffic entry
     */
    @Description("status code of response is {code}")
    public static Criteria<HttpTraffic> recordedResponseStatusCode(@DescriptionFragment("code") int status) {
        return condition(entry -> {
            var statusCode = entry.getResponse().getResponse().getStatus();
            return statusCode == status;
        });
    }
}
