package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;
import ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest;

import java.net.http.HttpRequest;

/**
 * This class is designed to read value of the property {@code 'default.http.domain.to.respond'} and convert it to
 * {@link java.net.URL}. This is the frequently uses domain of the service to respond. It makes possible to use
 * relative (part of the) URI by {@link PreparedHttpRequest#GET(java.lang.String)},
 * {@link PreparedHttpRequest#POST(String, HttpRequest.BodyPublisher)},
 * {@link PreparedHttpRequest#PUT(String, HttpRequest.BodyPublisher)},
 * {@link PreparedHttpRequest#DELETE(String)},
 * {@link PreparedHttpRequest#methodRequest(String, String)} and
 * {@link PreparedHttpRequest#methodRequest(String, String, HttpRequest.BodyPublisher)}
 */
@Deprecated(since = "0.11.4-ALPHA", forRemoval = true)
public final class DefaultHttpDomainToRespondProperty implements URLValuePropertySupplier {

    private static final String PROPERTY = "default.http.domain.to.respond";

    /**
     * This instance reads value of the property {@code 'default.http.domain.to.respond'} and returns an {@link java.net.URL}.
     * This is the frequently uses domain of the service to respond.
     */
    public static final DefaultHttpDomainToRespondProperty DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY =
            new DefaultHttpDomainToRespondProperty();

    private DefaultHttpDomainToRespondProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }
}
