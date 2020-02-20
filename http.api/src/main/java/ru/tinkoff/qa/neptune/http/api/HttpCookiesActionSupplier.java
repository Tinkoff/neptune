package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.StepAction.action;
import static ru.tinkoff.qa.neptune.http.api.GetCookieManager.getCookieManager;

/**
 * This is the basic class for actions associated with http cookies. The adding/clearing generally.
 *
 * @param <T> is a type of a {@link HttpCookiesActionSupplier} subclass required by specification
 */
@Deprecated(since = "0.11.4-ALPHA", forRemoval = true)
public abstract class HttpCookiesActionSupplier<T extends HttpCookiesActionSupplier<T>>
        extends SequentialActionSupplier<HttpStepContext, CookieManager, T> {

    private HttpCookiesActionSupplier(String description) {
        super(description);

        performOn(getCookieManager());
    }

    /**
     * Builds action that clears the cookie store of current http client.
     */
    private static final class HttpClearCookiesActionSupplier extends HttpCookiesActionSupplier<HttpClearCookiesActionSupplier> {

        private HttpClearCookiesActionSupplier() {
            super("Clear the cookie store of current http client");
        }

        @Override
        protected void performActionOn(CookieManager value) {
            value.getCookieStore().removeAll();
        }
    }

    /**
     * Builds action that adds cookies to the cookie store of current http client.
     */
    private static class HttpAddCookiesActionSupplier extends HttpCookiesActionSupplier<HttpAddCookiesActionSupplier> {

        private final Map<URI, List<HttpCookie>> cookieToAdd = new HashMap<>();

        private HttpAddCookiesActionSupplier() {
            super("Add cookies to the cookie store of current http client");
        }

        @Override
        protected void performActionOn(CookieManager value) {
            var cookieStore = value.getCookieStore();
            cookieToAdd.forEach((key, mapValue) ->
                    mapValue.forEach(cookie -> {
                        var description = ofNullable(key)
                                .map(uri -> format("Add cookie %s associated with %s to the cookie store", uri, cookie))
                                .orElseGet(() -> format("Add cookie %s to the cookie store", cookie));

                        cookieStore.add(key, cookie);
                        action(format(description, cookie),
                                (Consumer<CookieStore>) store -> store.add(key, cookie));
                    }));
        }

        /**
         * Adds all the cookies into cookie cache of the current http client.
         *
         * @param uri a {@code URI} where the cookies come from
         * @param cookiesToBeAdded cookies to be added
         * @return self-reference
         */
        private HttpAddCookiesActionSupplier addCookies(String uri, HttpCookie... cookiesToBeAdded) {
            checkArgument(nonNull(cookiesToBeAdded), "Cookies to be added should not be a null value");
            checkArgument(cookiesToBeAdded.length > 0, "At least one cookie should be defined for the adding");
            URI uriInstance = ofNullable(uri).map(s -> {
                try {
                    return new URI(s);
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }).orElse(null);

            ofNullable(cookieToAdd.get(uriInstance)).ifPresentOrElse(
                    httpCookies -> {
                        List<HttpCookie> toAdd = new ArrayList<>();
                        stream(cookiesToBeAdded).forEach(cookie -> {
                            if (!httpCookies.contains(cookie)) {
                                toAdd.add(cookie);
                            }
                        });
                        httpCookies.addAll(toAdd);
                    },
                    () -> cookieToAdd.put(uriInstance, asList(cookiesToBeAdded)));
            return this;
        }

        /**
         * Adds all the cookies into cookie cache of the current http client.
         *
         * @param cookieMap is a map of cached cookies. Key is a {@code URI} where the cookies come from.
         *                  Value is a list of cookies to be added.
         * @return self-reference
         */
        private HttpAddCookiesActionSupplier addCookies(Map<String, List<HttpCookie>> cookieMap) {
            cookieMap.forEach(((s, httpCookies) -> addCookies(s, httpCookies.toArray(new HttpCookie[]{}))));
            return this;
        }
    }


    /**
     * Creates an action that clears the cookie store of the http client.
     *
     * @return an instance of {@link HttpCookiesActionSupplier}
     */
    public static HttpCookiesActionSupplier<?> clearCookieStore() {
        return new HttpClearCookiesActionSupplier();
    }

    /**
     * Creates an action that adds cookies to the cookie store of current http client.
     *
     * @param uri a {@code URI} where the cookies come from
     * @param cookiesToBeAdded cookies to be added
     * @return an instance of {@link HttpCookiesActionSupplier}
     */
    public static HttpCookiesActionSupplier<?> addToCookies(String uri, HttpCookie... cookiesToBeAdded) {
        return new HttpAddCookiesActionSupplier().addCookies(uri, cookiesToBeAdded);
    }

    /**
     * Creates an action that adds cookies to the cookie store of current http client.
     *
     * @param cookieMap is a map of cached cookies. Key is a {@code URI} where the cookies come from.
     *                  Value is a list of cookies to be added.
     * @return an instance of {@link HttpCookiesActionSupplier}
     */
    public static HttpCookiesActionSupplier<?> addToCookies(Map<String, List<HttpCookie>> cookieMap) {
        return new HttpAddCookiesActionSupplier().addCookies(cookieMap);
    }
}
