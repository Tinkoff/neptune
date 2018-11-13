package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

/**
 * This class is designed to build chains of functions that get applicable cookies from a cookie cache for the specified
 * uri in the request header. from http client. As a result it should return a map from state management headers, with
 * field names "Cookie" or "Cookie2" to a list of cookies containing state information. If current http client doesn't
 * have a {@link java.net.CookieHandler} then it returns {@code null}
 */
public final class HttpGetCachedCookiesSupplier  extends GetStepSupplier<HttpSteps, Map<String, List<String>>, HttpGetCachedCookiesSupplier> {

    private final Map<String, List<String>> EMPTY_REQUEST_HEADERS = ofEntries(entry("", List.of("")));

    private final URI cookiesFrom;
    private Map<String, List<String>> requestHeaders = EMPTY_REQUEST_HEADERS;

    private HttpGetCachedCookiesSupplier(String cookiesFrom) {
        try {
            this.cookiesFrom = new URI(cookiesFrom);
            set(toGet(format("Cached cookies from %s", cookiesFrom), httpSteps -> httpSteps.getCurrentClient().cookieHandler().map(cookieHandler -> {
                try {
                    return cookieHandler.get(this.cookiesFrom, requestHeaders);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }).orElse(null)));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Builds a function to get applicable cookies stored by current http client.
     *
     * @param uri is URI to get cookie from
     * @return an instance of {@link HttpGetCachedCookiesSupplier}
     */
    public static HttpGetCachedCookiesSupplier cookiesFrom(String uri) {
        return new HttpGetCachedCookiesSupplier(uri);
    }

    /**
     * Sets request headers to get applicable cookies stored by current http client. The using of/result of the method
     * invocation depends on currently used {@link java.net.CookieHandler} instance.
     *
     * @param requestHeaders a Map from request header
     *                       field names to lists of field values representing
     *                       the current request headers
     * @return self-reference
     */
    public HttpGetCachedCookiesSupplier withHeaders(Map<String, List<String>> requestHeaders) {
        checkArgument(requestHeaders != null, "Map of request headers should not be a null value");
        checkArgument(requestHeaders.size() > 0, "Map of request headers should not be empty");
        this.requestHeaders = requestHeaders;
        return this;
    }
}
