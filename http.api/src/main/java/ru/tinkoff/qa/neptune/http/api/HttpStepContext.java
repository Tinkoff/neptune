package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;

import java.net.CookieManager;
import java.net.http.HttpClient;

@CreateWith(provider = HttpStepsParameterProvider.class)
public class HttpStepContext implements GetStepContext<HttpStepContext>, ActionStepContext<HttpStepContext>, Refreshable {

    private final HttpClient client;

    public HttpStepContext(HttpClient.Builder clientBuilder) {
        this.client = clientBuilder.build();
    }

    public HttpClient getCurrentClient() {
        return client;
    }

    @Override
    public void refresh() {
        client.cookieHandler().ifPresent(cookieHandler ->
                ((CookieManager) cookieHandler).getCookieStore().removeAll());
    }
}
