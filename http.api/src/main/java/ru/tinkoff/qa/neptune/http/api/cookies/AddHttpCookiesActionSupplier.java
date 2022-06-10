package ru.tinkoff.qa.neptune.http.api.cookies;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.http.api.GetCurrentCookieStore.getCurrentCookieStore;

/**
 * This class is designed to build an action that adds cookies to a "cookie jar".
 */
@Description("Add http cookies")
public final class AddHttpCookiesActionSupplier extends SequentialActionSupplier<HttpStepContext, CookieStore, AddHttpCookiesActionSupplier> {

    @StepParameter(value = "Associate with URI", doNotReportNullValues = true)
    private final URI uri;

    @StepParameter("Cookies to add")
    private final List<HttpCookie> cookies;

    private AddHttpCookiesActionSupplier(URI uri, List<HttpCookie> cookies) {
        super();
        this.uri = uri;
        checkArgument(nonNull(cookies) && !cookies.isEmpty(),
            "Should be defined at least one cookie");
        this.cookies = cookies;
        performOn(getCurrentCookieStore());
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
    protected void howToPerform(CookieStore value) {
        cookies.forEach(httpCookie -> value.add(uri, httpCookie));
    }
}
