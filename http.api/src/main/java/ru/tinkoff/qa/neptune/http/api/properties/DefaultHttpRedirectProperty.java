package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.net.http.HttpClient;

@PropertyDescription(description = "Defines default used http redirect policy. See the enum java.net.http.HttpClient.Version",
        section = "Http client. General")
@PropertyName("DEFAULT_HTTP_REDIRECT_POLICY")
public final class DefaultHttpRedirectProperty implements EnumPropertySuppler<HttpClient.Redirect> {

    /**
     * This instance reads value of the property {@code 'DEFAULT_HTTP_REDIRECT_POLICY'} and returns a constant of
     * {@link HttpClient.Redirect}. The value should be a name of one of the constant.
     */
    public static final DefaultHttpRedirectProperty DEFAULT_HTTP_REDIRECT_PROPERTY =
            new DefaultHttpRedirectProperty();

    private DefaultHttpRedirectProperty() {
        super();
    }
}
