package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.net.http.HttpClient;

/**
 * This class is designed to read value of the property {@code 'default.http.client.version'} and convert it to one of
 * constants from {@link HttpClient.Version}.
 */
public final class DefaultHttpProtocolVersionProperty implements EnumPropertySuppler<HttpClient.Version> {

    private static final String PROPERTY = "default.http.client.version";

    /**
     * This instance reads value of the property {@code 'default.http.client.version'} and returns a constant of
     * {@link HttpClient.Version}. The value should be a name of one of the constant.
     */
    public static final DefaultHttpProtocolVersionProperty DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY =
            new DefaultHttpProtocolVersionProperty();

    private DefaultHttpProtocolVersionProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }
}
