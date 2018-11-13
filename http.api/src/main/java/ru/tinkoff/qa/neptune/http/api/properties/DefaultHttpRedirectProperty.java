package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.net.http.HttpClient;

/**
 * This class is designed to read value of the property {@code 'default.http.redirect.policy'} and convert it to a constant of
 * {@link HttpClient.Redirect}
 */
public final class DefaultHttpRedirectProperty implements EnumPropertySuppler<HttpClient.Redirect> {

    private static final String PROPERTY = "default.http.redirect.policy";

    /**
     * This instance reads value of the property {@code 'default.http.redirect.policy'} and returns a constant of
     * {@link HttpClient.Redirect}. The value should be a name of one of the constant.
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
}
