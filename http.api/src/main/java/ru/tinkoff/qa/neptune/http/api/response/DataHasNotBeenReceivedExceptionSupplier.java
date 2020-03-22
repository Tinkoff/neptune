package ru.tinkoff.qa.neptune.http.api.response;

import java.net.http.HttpResponse;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

final class DataHasNotBeenReceivedExceptionSupplier implements Supplier<RuntimeException> {

    private final String text;
    private final ForResponseBodyFunction<?, ?> forResponse;

    DataHasNotBeenReceivedExceptionSupplier(String text, ForResponseBodyFunction<?, ?> forResponse) {
        checkArgument(isNotBlank(text), "Exception text should not be blank");
        this.text = text;
        this.forResponse = forResponse;
    }

    private static String buildResponseDescription(HttpResponse<?> response) {
        var descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("- response status code: ").append(response.statusCode()).append("\n");
        descriptionBuilder.append("- response URI: ").append(response.uri()).append("\n");
        descriptionBuilder.append("- response body: ").append(ofNullable(response.body())
                .map(String::valueOf).orElse("<no body> or body was not read as expected"))
                .append("\n");
        ofNullable(response.headers()).ifPresent(httpHeaders -> {
            var headerMap = httpHeaders.map();
            if (headerMap.size() > 0) {
                descriptionBuilder.append("- response headers: ").append(headerMap).append("\n");
            }
        });

        response.previousResponse().ifPresent(response1 -> {
            descriptionBuilder.append("- previous response: ").append(response1).append("\n");
        });

        descriptionBuilder.append("- request: ").append(response.request()).append("\n");
        return descriptionBuilder.toString();
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
                    new DesiredResponseHasNotBeenReceivedException(format("Desired response has not been received. The last received: \n%s",
                            buildResponseDescription(lastReceived))));
        }

        return new DesiredDataHasNotBeenReceivedException(format("%s. The received response: \n%s", text,
                buildResponseDescription(validResponse)));
    }
}
