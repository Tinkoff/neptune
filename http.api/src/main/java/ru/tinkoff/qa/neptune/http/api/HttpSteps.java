package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;

import java.net.CookieManager;
import java.net.http.HttpClient;

@CreateWith(provider = HttpStepsParameterProvider.class)
public class HttpSteps implements GetStep<HttpSteps>, PerformActionStep<HttpSteps>, Refreshable {

    private final HttpClient defaultClient;
    private HttpClient currentClient;

    public HttpSteps(HttpClient.Builder clientBuilder) {
        this.defaultClient = clientBuilder.build();
        currentClient = defaultClient;
    }

    public HttpClient getCurrentClient() {
        return currentClient;
    }

    public HttpSteps changeCurrentHttpClientSettings(HttpClient.Builder clientBuilder) {
        currentClient = clientBuilder.build();
        return this;
    }

    public HttpSteps resetHttpClient() {
        currentClient = defaultClient;
        return this;
    }

    @Override
    public void refresh() {
        resetHttpClient();
        currentClient.cookieHandler().ifPresent(cookieHandler ->
                ((CookieManager) cookieHandler).getCookieStore().removeAll());
    }
}
