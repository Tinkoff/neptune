package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import javax.net.ssl.SSLParameters;
import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'default.http.ssl.parameters'} and convert it to an instance of
 * {@link SslParametersSupplier}
 */
public final class DefaultHttpSslParametersProperty implements ObjectPropertySupplier<DefaultHttpSslParametersProperty.SslParametersSupplier> {

    private static final String PROPERTY = "default.http.ssl.parameters";

    /**
     * This instance reads value of the property {@code 'default.http.ssl.parameters'} and returns a {@link Supplier}
     * of {@link SSLParameters}. Invocation of the {@link Supplier#get()} returns actual value of the property.
     * The value provided must be fully qualified name of a {@link SslParametersSupplier} subclass.
     */
    public static final DefaultHttpSslParametersProperty DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY =
            new DefaultHttpSslParametersProperty();

    private DefaultHttpSslParametersProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }

    public static abstract class SslParametersSupplier implements Supplier<SSLParameters> {
    }
}
