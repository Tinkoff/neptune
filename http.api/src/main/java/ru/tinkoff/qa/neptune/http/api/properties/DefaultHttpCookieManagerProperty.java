package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.function.Supplier;

@PropertyDescription(description = "Defines full name of a class which extends DefaultHttpCookieManagerProperty.CookieManagerSupplier and whose objects " +
        "supply instances of java.net.CookieManager",
        section = "Http client. General")
@PropertyName("DEFAULT_HTTP_COOKIE_HANDLER")
public final class DefaultHttpCookieManagerProperty implements ObjectPropertySupplier<DefaultHttpCookieManagerProperty.CookieManagerSupplier> {
    /**
     * This instance reads value of the property {@code 'DEFAULT_HTTP_COOKIE_HANDLER'} and returns a {@link Supplier}
     * of {@link CookieHandler}. Invocation of the {@link Supplier#get()} returns actual value of the property. The value
     * provided must be fully qualified name of a {@link CookieManagerSupplier} subclass.
     */
    public static final DefaultHttpCookieManagerProperty DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY =
            new DefaultHttpCookieManagerProperty();

    private DefaultHttpCookieManagerProperty() {
        super();
    }

    public static abstract class CookieManagerSupplier implements Supplier<CookieManager> {
    }
}
