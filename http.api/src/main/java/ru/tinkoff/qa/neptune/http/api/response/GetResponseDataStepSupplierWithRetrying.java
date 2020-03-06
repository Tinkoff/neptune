package ru.tinkoff.qa.neptune.http.api.response;

public final class GetResponseDataStepSupplierWithRetrying<T, R> extends GetResponseDataStepSupplier<T, R, GetResponseDataStepSupplierWithRetrying<T, R>>  {

    private GetResponseDataStepSupplierWithRetrying(String description) {
        super(description);
    }
}
