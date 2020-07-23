package ru.tinkoff.qa.neptune.http.api.request.body.url.encoded;

import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.net.http.HttpRequest;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.util.Arrays.stream;

public final class URLEncodedForm extends RequestBody<String> {

    private URLEncodedForm(URLEncodedFormBuilder builder) {
        super(builder.buildForm());
    }

    public URLEncodedForm(FormParameter... formParameters) {
        this(createBuilder(formParameters));
    }

    private static URLEncodedFormBuilder createBuilder(FormParameter... formParameters) {
        var builder = new URLEncodedFormBuilder();
        checkNotNull(formParameters);
        checkArgument(formParameters.length > 0, "Should be defined at least one parameter.");
        stream(formParameters).forEach(formParameter -> {
            if (formParameter.isToNotEncodeValue()) {
                builder.addParameter(formParameter.getName(), formParameter.getValues()[0]);
            } else {
                builder.addParameter(formParameter.getName(),
                        formParameter.isToExpand(),
                        formParameter.getDelimiter(),
                        formParameter.isAllowReserved(),
                        formParameter.getValues());
            }
        });

        return builder;
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofString(super.body());
    }

    @Override
    public String toString() {
        return body();
    }
}
