package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.ResponseHasNoDesiredDataException;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public abstract class GetResponseDataStepSupplier<T, R, S extends GetResponseDataStepSupplier<T, R, S>> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, R, R, S> {

    private SequentialGetStepSupplier<T, ? extends R, ?, ?, ?> from;

    GetResponseDataStepSupplier(String description) {
        super(description, r -> r);
    }

    @Override
    protected S from(SequentialGetStepSupplier<T, ? extends R, ?, ?, ?> from) {
        super.from(from);
        this.from = from;
        return (S) this;
    }

    public static <T, R> GetResponseDataStepSupplierCommon<HttpStepContext, T>
    responseData(RequestBuilder requestBuilder,
                 HttpResponse.BodyHandler<R> bodyHandler,
                 SequentialGetStepSupplier<HttpResponse<R>, T, ?, ?, ?> whatToGet) {
        checkArgument(nonNull(requestBuilder), "Http request should not be null");
        checkArgument(nonNull(bodyHandler), "Http response body value should not be null");
        checkArgument(nonNull(whatToGet), "Value that describes what to get from http response body " +
                "should not be null");
        var supplier = new GetResponseDataStepSupplierCommon<>(whatToGet.toString());
        return supplier.from(whatToGet);
    }

    public S throwWhenNothing(String exceptionMessage) {
        ofNullable(from).ifPresent(tSequentialGetStepSupplier -> {
            var clazz = tSequentialGetStepSupplier.getClass();
            try {
                var m = clazz.getDeclaredMethod("throwOnEmptyResult", Supplier.class);
                m.setAccessible(true);
                m.invoke(tSequentialGetStepSupplier,
                        (Supplier<ResponseHasNoDesiredDataException>) () -> new ResponseHasNoDesiredDataException(exceptionMessage));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        return (S) this;
    }

    SequentialGetStepSupplier<T, ? extends R, ?, ?, ?> getFrom() {
        return from;
    }
}
