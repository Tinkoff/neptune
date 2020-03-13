package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

/**
 * Builds a step-function that receives http response
 *
 * @param <T> is a type of response body
 */
@MakeStringCapturesOnFinishing
@MakeFileCapturesOnFinishing
public class ResponseSequentialGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<HttpStepContext, HttpResponse<T>,
        ResponseSequentialGetSupplier<T>> {

    private ResponseSequentialGetSupplier(RequestBuilder requestBuilder, HttpResponse.BodyHandler<T> bodyHandler) {
        super(format("Response of [%s]", requestBuilder), httpStepContext -> {
            try {
                checkArgument(nonNull(requestBuilder), "Http request should not be null");
                checkArgument(nonNull(bodyHandler), "Http response body handler should not be null");
                return httpStepContext.getCurrentClient().send(requestBuilder.build(), bodyHandler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Creates an instance that builds a step-function to send an http request and to receive a response
     * with body.
     *
     * @param requestBuilder is a builder of an http request
     * @param bodyHandler    of a response body
     * @param <T>            is a type of response body
     * @return an instance of {@link ResponseSequentialGetSupplier}
     */
    public static <T> ResponseSequentialGetSupplier<T> response(RequestBuilder requestBuilder, HttpResponse.BodyHandler<T> bodyHandler) {
        return new ResponseSequentialGetSupplier<>(requestBuilder, bodyHandler);
    }

    @Override
    protected ResponseSequentialGetSupplier<T> criteria(String description, Predicate<? super HttpResponse<T>> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected ResponseSequentialGetSupplier<T> criteria(Criteria<? super HttpResponse<T>> criteria) {
        return super.criteria(criteria);
    }
}
