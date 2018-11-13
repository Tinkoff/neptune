package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.net.Authenticator;
import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'default.http.authenticator'} and convert it to an instance of
 * {@link AuthenticatorSupplier}
 */
public final class DefaultHttpAuthenticatorProperty implements ObjectPropertySupplier<DefaultHttpAuthenticatorProperty.AuthenticatorSupplier> {

    private static final String PROPERTY = "default.http.authenticator";

    /**
     * This instance reads value of the property {@code 'default.http.authenticator'} and returns a {@link Supplier}
     * of {@link Authenticator}. Invocation of the {@link Supplier#get()} returns actual value of the property. The value
     * provided must be fully qualified name of a {@link AuthenticatorSupplier} subclass.
     */
    public static final DefaultHttpAuthenticatorProperty DEFAULT_HTTP_AUTHENTICATOR_PROPERTY =
            new DefaultHttpAuthenticatorProperty();

    private DefaultHttpAuthenticatorProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }

    public static abstract class AuthenticatorSupplier implements Supplier<Authenticator> {
    }
}
