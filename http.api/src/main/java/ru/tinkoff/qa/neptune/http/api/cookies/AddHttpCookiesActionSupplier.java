package ru.tinkoff.qa.neptune.http.api.cookies;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

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
public final class AddHttpCookiesActionSupplier extends SequentialActionSupplier<CookieManager, CookieStore, AddHttpCookiesActionSupplier> {

    @StepParameter(value = "Associate with URI", doNotReportNullValues = true)
    private final URI uri;

    @StepParameter(value = "Cookies to add")
    private final List<HttpCookie> cookies;


    private AddHttpCookiesActionSupplier(URI uri, List<HttpCookie> cookies) {
        super("Add http cookies");
        this.uri = uri;
        checkArgument(nonNull(cookies) && cookies.size() > 0,
                "Should be defined at least one cookie");
        this.cookies = cookies;
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
        return new AddHttpCookiesActionSupplier(uri, cookies).performOn(CookieManager::getCookieStore);
    }

    @Override
    protected void performActionOn(CookieStore value) {
        cookies.forEach(httpCookie -> {
            value.add(uri, httpCookie);
        });
    }
}
