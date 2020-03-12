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
    private Function<HttpStepContext, HttpResponse<R>> getResponse;

    ForResponseBodyFunction(Function<R, T> getFromBody) {
        checkNotNull(getFromBody);
        this.getFromBody = getFromBody;
    }

    @Override
    public T apply(HttpStepContext httpStepContext) {
        checkArgument(nonNull(getResponse), "How to get response is not defined");
        var r =  getResponse.apply(httpStepContext);
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

    ForResponseBodyFunction<R, T> setResponseFunc(Function<HttpStepContext, HttpResponse<R>> getResponse) {
        this.getResponse = getResponse;
        return this;
    }
}
