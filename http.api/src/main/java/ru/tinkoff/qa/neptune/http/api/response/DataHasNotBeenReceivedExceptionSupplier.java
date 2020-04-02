package ru.tinkoff.qa.neptune.http.api.response;

import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

final class DataHasNotBeenReceivedExceptionSupplier implements Supplier<RuntimeException> {

    private final String text;
    private final ForResponseBodyFunction<?, ?> forResponse;

    DataHasNotBeenReceivedExceptionSupplier(String text, ForResponseBodyFunction<?, ?> forResponse) {
        checkArgument(isNotBlank(text), "Exception text should not be blank");
        this.text = text;
        this.forResponse = forResponse;
    }

    @Override
    public RuntimeException get() {
        var validResponse = forResponse.getLastValidResponse();
        if (validResponse == null) {
            var lastReceived = forResponse.getLastReceivedResponse();
            if (lastReceived == null) {
                return new DesiredDataHasNotBeenReceivedException(text,
                        new DesiredResponseHasNotBeenReceivedException("No response has not been received for some reason"));
            }

            return new DesiredDataHasNotBeenReceivedException(text,
                    new DesiredResponseHasNotBeenReceivedException("A response has been received bit it doesn't meet expectations"));
        }

        return new DesiredDataHasNotBeenReceivedException(text);
    }
}
