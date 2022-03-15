package ru.tinkoff.qa.neptune.http.api.cookies;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

/**
 * This class is designed to build an action that removes cookies from a "cookie jar".
 */
@Description("Delete http cookies")
public abstract class DeleteHttpCookiesActionSupplier<R, S extends DeleteHttpCookiesActionSupplier<R, S>>
        extends SequentialActionSupplier<HttpStepContext, R, S> {

    CookieStore cookieStore;

    private DeleteHttpCookiesActionSupplier() {
        super();
    }

    @Override
    protected void onStart(HttpStepContext httpStepContext) {
        cookieStore = httpStepContext
                .getCurrentClient()
                .cookieHandler()
                .map(cookieHandler -> ((CookieManager) cookieHandler).getCookieStore())
                .orElseThrow(() -> new IllegalStateException("There is no cookie manager"));
    }

    /**
     * Creates an instance to build an action that cleans a "cookie jar".
     *
     * @return instance of {@link DeleteHttpCookiesActionSupplier}
     */
    public static DeleteHttpCookiesActionSupplier<?, ?> deleteCookies() {
        return new DeleteAllHttpCookies();
    }

    /**
     * Creates an instance to build an action that cleans a "cookie jar" of cookies.
     * These cookies are expected to be found by criteria.
     *
     * @param uri         is an {@link URI} that associated with cookies to remove
     * @param toBeRemoved which cookies should be deleted
     * @return instance of {@link DeleteHttpCookiesActionSupplier}
     */
    @SafeVarargs
    public static DeleteHttpCookiesActionSupplier<?, ?> deleteCookies(URI uri,
                                                                      Criteria<HttpCookie>... toBeRemoved) {
        return new DeleteFoundHttpCookies(uri, toBeRemoved);
    }

    /**
     * Creates an instance to build an action that cleans a "cookie jar" of defined cookies.
     *
     * @param toBeRemoved cookies that should be deleted
     * @return instance of {@link DeleteHttpCookiesActionSupplier}
     */
    public static DeleteHttpCookiesActionSupplier<?, ?> deleteCookies(Collection<HttpCookie> toBeRemoved) {
        return new DeleteDefinedHttpCookies(toBeRemoved);
    }

    @MaxDepthOfReporting(0)
    @Description("Delete all http cookies")
    private static final class DeleteAllHttpCookies extends DeleteHttpCookiesActionSupplier<HttpStepContext, DeleteAllHttpCookies> {

        private DeleteAllHttpCookies() {
            super();
            performOn(httpStepContext -> httpStepContext);
        }

        @Override
        protected void howToPerform(HttpStepContext value) {
            cookieStore.removeAll();
        }
    }


    @MaxDepthOfReporting(0)
    @DefinePerformOnParameterName("Http cookies to delete")
    private static final class DeleteDefinedHttpCookies extends DeleteHttpCookiesActionSupplier<Collection<HttpCookie>, DeleteDefinedHttpCookies> {

        private DeleteDefinedHttpCookies(Collection<HttpCookie> toDelete) {
            super();
            checkArgument(nonNull(toDelete) && !toDelete.isEmpty(),
                    "Should be defined at least one cookie");
            performOn(toDelete);
        }

        @Override
        protected void howToPerform(Collection<HttpCookie> value) {
            value.forEach(httpCookie -> cookieStore.remove(null, httpCookie));
        }
    }

    @MaxDepthOfReporting(0)
    @IncludeParamsOfInnerGetterStep
    private static final class DeleteFoundHttpCookies extends DeleteHttpCookiesActionSupplier<List<HttpCookie>, DeleteFoundHttpCookies> {

        @SafeVarargs
        private DeleteFoundHttpCookies(URI uri, Criteria<HttpCookie>... toBeRemoved) {
            super();
            checkArgument(nonNull(toBeRemoved) && toBeRemoved.length > 0,
                    "It is necessary to define at least one criteria to find http cookies for removal");

            var getCookies = ofNullable(uri)
                    .map(GetHttpCookiesSupplier::httpCookies)
                    .orElseGet(GetHttpCookiesSupplier::httpCookies);
            stream(toBeRemoved).forEach(getCookies::criteria);
            performOn(getCookies);
        }

        @Override
        protected void howToPerform(List<HttpCookie> value) {
            value.forEach(httpCookie -> cookieStore.remove(null, httpCookie));
        }
    }
}
