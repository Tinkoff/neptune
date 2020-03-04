package ru.tinkoff.qa.neptune.selenium.functions.browser.proxy;

import net.lightbody.bmp.core.har.HarContent;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.core.har.HarPostData;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class BrowserProxyCriteria {

    private BrowserProxyCriteria() {
        super();
    }

    /**
     * Checks the url of request.
     *
     * @param url is the url request is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestUrl(String url) {
        checkArgument(isNotBlank(url), "URL should be defined");

        return condition(format("request url equals to '%s'", url), entry -> {
            String requestUrl = entry.getRequest().getUrl();

            return Objects.equals(requestUrl, url);
        });
    }

    /**
     * Checks the url of request.
     *
     * @param urlExpression is the substring the url is supposed to have or
     *                      the RegExp the url is predicted to match
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestUrlMatches(String urlExpression) {
        checkArgument(isNotBlank(urlExpression), "URL Substring/RegExp should be defined");

        return condition(format("request url contains '%s' or fits regExp pattern '%s'", urlExpression, urlExpression), entry -> {
            String requestUrl = entry.getRequest().getUrl();

            return ofNullable(requestUrl)
                    .map(s -> {
                        if (s.contains(urlExpression)) {
                            return true;
                        }

                        try {
                            var pattern = compile(urlExpression);
                            var matcher = pattern.matcher(s);

                            return matcher.matches() || matcher.find();
                        } catch (Throwable thrown) {
                            thrown.printStackTrace();
                            return false;
                        }
                    })
                    .orElse(false);
        });
    }

    /**
     * Checks HTTP method of request.
     *
     * @param method is the name of HTTP method request is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestMethod(String method) {
        checkArgument(isNotBlank(method), "Method should be defined");

        return condition(format("request with method '%s'", method), entry -> {
            String requestMethod = entry.getRequest().getMethod();

            return Objects.equals(requestMethod, method);
        });
    }

    /**
     * Checks HTTP version of request.
     *
     * @param version is the HTTP version request is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestHttpVersion(HttpClient.Version version) {
        checkArgument(nonNull(version), "Http version should be defined");

        return condition(format("request with HTTP version '%s'", version), entry -> {
            HttpClient.Version httpVersion = entry.getRequest().getHttpVersion().equals("HTTP/1.1") ? HTTP_1_1 : HTTP_2;

            return Objects.equals(httpVersion, version);
        });
    }

    /**
     * Checks HTTP version of response.
     *
     * @param version is the HTTP version response is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> responseHttpVersion(HttpClient.Version version) {
        checkArgument(nonNull(version), "Http version should be defined");

        return condition(format("response with HTTP version '%s'", version), entry -> {
            HttpClient.Version httpVersion = entry.getResponse().getHttpVersion().equals("HTTP/1.1") ? HTTP_1_1 : HTTP_2;

            return Objects.equals(httpVersion, version);
        });
    }

    /**
     * Checks query parameters of request.
     *
     * @param params is the {@link HarNameValuePair} list of query parameters request is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestQueryParams(List<HarNameValuePair> params) {
        checkArgument(nonNull(params), "Query params list should be defined");
        checkArgument(params.size() > 0, "Query params list can'entry be empty");

        return condition(format("request with query parameters '%s'", params), entry -> {
            List<HarNameValuePair> queryParams = entry.getRequest().getQueryString();

            return ofNullable(queryParams)
                    .map(qParams -> qParams.size() == params.size() && qParams.containsAll(params))
                    .orElse(false);
        });
    }

    /**
     * Checks query parameters of request.
     *
     * @param params is the {@link HarNameValuePair} list of query parameters request is supposed to contain
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestContainsQueryParams(List<HarNameValuePair> params) {
        checkArgument(nonNull(params), "Query params list should be defined");
        checkArgument(params.size() > 0, "Query params list can'entry be empty");

        return condition(format("request containing query parameters '%s'", params), entry -> {
            List<HarNameValuePair> queryParams = entry.getRequest().getQueryString();

            return ofNullable(queryParams)
                    .map(qParams -> qParams.containsAll(params))
                    .orElse(false);
        });
    }

    /**
     * Checks headers of request.
     *
     * @param headers is the {@link HarNameValuePair} list of headers request is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestHeaders(List<HarNameValuePair> headers) {
        checkArgument(nonNull(headers), "Request headers list should be defined");
        checkArgument(headers.size() > 0, "Request headers list can'entry be empty");

        return condition(format("request with headers '%s'", headers), entry -> {
            List<HarNameValuePair> requestHeaders = entry.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders -> reqHeaders.size() == headers.size() && reqHeaders.containsAll(headers))
                    .orElse(false);
        });
    }

    /**
     * Checks headers of request.
     *
     * @param headers headers is the {@link HarNameValuePair} list of headers request is supposed to contain
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestHeadersContains(List<HarNameValuePair> headers) {
        checkArgument(nonNull(headers), "Request headers list should be defined");
        checkArgument(headers.size() > 0, "Request headers list can'entry be empty");

        return condition(format("request with headers '%s'", headers), entry -> {
            List<HarNameValuePair> requestHeaders = entry.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders -> reqHeaders.containsAll(headers))
                    .orElse(false);
        });
    }

    /**
     * Checks headers of response.
     *
     * @param headers headers is the {@link HarNameValuePair} list of headers response is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> responseHeaders(List<HarNameValuePair> headers) {
        checkArgument(nonNull(headers), "Response headers list should be defined");
        checkArgument(headers.size() > 0, "Response headers list can'entry be empty");

        return condition(format("response with headers '%s'", headers), entry -> {
            List<HarNameValuePair> responseHeaders = entry.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders -> respHeaders.size() == headers.size() && respHeaders.containsAll(headers))
                    .orElse(false);
        });
    }

    /**
     * Checks headers of response.
     *
     * @param headers headers is the {@link HarNameValuePair} list of headers response is supposed to contain
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> responseHeadersContains(List<HarNameValuePair> headers) {
        checkArgument(nonNull(headers), "Response headers list should be defined");
        checkArgument(headers.size() > 0, "Response headers list can'entry be empty");

        return condition(format("response with headers '%s'", headers), entry -> {
            List<HarNameValuePair> responseHeaders = entry.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders -> respHeaders.containsAll(headers))
                    .orElse(false);
        });
    }

    /**
     * Checks header of request.
     *
     * @param name  is the name of header request is supposed to have
     * @param value is the value of header request is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestHeader(String name, String value) {
        checkArgument(isNotBlank(name), "Request header name should be defined");
        checkArgument(isNotBlank(value), "Request header value should be defined");

        return condition(format("request has header '%s' with value '%s'", name, value), entry -> {
            List<HarNameValuePair> requestHeaders = entry.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders -> reqHeaders.contains(new HarNameValuePair(name, value)))
                    .orElse(false);
        });
    }

    /**
     * Checks header of request.
     *
     * @param name            is the name of header request is supposed to have
     * @param valueExpression is the substring the header value is supposed to have or
     *                        the RegExp the header value is predicted to match
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestHeaderMatches(String name, String valueExpression) {
        checkArgument(isNotBlank(name), "Request header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Request header value substring/RegExp should be defined");

        return condition(format("request has header '%s' with value contains/matches RegExp pattern '%s'", name, valueExpression), entry -> {
            List<HarNameValuePair> requestHeaders = entry.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders ->
                            reqHeaders.stream().anyMatch(header -> {
                                if (header.getValue().contains(valueExpression)) {
                                    return true;
                                }

                                try {
                                    var pattern = compile(valueExpression);
                                    var matcher = pattern.matcher(header.getValue());

                                    return matcher.matches() || matcher.find();
                                } catch (Throwable thrown) {
                                    thrown.printStackTrace();
                                    return false;
                                }
                            }))
                    .orElse(false);
        });
    }

    /**
     * Checks header of response.
     *
     * @param name  is the name of header response is supposed to have
     * @param value is the value of header response is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> responseHeader(String name, String value) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(value), "Response header value should be defined");

        return condition(format("response has header '%s' with value '%s'", name, value), entry -> {
            List<HarNameValuePair> responseHeaders = entry.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders -> respHeaders.contains(new HarNameValuePair(name, value)))
                    .orElse(false);
        });
    }

    /**
     * Checks header of response.
     *
     * @param name            is the name of header response is supposed to have
     * @param valueExpression is the substring the header value is supposed to have or
     *                        the RegExp the header value is predicted to match
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> responseHeaderMatches(String name, String valueExpression) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Response header value substring/RegExp should be defined");

        return condition(format("response has header '%s' with value contains/matches RegExp pattern '%s'", name, valueExpression), entry -> {
            List<HarNameValuePair> responseHeaders = entry.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders ->
                            respHeaders.stream().anyMatch(header -> {
                                if (header.getValue().contains(valueExpression)) {
                                    return true;
                                }

                                try {
                                    var pattern = compile(valueExpression);
                                    var matcher = pattern.matcher(header.getValue());

                                    return matcher.matches() || matcher.find();
                                } catch (Throwable thrown) {
                                    thrown.printStackTrace();
                                    return false;
                                }
                            }))
                    .orElse(false);
        });
    }

    /**
     * Checks body of request.
     *
     * @param body is the body request is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestBody(String body) {
        checkArgument(isNotBlank(body), "Request body should be defined");

        return condition(format("request has body '%s'", body), entry -> {
            HarPostData postData = entry.getRequest().getPostData();

            return ofNullable(postData)
                    .map(data -> Objects.equals(data.getText(), body))
                    .orElse(false);
        });
    }

    /**
     * Checks body of response.
     *
     * @param body is the body response is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> responseBody(String body) {
        checkArgument(isNotBlank(body), "Response body should be defined");

        return condition(format("response has body '%s'", body), entry -> {
            HarContent responseContent = entry.getResponse().getContent();

            return ofNullable(responseContent)
                    .map(content -> Objects.equals(content.getText(), body))
                    .orElse(false);
        });
    }

    /**
     * Checks body of request.
     *
     * @param bodyExpression is the substring the request body is supposed to have or
     *                       the RegExp the request body is predicted to match
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> requestBodyMatches(String bodyExpression) {
        checkArgument(isNotBlank(bodyExpression), "Request body substring/RegExp should be defined");

        return condition(format("request body contains substring/matches RegExp '%s'", bodyExpression), entry -> {
            HarPostData postData = entry.getRequest().getPostData();

            return ofNullable(postData)
                    .map(data -> {
                        if (data.getText().contains(bodyExpression)) {
                            return true;
                        }

                        try {
                            var pattern = compile(bodyExpression);
                            var matcher = pattern.matcher(data.getText());

                            return matcher.matches() || matcher.find();
                        } catch (Throwable thrown) {
                            thrown.printStackTrace();
                            return false;
                        }
                    })
                    .orElse(false);
        });
    }

    /**
     * Checks body of response.
     *
     * @param bodyExpression is the substring the response body is supposed to have or
     *                       the RegExp the response body is predicted to match
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> responseBodyMatches(String bodyExpression) {
        checkArgument(isNotBlank(bodyExpression), "Response body substring/RegExp should be defined");

        return condition(format("response body contains substring/matches RegExp '%s'", bodyExpression), entry -> {
            HarContent responseContent = entry.getResponse().getContent();

            return ofNullable(responseContent)
                    .map(content -> {
                        if (content.getText().contains(bodyExpression)) {
                            return true;
                        }

                        try {
                            var pattern = compile(bodyExpression);
                            var matcher = pattern.matcher(content.getText());

                            return matcher.matches() || matcher.find();
                        } catch (Throwable thrown) {
                            thrown.printStackTrace();
                            return false;
                        }
                    })
                    .orElse(false);
        });
    }

    /**
     * Checks status code of response.
     *
     * @param status is the status code response is supposed to have
     * @return criteria that checks HAR entry
     */
    public static Criteria<HarEntry> responseStatusCode(Integer status) {
        checkArgument(nonNull(status), "Status code should be defined");

        return condition(format("response status code is '%s'", status), entry -> {
            Integer statusCode = entry.getResponse().getStatus();

            return Objects.equals(statusCode, status);
        });
    }
}
