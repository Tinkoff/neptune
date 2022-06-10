package ru.tinkoff.qa.neptune.http.api.cookies;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static ru.tinkoff.qa.neptune.http.api.GetCurrentCookieStore.getCurrentCookieStore;

@CaptureOnSuccess(by = CollectionCaptor.class)
@Description("Http cookies")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Http cookie criteria")
public final class GetHttpCookiesSupplier extends SequentialGetStepSupplier.GetListChainedStepSupplier<HttpStepContext, List<HttpCookie>, CookieStore, HttpCookie, GetHttpCookiesSupplier> {

    @StepParameter(value = "Associated with URI", doNotReportNullValues = true)
    final URI uri;

    private GetHttpCookiesSupplier() {
        super(cookieStore -> new ArrayList<>(cookieStore.getCookies()));
        uri = null;
        from(getCurrentCookieStore());
    }

    private GetHttpCookiesSupplier(URI uri) {
        super(cookieStore -> new ArrayList<>(cookieStore.get(uri)));
        this.uri = uri;
        from(getCurrentCookieStore());
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
}
