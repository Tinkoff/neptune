package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;

import java.net.CookieManager;
import java.net.http.HttpClient;

@CreateWith(provider = HttpStepsParameterProvider.class)
public class HttpStepContext implements GetStepContext<HttpStepContext>, ActionStepContext<HttpStepContext>, Refreshable {

    private final HttpClient defaultClient;
    private HttpClient currentClient;

    public HttpStepContext(HttpClient.Builder clientBuilder) {
        this.defaultClient = clientBuilder.build();
        currentClient = defaultClient;
    }

    public HttpClient getCurrentClient() {
        return currentClient;
    }

    public HttpStepContext changeCurrentHttpClientSettings(HttpClient.Builder clientBuilder) {
        currentClient = clientBuilder.build();
        return this;
    }

    public HttpStepContext resetHttpClient() {
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
