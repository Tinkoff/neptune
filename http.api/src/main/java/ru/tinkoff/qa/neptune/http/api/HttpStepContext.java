package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.steps.StepAction;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;

import java.net.CookieManager;
import java.net.http.HttpClient;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
    public HttpStepContext perform(StepAction<HttpStepContext> actionConsumer) {
        checkArgument(Objects.nonNull(actionConsumer), "Action is not defined");
        actionConsumer.accept(this);
        return this;
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA", forRemoval = true)
    public HttpStepContext perform(Supplier<StepAction<HttpStepContext>> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return this.perform(actionSupplier.get());
    }
}
