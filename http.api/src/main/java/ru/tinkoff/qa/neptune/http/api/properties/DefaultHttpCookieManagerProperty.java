package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'default.http.cookie.handler'} and convert it to an instance of
 * {@link CookieManagerSupplier}
 */
@Deprecated(since = "0.11.4-ALPHA", forRemoval = true)
public final class DefaultHttpCookieManagerProperty implements ObjectPropertySupplier<DefaultHttpCookieManagerProperty.CookieManagerSupplier> {

    private static final String PROPERTY = "default.http.cookie.handler";

    /**
     * This instance reads value of the property {@code 'default.http.cookie.handler'} and returns a {@link Supplier}
     * of {@link CookieHandler}. Invocation of the {@link Supplier#get()} returns actual value of the property. The value
     * provided must be fully qualified name of a {@link CookieManagerSupplier} subclass.
     */
    public static final DefaultHttpCookieManagerProperty DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY =
            new DefaultHttpCookieManagerProperty();

    private DefaultHttpCookieManagerProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }

    public static abstract class CookieManagerSupplier implements Supplier<CookieManager> {
    }
}
