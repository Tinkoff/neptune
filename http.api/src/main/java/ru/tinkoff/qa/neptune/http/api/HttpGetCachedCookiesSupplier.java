package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.List;
import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.http.api.GetCookieManager.getCookieManager;

/**
 * This class is designed to build functions that get applicable cookies from a cookie cache
 */
@MakeStringCapturesOnFinishing
public final class HttpGetCachedCookiesSupplier extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<HttpStepContext, List<HttpCookie>, CookieManager, HttpCookie, HttpGetCachedCookiesSupplier> {

    private HttpGetCachedCookiesSupplier() {
        super("Cached cookies", cookieManager -> cookieManager.getCookieStore().getCookies());
    }

    /**
     * Builds a function to get applicable cookies stored by current http client.
     *
     * @return an instance of {@link HttpGetCachedCookiesSupplier}
     */
    public static HttpGetCachedCookiesSupplier cachedCookies() {
        return new HttpGetCachedCookiesSupplier().from(getCookieManager());
    }

    @Override
    public HttpGetCachedCookiesSupplier criteria(Criteria<? super HttpCookie> criteria) {
        return super.criteria(criteria);
    }

    @Override
    public HttpGetCachedCookiesSupplier criteria(String conditionDescription, Predicate<? super HttpCookie> condition) {
        return super.criteria(conditionDescription, condition);
    }
}
