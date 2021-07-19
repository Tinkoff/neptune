package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.retrofit2.steps.StepInterceptor.getCurrentInterceptor;

class GetStepResultFunction<M, R> implements Function<Supplier<M>, RequestExecutionResult<R>> {

    private final StepInterceptor interceptor;
    private final Function<M, R> f;

    GetStepResultFunction(Function<M, R> f) {
        checkNotNull(f);
        this.interceptor = getCurrentInterceptor();
        this.f = f;
    }

    @Override
    public RequestExecutionResult<R> apply(Supplier<M> t) {
        interceptor.eraseRequest();
        var m = t.get();
        var result = f.apply(m);
        var req = interceptor.getRequest();
        if (req == null) {
            throw new NoRequestWasSentError();
        }

        var response = interceptor.getLastResponse();
        return new RequestExecutionResult<>(response, result);
    }

    Request request() {
        return interceptor.getRequest();
    }

    Response response() {
        return interceptor.getLastResponse();
    }

    ResponseBody body() {
        return interceptor.getBody();
    }
}
