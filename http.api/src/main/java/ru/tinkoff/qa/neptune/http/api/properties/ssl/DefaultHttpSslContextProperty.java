package ru.tinkoff.qa.neptune.http.api.properties.ssl;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import javax.net.ssl.SSLContext;
import java.util.function.Supplier;

@PropertyDescription(description = {"Defines full name of a class which extends DefaultHttpSslContextProperty.SslContextSupplier and whose objects",
        "supply instances of javax.net.ssl.SSLContext"},
        section = "Http client. General")
@PropertyName("DEFAULT_HTTP_SSL_CONTEXT")
@PropertyDefaultValue("ru.tinkoff.qa.neptune.http.api.properties.ssl.AllTrustedSslContextSupplier")
public final class DefaultHttpSslContextProperty implements ObjectPropertySupplier<DefaultHttpSslContextProperty.SslContextSupplier> {
    /**
     * This instance reads value of the property {@code 'DEFAULT_HTTP_SSL_CONTEXT'} and returns a {@link Supplier}
     * of {@link SSLContext}. Invocation of the {@link Supplier#get()} returns actual value of the property. The value
     * provided must be fully qualified name of a {@link SslContextSupplier} subclass.
     */
    public static final DefaultHttpSslContextProperty DEFAULT_HTTP_SSL_CONTEXT_PROPERTY =
            new DefaultHttpSslContextProperty();

    private DefaultHttpSslContextProperty() {
        super();
    }

    public static abstract class SslContextSupplier implements Supplier<SSLContext> {
    }
}
