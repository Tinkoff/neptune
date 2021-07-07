package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.net.http.HttpResponse;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.turnReportingOff;

final class ReceiveResponseAndGetResultFunction<T, R> implements Function<HttpStepContext, R> {

    private final ResponseSequentialGetSupplier<T> getResponse;
    private final Function<T, R> endFunction;

    ReceiveResponseAndGetResultFunction(Function<T, R> endFunction, ResponseSequentialGetSupplier<T> getResponse) {
        checkNotNull(getResponse);
        checkNotNull(endFunction);
        this.getResponse = turnReportingOff(getResponse.clone()
                .addIgnored(Exception.class)
                .throwOnNoResult());
        this.endFunction = endFunction;
    }

    @Override
    public R apply(HttpStepContext httpStepContext) {
        HttpResponse<T> response;
        response = getResponse.get().apply(httpStepContext);

        if (response == null) {
            return null;
        }

        var body = response.body();
        return ofNullable(body)
                .map(t -> {
                    try {
                        return endFunction.apply(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .orElse(null);
    }

    ResponseSequentialGetSupplier<T> getGetResponseSupplier() {
        return getResponse;
    }
}
