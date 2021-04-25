package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import java.net.CookieManager;
import java.net.http.HttpClient;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.properties.authentification.DefaultHttpAuthenticatorProperty.DEFAULT_HTTP_AUTHENTICATOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.cookies.DefaultHttpCookieManagerProperty.DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.executor.DefaultHttpExecutorProperty.DEFAULT_HTTP_EXECUTOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.priority.DefaultHttpPriorityProperty.DEFAULT_HTTP_PRIORITY_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.protocol.version.DefaultHttpProtocolVersionProperty.DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.proxy.DefaultHttpProxySelectorProperty.DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.redirect.DefaultHttpRedirectProperty.DEFAULT_HTTP_REDIRECT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslContextProperty.DEFAULT_HTTP_SSL_CONTEXT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslParametersProperty.DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.DefaultConnectTimeOutProperty.DEFAULT_CONNECT_TIME_OUT_PROPERTY;

public class HttpStepsParameterProvider implements ParameterProvider {
    @Override
    public Object[] provide() {
        final var builder = HttpClient.newBuilder();
        ofNullable(DEFAULT_CONNECT_TIME_OUT_PROPERTY.get())
                .ifPresent(builder::connectTimeout);

        ofNullable(DEFAULT_HTTP_AUTHENTICATOR_PROPERTY.get()).ifPresent(builder::authenticator);
        ofNullable(DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY.get())
                .map(builder::cookieHandler)
                .orElseGet(() -> {
                    var cookieHandler = new CookieManager();
                    return builder.cookieHandler(cookieHandler);
                });

        ofNullable(DEFAULT_HTTP_EXECUTOR_PROPERTY.get()).ifPresent(builder::executor);
        ofNullable(DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY.get()).ifPresent(builder::version);
        ofNullable(DEFAULT_HTTP_PRIORITY_PROPERTY.get()).ifPresent(builder::priority);
        ofNullable(DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY.get()).ifPresent(builder::proxy);
        ofNullable(DEFAULT_HTTP_REDIRECT_PROPERTY.get()).ifPresent(builder::followRedirects);
        ofNullable(DEFAULT_HTTP_SSL_CONTEXT_PROPERTY.get()).ifPresent(builder::sslContext);
        ofNullable(DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY.get()).ifPresent(builder::sslParameters);

        return new Object[]{builder};
    }
}
