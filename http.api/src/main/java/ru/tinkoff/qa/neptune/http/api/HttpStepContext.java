package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.response.GetResponseDataStepSupplier;

import java.net.CookieManager;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.net.http.HttpResponse.BodyHandlers.discarding;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

@CreateWith(provider = HttpStepsParameterProvider.class)
public class HttpStepContext extends Context<HttpStepContext> implements ContextRefreshable {

    private static final HttpStepContext context = getInstance(HttpStepContext.class);

    private final HttpClient client;

    public HttpStepContext(HttpClient.Builder clientBuilder) {
        this.client = clientBuilder.build();
    }

    public static HttpStepContext http() {
        return context;
    }

    public HttpClient getCurrentClient() {
        return client;
    }

    public <T> HttpResponse<T> responseOf(RequestBuilder requestBuilder, HttpResponse.BodyHandler<T> bodyHandler) {
        return response(requestBuilder, bodyHandler).get().apply(this);
    }

    public HttpResponse<Void> responseOf(RequestBuilder requestBuilder) {
        return responseOf(requestBuilder, discarding());
    }

    public <T> T get(GetResponseDataStepSupplier<T, ?, ?, ?> responseBody) {
        return responseBody.get().apply(this);
    }

    @Override
    public void refreshContext() {
        client.cookieHandler().ifPresent(cookieHandler ->
                ((CookieManager) cookieHandler).getCookieStore().removeAll());
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA", forRemoval = true)
    public <T> T get(Function<HttpStepContext, T> function) {
        checkArgument(Objects.nonNull(function), "The function is not defined");
        return function.apply(this);
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA", forRemoval = true)
    public <T> T get(Supplier<Function<HttpStepContext, T>> functionSupplier) {
        checkNotNull(functionSupplier, "Supplier of the value to get was not defined");
        return this.get(functionSupplier.get());
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA", forRemoval = true)
    public HttpStepContext perform(Consumer<HttpStepContext> actionConsumer) {
        checkArgument(Objects.nonNull(actionConsumer), "Action is not defined");
        actionConsumer.accept(this);
        return this;
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA", forRemoval = true)
    public HttpStepContext perform(Supplier<Consumer<HttpStepContext>> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return this.perform(actionSupplier.get());
    }
}
