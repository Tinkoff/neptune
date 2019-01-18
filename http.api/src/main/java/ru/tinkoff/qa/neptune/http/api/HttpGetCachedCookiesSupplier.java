package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.GetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubIterable.getIterable;

/**
 * This class is designed to build functions that get applicable cookies from a cookie cache
 */
@MakeStringCapturesOnFinishing
public final class HttpGetCachedCookiesSupplier extends GetStepSupplier<HttpStepContext, List<HttpCookie>, HttpGetCachedCookiesSupplier> {

    private Predicate<HttpCookie> criteria;
    private static final Function<HttpStepContext, List<HttpCookie>> GET_COOKIES = httpSteps ->
            httpSteps.getCurrentClient().cookieHandler()
                    .map(cookieHandler -> {
                        if (!CookieManager.class.isAssignableFrom(cookieHandler.getClass())) {
                            throw new IllegalStateException(format("We support only %s as cookie handler for a while", CookieManager.class.getName()));
                        }
                        return new ArrayList<>(((CookieManager) cookieHandler).getCookieStore().getCookies());
                    })
                    .orElseThrow(() -> {
                        throw new IllegalStateException("Can't get access to a cookie store of the current http client");
                    });

    private HttpGetCachedCookiesSupplier() {
        super();
    }

    /**
     * Builds a function to get applicable cookies stored by current http client.
     *
     * @return an instance of {@link HttpGetCachedCookiesSupplier}
     */
    public static HttpGetCachedCookiesSupplier cachedCookies() {
        return new HttpGetCachedCookiesSupplier();
    }

    /**
     * Sets a criteria to get cached cookies. It is supposed that cookies to be returned should meet this criteria.
     *
     * @param criteria is a criteria that cached cookie should meet to be returned
     * @return self-reference
     */
    public HttpGetCachedCookiesSupplier criteriaToGetCookies(Predicate<HttpCookie> criteria) {
        this.criteria = criteria;
        return this;
    }

    public Function<HttpStepContext, List<HttpCookie>> get() {
        return ofNullable(super.get()).orElseGet(() -> {
            ofNullable(criteria)
                    .ifPresentOrElse(httpCookiePredicate ->
                                    super.set(getIterable("Cached cookies", GET_COOKIES,
                                            httpCookiePredicate, true, true)),
                            () -> super.set(toGet("Cached cookies", GET_COOKIES)));
            return super.get();
        });
    }
}
