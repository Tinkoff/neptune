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

    public static <T extends HarEntry> Criteria<T> requestUrl(String url) {
        checkArgument(isNotBlank(url), "URL should be defined");

        return condition(format("request url equals to '%s'", url), t -> {
            String requestUrl = t.getRequest().getUrl();

            return Objects.equals(requestUrl, url);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestUrlMatches(String urlExpression) {
        checkArgument(isNotBlank(urlExpression), "URL Substring/RegExp should be defined");

        return condition(format("request url contains '%s' or fits regExp pattern '%s'", urlExpression, urlExpression), t -> {
            String requestUrl = t.getRequest().getUrl();

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

    public static <T extends HarEntry> Criteria<T> requestMethod(String method) {
        checkArgument(isNotBlank(method), "Method should be defined");

        return condition(format("request with method '%s'", method), t -> {
            String requestMethod = t.getRequest().getMethod();

            return Objects.equals(requestMethod, method);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestHttpVersion(HttpClient.Version version) {
        checkArgument(nonNull(version), "Http version should be defined");

        return condition(format("request with HTTP version '%s'", version), t -> {
            HttpClient.Version httpVersion = t.getRequest().getHttpVersion().equals("HTTP/1.1") ? HTTP_1_1 : HTTP_2;

            return Objects.equals(httpVersion, version);
        });
    }

    public static <T extends HarEntry> Criteria<T> responseHttpVersion(HttpClient.Version version) {
        checkArgument(nonNull(version), "Http version should be defined");

        return condition(format("response with HTTP version '%s'", version), t -> {
            HttpClient.Version httpVersion = t.getResponse().getHttpVersion().equals("HTTP/1.1") ? HTTP_1_1 : HTTP_2;

            return Objects.equals(httpVersion, version);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestQueryParams(List<HarNameValuePair> params) {
        checkArgument(nonNull(params), "Query params list should be defined");
        checkArgument(params.size() > 0, "Query params list can't be empty");

        return condition(format("request with query parameters '%s'", params), t -> {
            List<HarNameValuePair> queryParams = t.getRequest().getQueryString();

            return ofNullable(queryParams)
                    .map(qParams -> qParams.size() == params.size() && qParams.containsAll(params))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestContainsQueryParams(List<HarNameValuePair> params) {
        checkArgument(nonNull(params), "Query params list should be defined");
        checkArgument(params.size() > 0, "Query params list can't be empty");

        return condition(format("request containing query parameters '%s'", params), t -> {
            List<HarNameValuePair> queryParams = t.getRequest().getQueryString();

            return ofNullable(queryParams)
                    .map(qParams -> qParams.containsAll(params))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestHeaders(List<HarNameValuePair> headers) {
        checkArgument(nonNull(headers), "Request headers list should be defined");
        checkArgument(headers.size() > 0, "Request headers list can't be empty");

        return condition(format("request with headers '%s'", headers), t -> {
            List<HarNameValuePair> requestHeaders = t.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders -> reqHeaders.size() == headers.size() && reqHeaders.containsAll(headers))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestHeadersContains(List<HarNameValuePair> headers) {
        checkArgument(nonNull(headers), "Request headers list should be defined");
        checkArgument(headers.size() > 0, "Request headers list can't be empty");

        return condition(format("request with headers '%s'", headers), t -> {
            List<HarNameValuePair> requestHeaders = t.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders -> reqHeaders.containsAll(headers))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> responseHeaders(List<HarNameValuePair> headers) {
        checkArgument(nonNull(headers), "Response headers list should be defined");
        checkArgument(headers.size() > 0, "Response headers list can't be empty");

        return condition(format("response with headers '%s'", headers), t -> {
            List<HarNameValuePair> responseHeaders = t.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders -> respHeaders.size() == headers.size() && respHeaders.containsAll(headers))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> responseHeadersContains(List<HarNameValuePair> headers) {
        checkArgument(nonNull(headers), "Response headers list should be defined");
        checkArgument(headers.size() > 0, "Response headers list can't be empty");

        return condition(format("response with headers '%s'", headers), t -> {
            List<HarNameValuePair> responseHeaders = t.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders -> respHeaders.containsAll(headers))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestHeader(String name, String value) {
        checkArgument(isNotBlank(name), "Request header name should be defined");
        checkArgument(isNotBlank(value), "Request header value should be defined");

        return condition(format("request has header '%s' with value '%s'", name, value), t -> {
            List<HarNameValuePair> requestHeaders = t.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders -> reqHeaders.contains(new HarNameValuePair(name, value)))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestHeaderMatches(String name, String valueExpression) {
        checkArgument(isNotBlank(name), "Request header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Request header value substring/RegExp should be defined");

        return condition(format("request has header '%s' with value contains/matches RegExp pattern '%s'", name, valueExpression), t -> {
            List<HarNameValuePair> requestHeaders = t.getRequest().getHeaders();

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

    public static <T extends HarEntry> Criteria<T> responseHeader(String name, String value) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(value), "Response header value should be defined");

        return condition(format("response has header '%s' with value '%s'", name, value), t -> {
            List<HarNameValuePair> responseHeaders = t.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders -> respHeaders.contains(new HarNameValuePair(name, value)))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> responseHeaderMatches(String name, String valueExpression) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Response header value substring/RegExp should be defined");

        return condition(format("response has header '%s' with value contains/matches RegExp pattern '%s'", name, valueExpression), t -> {
            List<HarNameValuePair> responseHeaders = t.getResponse().getHeaders();

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

    public static <T extends HarEntry> Criteria<T> requestBody(String body) {
        checkArgument(isNotBlank(body), "Request body should be defined");

        return condition(format("request has body '%s'", body), t -> {
            HarPostData postData = t.getRequest().getPostData();

            return ofNullable(postData)
                    .map(data -> Objects.equals(data.getText(), body))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> responseBody(String body) {
        checkArgument(isNotBlank(body), "Response body should be defined");

        return condition(format("response has body '%s'", body), t -> {
            HarContent responseContent = t.getResponse().getContent();

            return ofNullable(responseContent)
                    .map(content -> Objects.equals(content.getText(), body))
                    .orElse(false);
        });
    }

    public static <T extends HarEntry> Criteria<T> requestBodyMatches(String bodyExpression) {
        checkArgument(isNotBlank(bodyExpression), "Request body substring/RegExp should be defined");

        return condition(format("request body contains substring/matches RegExp '%s'", bodyExpression), t -> {
            HarPostData postData = t.getRequest().getPostData();

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

    public static <T extends HarEntry> Criteria<T> responseBodyMatches(String bodyExpression) {
        checkArgument(isNotBlank(bodyExpression), "Response body substring/RegExp should be defined");

        return condition(format("response body contains substring/matches RegExp '%s'", bodyExpression), t -> {
            HarContent responseContent = t.getResponse().getContent();

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

    public static <T extends HarEntry> Criteria<T> responseStatusCode(Integer status) {
        checkArgument(nonNull(status), "Status code should be defined");

        return condition(format("response status code is '%s'", status), t -> {
            var clazz = t.getClass();

            Integer statusCode = null;

            if (HarEntry.class.isAssignableFrom(clazz)) {
                statusCode = t.getResponse().getStatus();
            }

            return Objects.equals(statusCode, status);
        });
    }
}
