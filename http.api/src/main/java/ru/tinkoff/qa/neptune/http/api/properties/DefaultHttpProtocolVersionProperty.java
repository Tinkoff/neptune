package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.net.http.HttpClient;

@PropertyDescription(description = "Defines default used version of http protocol. See the enum java.net.http.HttpClient.Version",
        section = "Http client. General")
@PropertyName("DEFAULT_HTTP_PROTOCOL_VERSION")
public final class DefaultHttpProtocolVersionProperty implements EnumPropertySuppler<HttpClient.Version> {

    public static final DefaultHttpProtocolVersionProperty DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY =
            new DefaultHttpProtocolVersionProperty();

    private DefaultHttpProtocolVersionProperty() {
        super();
    }
}
