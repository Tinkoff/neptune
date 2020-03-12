package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.ResponseHasNoDesiredDataException;

import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public abstract class GetResponseDataStepSupplier<R, T, S extends GetResponseDataStepSupplier<R, T, S>> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HttpStepContext, R, R, S> {

    private SequentialGetStepSupplier<HttpStepContext, ? extends R, ?, ?, ?> from;
    Object responseFunction;

    private GetResponseDataStepSupplier(String description) {
        super(description, r -> r);
    }

    private static <T> void addHowToGetResponse(SequentialGetStepSupplier<HttpStepContext, ?, ?, ?, ?> from,
                                                Function<HttpStepContext, HttpResponse<T>> f) {
        var clazz = from.getClass();

        if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromArrayBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponseFunc(f);
            return;
        }

        if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponseFunc(f);
            return;
        }

        if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromIterableBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponseFunc(f);
            return;
        }

        if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectsFromArrayBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponseFunc(f);
            return;
        }

        if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectsFromIterableBodyStepSupplier<T, ?, ?>) from).getOriginalFunction().setResponseFunc(f);
        }
    }

    @Override
    protected S from(SequentialGetStepSupplier<HttpStepContext, ? extends R, ?, ?, ?> from) {
        super.from(from);
        this.from = from;
        return (S) this;
    }

    S fromResponse(Function<HttpStepContext, HttpResponse<T>> getResponse) {
        this.responseFunction = getResponse;
        return (S) this;
    }

    S fromResponse(ResponseSequentialGetSupplier<T> getResponse) {
        this.responseFunction = getResponse;
        return (S) this;
    }

    public S throwWhenNothing(String exceptionMessage) {
        ofNullable(from).ifPresent(s -> {
            var toThrow = ((Supplier<ResponseHasNoDesiredDataException>) () -> new ResponseHasNoDesiredDataException(exceptionMessage));
            var clazz = s.getClass();

            if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromArrayBodyStepSupplier<?, ?>) s).throwOnEmptyResult(toThrow);
                return;
            }

            if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromBodyStepSupplier<?, ?>) s).throwOnEmptyResult(toThrow);
                return;
            }

            if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromIterableBodyStepSupplier<?, ?>) s).throwOnEmptyResult(toThrow);
                return;
            }

            if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectsFromArrayBodyStepSupplier<?, ?>) s).throwOnEmptyResult(toThrow);
                return;
            }

            if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectsFromIterableBodyStepSupplier<?, ?, ?>) s).throwOnEmptyResult(toThrow);
            }
        });
        return (S) this;
    }

    @Override
    public Function<HttpStepContext, R> get() {
        checkArgument(nonNull(from), "It is not defined how to get data from http response");
        checkArgument(nonNull(responseFunction), "Response to be received is not defined");

        var clazz = responseFunction.getClass();
        Function<HttpStepContext, HttpResponse<T>> f;
        if (Function.class.isAssignableFrom(clazz)) {
            f = (Function<HttpStepContext, HttpResponse<T>>) responseFunction;
        } else {
            f = ((StepFunction<HttpStepContext, HttpResponse<T>>) ((ResponseSequentialGetSupplier<T>) responseFunction).get())
                    .turnReportingOff();
        }

        addHowToGetResponse(from, f);
        return super.get();
    }

    public static class Common<R, T> extends GetResponseDataStepSupplier<R, T, Common<R, T>> {
        private Common(String description) {
            super(description);
        }
    }
}
