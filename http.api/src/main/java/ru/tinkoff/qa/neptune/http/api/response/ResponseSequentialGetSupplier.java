package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.util.function.Predicate;

import static java.lang.String.format;

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
        super(format("Response of [%s]", requestBuilder),
                new ForResponseFunction<>(requestBuilder, bodyHandler));
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

    @Override
    protected String prepareStepDescription() {
        return super.prepareStepDescription();
    }

    protected ResponseSequentialGetSupplier<T> clone() {
        return super.clone();
    }

    @Override
    protected ForResponseFunction<T> getOriginalFunction() {
        return (ForResponseFunction<T>) super.getOriginalFunction();
    }
}
