package ru.tinkoff.qa.neptune.selenium.functions.browser.proxy;

import com.browserup.harreader.model.*;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.net.http.HttpClient;
import java.util.Date;
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
    @Description("request url equals to '{url}'")
    public static Criteria<HarEntry> recordedRequestUrl(@DescriptionFragment("url") String url) {
        checkArgument(isNotBlank(url), "URL should be defined");

        return condition(entry -> {
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
    @Description("request url contains '{urlExpression}' or fits regExp pattern '{urlExpression}'")
    public static Criteria<HarEntry> recordedRequestUrlMatches(@DescriptionFragment("urlExpression") String urlExpression) {
        checkArgument(isNotBlank(urlExpression), "URL Substring/RegExp should be defined");

        return condition(entry -> {
            String requestUrl = entry.getRequest().getUrl();

            return ofNullable(requestUrl)
                    .map(s -> {
                        if (s.contains(urlExpression)) {
                            return true;
                        }

                        try {
                            var pattern = compile(urlExpression);
                            var matcher = pattern.matcher(s);
                            return matcher.matches();
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
    @Description("request with method '{method}'")
    public static Criteria<HarEntry> recordedRequestMethod(@DescriptionFragment("method") HttpMethod method) {
        checkArgument(nonNull(method), "Method should be defined");

        return condition(entry -> {
            HttpMethod requestMethod = entry.getRequest().getMethod();

            return Objects.equals(requestMethod, method);
        });
    }

    /**
     * Checks HTTP version of request.
     *
     * @param version is the HTTP version request is supposed to have
     * @return criteria that checks HAR entry
     */
    @Description("request with HTTP version '{version}'")
    public static Criteria<HarEntry> recordedRequestHttpVersion(@DescriptionFragment("version") HttpClient.Version version) {
        checkArgument(nonNull(version), "Http version should be defined");

        return condition(entry -> {
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
    @Description("response with HTTP version '{version}'")
    public static Criteria<HarEntry> recordedResponseHttpVersion(@DescriptionFragment("version") HttpClient.Version version) {
        checkArgument(nonNull(version), "Http version should be defined");

        return condition(entry -> {
            HttpClient.Version httpVersion = entry.getResponse().getHttpVersion().equals("HTTP/1.1") ? HTTP_1_1 : HTTP_2;

            return Objects.equals(httpVersion, version);
        });
    }

    /**
     * Checks query parameter of request.
     *
     * @param queryParam is the name of http query parameter
     * @param value      is required value of http query parameter
     * @return criteria that checks HAR entry
     */
    @Description("request has query parameter '{param}' with value '{value}'")
    public static Criteria<HarEntry> recordedRequestQueryParam(@DescriptionFragment("param") String queryParam,
                                                               @DescriptionFragment("value") String value) {
        checkArgument(isNotBlank(queryParam), "Query parameter should not be blank or null string");
        checkArgument(isNotBlank(value), "Value of query parameter should not be blank or null string");

        return condition(entry -> {
            List<HarQueryParam> queryParams = entry.getRequest().getQueryString();

            return ofNullable(queryParams)
                    .map(parameters ->
                            parameters.stream()
                                    .anyMatch(param ->
                                            param.getName().equals(queryParam) && param.getValue().equals(value)))
                    .orElse(false);
        });
    }

    /**
     * Checks query parameters of request.
     *
     * @param queryParam      is the name of http query parameter
     * @param valueExpression is the substring the query parameter value is supposed to have or
     *                        the RegExp the value is predicted to match
     * @return criteria that checks HAR entry
     */
    @Description("request has query parameter '{param}' with value contains/matches RegExp pattern '{valueExpression}'")
    public static Criteria<HarEntry> recordedRequestQueryParamMatches(@DescriptionFragment("param") String queryParam,
                                                                      @DescriptionFragment("valueExpression") String valueExpression) {
        checkArgument(isNotBlank(queryParam), "Query parameter should not be blank or null string");
        checkArgument(isNotBlank(valueExpression), "Query parameter value substring/RegExp should be defined");

        return condition(entry -> {
            List<HarQueryParam> queryParams = entry.getRequest().getQueryString();

            return ofNullable(queryParams)
                    .map(params ->
                            params.stream().anyMatch(param -> {
                                if (param.getValue().contains(valueExpression)) {
                                    return true;
                                }

                                try {
                                    var pattern = compile(valueExpression);
                                    var matcher = pattern.matcher(param.getValue());

                                    return matcher.matches();
                                } catch (Throwable thrown) {
                                    thrown.printStackTrace();
                                    return false;
                                }
                            }))
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
    @Description("request has header '{name}' with value '{value}'")
    public static Criteria<HarEntry> recordedRequestHeader(@DescriptionFragment("name") String name, @DescriptionFragment("value") String value) {
        checkArgument(isNotBlank(name), "Request header name should be defined");
        checkArgument(isNotBlank(value), "Request header value should be defined");

        return condition(entry -> {
            List<HarHeader> requestHeaders = entry.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders ->
                            reqHeaders.stream()
                                    .anyMatch(header ->
                                            header.getName().equals(name) && header.getValue().equals(value)))
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
    @Description("request has header '{name}' with value contains/matches RegExp pattern '{valueExpression}'")
    public static Criteria<HarEntry> recordedRequestHeaderMatches(@DescriptionFragment("name") String name,
                                                                  @DescriptionFragment("valueExpression") String valueExpression) {
        checkArgument(isNotBlank(name), "Request header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Request header value substring/RegExp should be defined");

        return condition(entry -> {
            List<HarHeader> requestHeaders = entry.getRequest().getHeaders();

            return ofNullable(requestHeaders)
                    .map(reqHeaders ->
                            reqHeaders.stream().anyMatch(header -> {
                                if (header.getValue().contains(valueExpression)) {
                                    return true;
                                }

                                try {
                                    var pattern = compile(valueExpression);
                                    var matcher = pattern.matcher(header.getValue());

                                    return matcher.matches();
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
    @Description("response has header '{name}' with value '{value}'")
    public static Criteria<HarEntry> recordedResponseHeader(String name, String value) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(value), "Response header value should be defined");

        return condition(format("response has header '%s' with value '%s'", name, value), entry -> {
            List<HarHeader> responseHeaders = entry.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders ->
                            respHeaders.stream()
                                    .anyMatch(header ->
                                            header.getName().equals(name) && header.getValue().equals(value)))
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
    @Description("response has header '{name}' with value contains/matches RegExp pattern '{valueExpression}'")
    public static Criteria<HarEntry> recordedResponseHeaderMatches(@DescriptionFragment("name") String name,
                                                                   @DescriptionFragment("valueExpression") String valueExpression) {
        checkArgument(isNotBlank(name), "Response header name should be defined");
        checkArgument(isNotBlank(valueExpression), "Response header value substring/RegExp should be defined");

        return condition(entry -> {
            List<HarHeader> responseHeaders = entry.getResponse().getHeaders();

            return ofNullable(responseHeaders)
                    .map(respHeaders ->
                            respHeaders.stream().anyMatch(header -> {
                                if (header.getValue().contains(valueExpression)) {
                                    return true;
                                }

                                try {
                                    var pattern = compile(valueExpression);
                                    var matcher = pattern.matcher(header.getValue());

                                    return matcher.matches();
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
    @Description("request has body '{body}'")
    public static Criteria<HarEntry> recordedRequestBody(@DescriptionFragment("body") String body) {
        checkArgument(isNotBlank(body), "Request body should be defined");

        return condition(entry -> {
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
    @Description("response has body '{body}'")
    public static Criteria<HarEntry> recordedResponseBody(@DescriptionFragment("body") String body) {
        checkArgument(isNotBlank(body), "Response body should be defined");

        return condition(entry -> {
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
    @Description("request body contains substring/matches RegExp '{bodyExpression}'")
    public static Criteria<HarEntry> recordedRequestBodyMatches(@DescriptionFragment("bodyExpression") String bodyExpression) {
        checkArgument(isNotBlank(bodyExpression), "Request body substring/RegExp should be defined");

        return condition(entry -> {
            HarPostData postData = entry.getRequest().getPostData();

            return ofNullable(postData)
                    .map(data -> {
                        if (data.getText().contains(bodyExpression)) {
                            return true;
                        }

                        try {
                            var pattern = compile(bodyExpression);
                            var matcher = pattern.matcher(data.getText());

                            return matcher.matches();
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
    @Description("response body contains substring/matches RegExp '{bodyExpression}'")
    public static Criteria<HarEntry> recordedResponseBodyMatches(@DescriptionFragment("bodyExpression") String bodyExpression) {
        checkArgument(isNotBlank(bodyExpression), "Response body substring/RegExp should be defined");

        return condition(entry -> {
            HarContent responseContent = entry.getResponse().getContent();

            return ofNullable(responseContent)
                    .map(content -> {
                        if (content.getText().contains(bodyExpression)) {
                            return true;
                        }

                        try {
                            var pattern = compile(bodyExpression);
                            var matcher = pattern.matcher(content.getText());

                            return matcher.matches();
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
    @Description("status code of response is {code}")
    public static Criteria<HarEntry> recordedResponseStatusCode(@DescriptionFragment("code") int status) {
        return condition(format("response status code is '%s'", status), entry -> {
            var statusCode = entry.getResponse().getStatus();
            return statusCode == status;
        });
    }

    /**
     * Checks the datetime the request was sent
     *
     * @param date is the datetime after which the request was to be sent
     * @return criteria that checks HAR entry
     */
    @Description("request was sent after {date}")
    public static Criteria<HarEntry> recordedStartedDateTimeAfter(@DescriptionFragment("date") Date date) {
        checkArgument(nonNull(date), "Date should be defined");

        return condition(entry -> {
            Date startedDateTime = entry.getStartedDateTime();

            return startedDateTime.after(date);
        });
    }

    /**
     * Checks the datetime the request was sent
     *
     * @param date is the datetime before which the request was to be sent
     * @return criteria that checks HAR entry
     */
    @Description("request was sent before {date}")
    public static Criteria<HarEntry> recordedStartedDateTimeBefore(@DescriptionFragment("date") Date date) {
        checkArgument(nonNull(date), "Date should be defined");

        return condition(entry -> {
            Date startedDateTime = entry.getStartedDateTime();

            return startedDateTime.before(date);
        });
    }

    /**
     * Checks the duration of the request
     *
     * @param millis is the number of milliseconds longer than which the duration of the request is supposed to be
     * @return criteria that checks HAR entry
     */
    @Description("request duration is longer than {millis}")
    public static Criteria<HarEntry> recordedDurationLongerThan(@DescriptionFragment("millis") Long millis) {
        checkArgument(nonNull(millis), "Duration must be defined");
        checkArgument(millis > 0, "Duration should be positive");

        return condition(entry -> {
            long time = entry.getTime();

            return time > millis;
        });
    }

    /**
     * Checks the duration of the request
     *
     * @param millis is the number of milliseconds shorter than which the duration of the request is supposed to be
     * @return criteria that checks HAR entry
     */
    @Description("request duration is shorter than {millis}")
    public static Criteria<HarEntry> recordedDurationShorterThan(@DescriptionFragment("millis") Long millis) {
        checkArgument(nonNull(millis), "Duration must be defined");
        checkArgument(millis > 0, "Duration should be positive");

        return condition(entry -> {
            long time = entry.getTime();

            return time < millis;
        });
    }
}
