package ru.tinkoff.qa.neptune.http.api.cookies;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

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
        extends SequentialActionSupplier<CookieManager, R, S> {

    private DeleteHttpCookiesActionSupplier() {
        super();
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
    private static final class DeleteAllHttpCookies extends DeleteHttpCookiesActionSupplier<CookieStore, DeleteAllHttpCookies> {

        private DeleteAllHttpCookies() {
            super();
            performOn(CookieManager::getCookieStore);
        }

        @Override
        protected void performActionOn(CookieStore value) {
            value.removeAll();
        }
    }


    @MaxDepthOfReporting(0)
    private static final class DeleteDefinedHttpCookies extends DeleteHttpCookiesActionSupplier<CookieStore, DeleteDefinedHttpCookies> {

        @StepParameter(value = "Http cookies to delete")
        private final Collection<HttpCookie> toDelete;

        private DeleteDefinedHttpCookies(Collection<HttpCookie> toDelete) {
            super();
            checkArgument(nonNull(toDelete) && toDelete.size() > 0,
                    "Should be defined at least one cookie");
            this.toDelete = toDelete;
            performOn(CookieManager::getCookieStore);
        }

        @Override
        protected void performActionOn(CookieStore value) {
            toDelete.forEach(httpCookie -> value.remove(null, httpCookie));
        }
    }

    @MaxDepthOfReporting(0)
    @IncludeParamsOfInnerGetterStep
    private static final class DeleteFoundHttpCookies extends DeleteHttpCookiesActionSupplier<List<HttpCookie>, DeleteFoundHttpCookies> {

        private CookieStore cookieStore;

        @SafeVarargs
        private DeleteFoundHttpCookies(URI uri, Criteria<HttpCookie>... toBeRemoved) {
            super();
            checkArgument(nonNull(toBeRemoved) && toBeRemoved.length > 0,
                    "It is necessary to define at least one criteria to find http cookies for removal");

            var getCookies = ofNullable(uri)
                    .map(GetHttpCookiesSupplier::httpCookies)
                    .orElseGet(GetHttpCookiesSupplier::httpCookies);
            stream(toBeRemoved).forEach(getCookies::criteria);
            performOn(getCookies.from(cookieManager -> {
                cookieStore = cookieManager.getCookieStore();
                return cookieManager;
            }));
        }

        @Override
        protected void performActionOn(List<HttpCookie> value) {
            value.forEach(httpCookie -> cookieStore.remove(null, httpCookie));
        }
    }
}
