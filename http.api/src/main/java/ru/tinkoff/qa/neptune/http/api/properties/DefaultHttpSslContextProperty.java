package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import javax.net.ssl.SSLContext;
import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'default.http.ssl.context'} and convert it to an instance of
 * {@link SslContextSupplier}
 */
public final class DefaultHttpSslContextProperty implements ObjectPropertySupplier<DefaultHttpSslContextProperty.SslContextSupplier> {

    private static final String PROPERTY = "default.http.ssl.context";

    /**
     * This instance reads value of the property {@code 'default.http.ssl.context'} and returns a {@link Supplier}
     * of {@link SSLContext}. Invocation of the {@link Supplier#get()} returns actual value of the property. The value
     * provided must be fully qualified name of a {@link SslContextSupplier} subclass.
     */
    public static final DefaultHttpSslContextProperty DEFAULT_HTTP_SSL_CONTEXT_PROPERTY =
            new DefaultHttpSslContextProperty();

    private DefaultHttpSslContextProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }

    public static abstract class SslContextSupplier implements Supplier<SSLContext> {
    }
}
