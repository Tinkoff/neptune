package ru.tinkoff.qa.neptune.http.api.cookies;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@CaptureOnSuccess(by = CollectionCaptor.class)
@Description("Http cookies")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Http cookie criteria")
public final class GetHttpCookiesSupplier extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<CookieManager, List<HttpCookie>, CookieManager, HttpCookie, GetHttpCookiesSupplier> {

    @StepParameter(value = "Associated with URI", doNotReportNullValues = true)
    private final URI uri;

    private GetHttpCookiesSupplier() {
        super(cookieManager -> new ArrayList<>(cookieManager.getCookieStore().getCookies()));
        uri = null;
        from(cookieManager -> cookieManager);
    }

    private GetHttpCookiesSupplier(URI uri) {
        super(cookieManager -> new ArrayList<>(cookieManager.getCookieStore().get(uri)));
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

    @Override
    protected GetHttpCookiesSupplier from(Function<CookieManager, ? extends CookieManager> from) {
        return super.from(from);
    }
}
