package ru.tinkoff.qa.neptune.retrofit2;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

import static java.util.Optional.ofNullable;

public final class StepInterceptor implements Interceptor {

    private static final ThreadLocal<StepInterceptor> INTERCEPTOR_THREAD_LOCAL = new ThreadLocal<>();

    private Request request;
    private Response lastResponse;
    private ResponseBody body;

    private StepInterceptor() {
        super();
    }

    public static StepInterceptor getCurrentInterceptor() {
        return ofNullable(INTERCEPTOR_THREAD_LOCAL.get()).orElseGet(() -> {
            var interceptor = new StepInterceptor();
            INTERCEPTOR_THREAD_LOCAL.set(interceptor);
            return interceptor;
        });
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        lastResponse = null;
        body = null;
        request = ofNullable(request).orElseGet(chain::request);
        lastResponse = chain.proceed(chain.request());
        body = lastResponse.peekBody(Long.MAX_VALUE);
        return lastResponse;
    }

    public Request getRequest() {
        return request;
    }

    public void eraseRequest() {
        this.request = null;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public ResponseBody getBody() {
        return body;
    }
}
