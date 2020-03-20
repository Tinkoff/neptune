package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.net.http.HttpResponse;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;

final class ForResponseBodyFunction<R, T> implements Function<HttpStepContext, T> {

    final Function<R, T> getFromBody;
    private Object response;

    ForResponseBodyFunction(Function<R, T> getFromBody) {
        checkNotNull(getFromBody);
        this.getFromBody = getFromBody;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T apply(HttpStepContext httpStepContext) {
        checkArgument(nonNull(response), "How to get response is not defined");
        HttpResponse<R> r;

        if (response instanceof HttpResponse) {
            r = (HttpResponse<R>) response;
        }
        else {
            r = ((StepFunction<HttpStepContext, HttpResponse<R>>) ((ResponseSequentialGetSupplier<R>) response).get())
                    .turnReportingOff()
                    .apply(httpStepContext);
        }

        if (r == null) {
            return null;
        }

        try {
            return getFromBody.apply(r.body());
        }
        catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    ForResponseBodyFunction<R, T> setResponse(HttpResponse<R> response) {
        this.response = response;
        return this;
    }

    ForResponseBodyFunction<R, T> setResponse(ResponseSequentialGetSupplier<R> howToGetResponse) {
        this.response = howToGetResponse;
        return this;
    }
}
