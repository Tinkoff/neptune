package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.net.http.HttpClient;
import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'default.http.redirect.policy'} and convert it to an instance of
 * {@link RedirectSupplier}
 */
public final class DefaultHttpRedirectProperty implements ObjectPropertySupplier<DefaultHttpRedirectProperty.RedirectSupplier> {

    private static final String PROPERTY = "default.http.redirect.policy";

    /**
     * This instance reads value of the property {@code 'default.http.redirect.policy'} and returns a {@link Supplier}
     * of {@link HttpClient.Redirect}. Invocation of the {@link Supplier#get()} returns actual value of the property.
     * The value provided must be fully qualified name of a {@link RedirectSupplier} subclass.
     */
    public static final DefaultHttpRedirectProperty DEFAULT_HTTP_REDIRECT_PROPERTY =
            new DefaultHttpRedirectProperty();

    private DefaultHttpRedirectProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }

    public static abstract class RedirectSupplier implements Supplier<HttpClient.Redirect> {
    }
}
