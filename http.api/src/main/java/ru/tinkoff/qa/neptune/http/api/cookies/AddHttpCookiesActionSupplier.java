package ru.tinkoff.qa.neptune.http.api.cookies;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

/**
 * This class is designed to build an action that adds cookies to a "cookie jar".
 */
@Description("Add http cookies")
@SequentialActionSupplier.DefinePerformOnParameterName("Cookies to add")
public final class AddHttpCookiesActionSupplier extends SequentialActionSupplier<HttpStepContext, List<HttpCookie>, AddHttpCookiesActionSupplier> {

    @StepParameter(value = "Associate with URI", doNotReportNullValues = true)
    private final URI uri;
    private CookieStore cookieStore;

    private AddHttpCookiesActionSupplier(URI uri, List<HttpCookie> cookies) {
        super();
        this.uri = uri;
        checkArgument(nonNull(cookies) && !cookies.isEmpty(),
                "Should be defined at least one cookie");
        performOn(cookies);
    }

    /**
     * Creates a step action that adds all the defined cookies to cookie jar.
     *
     * @param uri     the uri these cookies associated with.
     *                if {@code null}, then cookies are not associated
     *                with an URI
     * @param cookies cookies to be added
     * @return self-reference
     */
    public static AddHttpCookiesActionSupplier addHttpCookies(URI uri, List<HttpCookie> cookies) {
        return new AddHttpCookiesActionSupplier(uri, cookies);
    }

    @Override
    protected void onStart(HttpStepContext httpStepContext) {
        cookieStore = httpStepContext
                .getCurrentClient()
                .cookieHandler()
                .map(cookieHandler -> ((CookieManager) cookieHandler).getCookieStore())
                .orElseThrow(() -> new IllegalStateException("There is no cookie manager"));
    }

    @Override
    protected void howToPerform(List<HttpCookie> value) {
        value.forEach(httpCookie -> cookieStore.add(uri, httpCookie));
    }
}
