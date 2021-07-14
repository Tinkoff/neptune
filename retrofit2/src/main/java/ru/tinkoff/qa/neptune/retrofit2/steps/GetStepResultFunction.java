package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;
import ru.tinkoff.qa.neptune.retrofit2.StepInterceptor;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.retrofit2.StepInterceptor.getCurrentInterceptor;

class GetStepResultFunction<T, R> implements Function<RetrofitContext<T>, RequestExecutionResult<R>> {

    private final StepInterceptor interceptor;
    private final Function<T, R> f;

    GetStepResultFunction(Function<T, R> f) {
        checkNotNull(f);
        this.interceptor = getCurrentInterceptor();
        this.f = f;
    }

    @Override
    public RequestExecutionResult<R> apply(RetrofitContext<T> t) {
        interceptor.eraseRequest();
        var result = f.apply(t.getService());
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
