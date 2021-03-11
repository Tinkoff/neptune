package ru.tinkoff.qa.neptune.http.api.properties.authentification;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.net.Authenticator;
import java.util.function.Supplier;

@PropertyDescription(description = {"Defines full name of a class which extends DefaultHttpAuthenticatorProperty.AuthenticatorSupplier",
        "and whose objects supply instances of java.net.Authenticator"},
        section = "Http client. General")
@PropertyName("DEFAULT_HTTP_AUTHENTICATOR")
public final class DefaultHttpAuthenticatorProperty implements ObjectPropertySupplier<Authenticator, DefaultHttpAuthenticatorProperty.AuthenticatorSupplier> {
    /**
     * This instance reads value of the property {@code 'DEFAULT_HTTP_AUTHENTICATOR'} and returns a {@link Supplier}
     * of {@link Authenticator}. Invocation of the {@link Supplier#get()} returns actual value of the property. The value
     * provided must be fully qualified name of a {@link AuthenticatorSupplier} subclass.
     */
    public static final DefaultHttpAuthenticatorProperty DEFAULT_HTTP_AUTHENTICATOR_PROPERTY =
            new DefaultHttpAuthenticatorProperty();

    private DefaultHttpAuthenticatorProperty() {
        super();
    }

    public static abstract class AuthenticatorSupplier implements Supplier<Authenticator> {
    }
}
