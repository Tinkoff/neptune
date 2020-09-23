package ru.tinkoff.qa.neptune.http.api.cookies;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@MakeStringCapturesOnFinishing
public final class GetHttpCookiesSupplier extends SequentialGetStepSupplier
        .GetIterableStepSupplier<CookieManager, List<HttpCookie>, HttpCookie, GetHttpCookiesSupplier> {

    @StepParameter(value = "Associated with URI", doNotReportNullValues = true)
    private final URI uri;

    private GetHttpCookiesSupplier() {
        super("Http cookies", cookieManager -> new ArrayList<>(cookieManager.getCookieStore().getCookies()));
        uri = null;
    }

    private GetHttpCookiesSupplier(URI uri) {
        super("Http cookies", cookieManager -> new ArrayList<>(cookieManager.getCookieStore().get(uri)));
        this.uri = uri;
    }

    /**
     * Builds a step-function to get not expired cookies.
     *
     * @return an instance of {@link GetHttpCookiesSupplier}
     */
    public static GetHttpCookiesSupplier httpCookies() {
        return new GetHttpCookiesSupplier();
    }

    /**
     * Builds a step-function to get not expired cookies.
     *
     * @param uri is an {@link URI} that associated with resulted cookies
     * @return an instance of {@link GetHttpCookiesSupplier}
     */
    public static GetHttpCookiesSupplier httpCookies(URI uri) {
        return new GetHttpCookiesSupplier(uri);
    }

    @Override
    public GetHttpCookiesSupplier criteria(Criteria<? super HttpCookie> criteria) {
        return super.criteria(criteria);
    }
}
