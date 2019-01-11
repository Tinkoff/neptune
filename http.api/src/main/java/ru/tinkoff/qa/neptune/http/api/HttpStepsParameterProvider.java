package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.http.HttpClient;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.ConstructorParameters.params;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultConnectTimeOutProperty.DEFAULT_CONNECT_TIME_OUT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpAuthenticatorProperty.DEFAULT_HTTP_AUTHENTICATOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpCookieManagerProperty.DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpExecutorProperty.DEFAULT_HTTP_EXECUTOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpPriorityProperty.DEFAULT_HTTP_PRIORITY_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpProtocolVersionProperty.DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpProxySelectorProperty.DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpRedirectProperty.DEFAULT_HTTP_REDIRECT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpSslContextProperty.DEFAULT_HTTP_SSL_CONTEXT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpSslParametersProperty.DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY;

public class HttpStepsParameterProvider implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        final var builder = HttpClient.newBuilder();
        ofNullable(DEFAULT_CONNECT_TIME_OUT_PROPERTY.get())
                .ifPresent(builder::connectTimeout);

        ofNullable(DEFAULT_HTTP_AUTHENTICATOR_PROPERTY.get())
                .ifPresent(authenticatorSupplier -> ofNullable(authenticatorSupplier.get())
                        .ifPresent(builder::authenticator));

        ofNullable(DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY.get())
                .map(cookieHandlerSupplier -> {
                    ofNullable(cookieHandlerSupplier.get()).ifPresent(builder::cookieHandler);
                    return builder;
                }).orElseGet(() -> {
                    var cookieHandler = ofNullable(CookieHandler.getDefault()).orElseGet(() -> {
                        var handler = new CookieManager();
                        CookieHandler.setDefault(handler);
                        return handler;
                    });
                    return builder.cookieHandler(cookieHandler);
                });

        ofNullable(DEFAULT_HTTP_EXECUTOR_PROPERTY.get())
                .ifPresent(executorSupplier -> ofNullable(executorSupplier.get())
                        .ifPresent(builder::executor));

        ofNullable(DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY.get())
                .ifPresent(builder::version);

        ofNullable(DEFAULT_HTTP_PRIORITY_PROPERTY.get())
                .ifPresent(builder::priority);

        ofNullable(DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY.get())
                .ifPresent(proxySelectorSupplier -> ofNullable(proxySelectorSupplier.get())
                        .ifPresent(builder::proxy));

        ofNullable(DEFAULT_HTTP_REDIRECT_PROPERTY.get())
                .ifPresent(builder::followRedirects);

        ofNullable(DEFAULT_HTTP_SSL_CONTEXT_PROPERTY.get())
                .ifPresent(sslContextSupplier -> ofNullable(sslContextSupplier.get())
                        .ifPresent(builder::sslContext));

        ofNullable(DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY.get())
                .ifPresent(sslParametersSupplier -> ofNullable(sslParametersSupplier.get())
                        .ifPresent(builder::sslParameters));

        return params(builder);
    }
}
