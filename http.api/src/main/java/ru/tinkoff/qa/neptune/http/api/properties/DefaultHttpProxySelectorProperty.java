package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.net.ProxySelector;
import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'default.http.proxy.selector'} and convert it to an instance of
 * {@link ProxySelectorSupplier}
 */
public final class DefaultHttpProxySelectorProperty implements ObjectPropertySupplier<DefaultHttpProxySelectorProperty.ProxySelectorSupplier> {

    private static final String PROPERTY = "default.http.proxy.selector";

    /**
     * This instance reads value of the property {@code 'default.http.proxy.selector'} and returns a {@link Supplier}
     * of {@link ProxySelector}. Invocation of the {@link Supplier#get()} returns actual value of the property. The value
     * provided must be fully qualified name of a {@link ProxySelectorSupplier} subclass.
     */
    public static final DefaultHttpProxySelectorProperty DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY =
            new DefaultHttpProxySelectorProperty();

    private DefaultHttpProxySelectorProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }

    public static abstract class ProxySelectorSupplier implements Supplier<ProxySelector> {
    }
}
