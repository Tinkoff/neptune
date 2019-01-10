package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.performer.ActionStepPerformer;
import ru.tinkoff.qa.neptune.core.api.steps.performer.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.performer.GetStepPerformer;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;

import java.net.CookieManager;
import java.net.http.HttpClient;

@CreateWith(provider = HttpStepsParameterProvider.class)
public class HttpStepPerformer implements GetStepPerformer<HttpStepPerformer>, ActionStepPerformer<HttpStepPerformer>, Refreshable {

    private final HttpClient defaultClient;
    private HttpClient currentClient;

    public HttpStepPerformer(HttpClient.Builder clientBuilder) {
        this.defaultClient = clientBuilder.build();
        currentClient = defaultClient;
    }

    public HttpClient getCurrentClient() {
        return currentClient;
    }

    public HttpStepPerformer changeCurrentHttpClientSettings(HttpClient.Builder clientBuilder) {
        currentClient = clientBuilder.build();
        return this;
    }

    public HttpStepPerformer resetHttpClient() {
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
