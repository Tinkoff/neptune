package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.retrofit2.steps.StepInterceptor.getCurrentInterceptor;

class GetStepResultFunction<M, R> implements Function<Supplier<M>, RequestExecutionResult<M, R>> {

    private final StepInterceptor interceptor;
    private final Function<M, R> f;
    private final Predicate<? super R> predicate;

    GetStepResultFunction(Function<M, R> f, Predicate<? super R> predicate) {
        this.predicate = predicate;
        checkNotNull(f);
        this.interceptor = getCurrentInterceptor();
        this.f = f;
    }

    @Override
    public RequestExecutionResult<M, R> apply(Supplier<M> t) {
        interceptor.eraseRequest();

        var m = t.get();

        var req = interceptor.getRequest();
        if (req == null) {
            throw new NoRequestWasSentError();
        }

        R result = f.apply(m);
        if (ofNullable(predicate)
                .map(p -> p.test(result))
                .orElse(true)) {
            var response = interceptor.getLastResponse();
            return new RequestExecutionResult<>(response, m, result);
        } else {
            return null;
        }
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
