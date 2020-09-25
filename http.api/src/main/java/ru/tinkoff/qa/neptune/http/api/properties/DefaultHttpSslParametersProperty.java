package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import javax.net.ssl.SSLParameters;
import java.util.function.Supplier;

@PropertyDescription(description = "Defines full name of a class which extends DefaultHttpSslParametersProperty.SslParametersSupplier and whose objects " +
        "supply instances of javax.net.ssl.SSLParameters",
        section = "Http client. General")
@PropertyName("DEFAULT_HTTP_SSL_PARAMETERS")
public final class DefaultHttpSslParametersProperty implements ObjectPropertySupplier<DefaultHttpSslParametersProperty.SslParametersSupplier> {
    /**
     * This instance reads value of the property {@code 'DEFAULT_HTTP_SSL_PARAMETERS'} and returns a {@link Supplier}
     * of {@link SSLParameters}. Invocation of the {@link Supplier#get()} returns actual value of the property.
     * The value provided must be fully qualified name of a {@link SslParametersSupplier} subclass.
     */
    public static final DefaultHttpSslParametersProperty DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY =
            new DefaultHttpSslParametersProperty();

    private DefaultHttpSslParametersProperty() {
        super();
    }

    public static abstract class SslParametersSupplier implements Supplier<SSLParameters> {
    }
}
