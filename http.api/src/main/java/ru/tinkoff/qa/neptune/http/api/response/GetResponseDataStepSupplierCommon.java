package ru.tinkoff.qa.neptune.http.api.response;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;

public final class GetResponseDataStepSupplierCommon<T, R> extends GetResponseDataStepSupplier<T, R, GetResponseDataStepSupplierCommon<T, R>> {

    GetResponseDataStepSupplierCommon(String description) {
        super(description);
    }

    @Override
    public Function<T, R> get() {
        checkArgument(nonNull(getFrom()), "FROM-object is not defined");
        var composeWith = preparePreFunction();
        var endFunction = getEndFunction();
        checkNotNull(endFunction);
        return endFunction.compose(composeWith);
    }
}
