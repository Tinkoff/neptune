package ru.tinkoff.qa.neptune.http.api.service.mapping;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.Header.HeaderReader.readHeaders;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter.HeaderParameterReader.readHeaderParameters;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter.QueryParameterReader.readQueryParameters;

class ProxyRequestTuner implements RequestTuner {

    private final Method method;
    private final Object[] invocationParams;

    ProxyRequestTuner(Method method, Object[] invocationParams) {
        this.method = method;
        this.invocationParams = invocationParams;
    }

    @Override
    public void setUp(RequestSettings<?> requestSettings) {
        var headers = readHeaders(method);
        var queryParams = readQueryParameters(method, invocationParams);
        var headerParams = readHeaderParameters(method, invocationParams);

        ofNullable(headers).ifPresent(m -> m
                .forEach((s, strings) -> requestSettings.header(s, strings.toArray(new String[]{})))
        );

        ofNullable(queryParams).ifPresent(objects ->
                objects.forEach(o -> requestSettings.queryParam(o.getName(), o.isToExpand(), o.getParams())));

        ofNullable(headerParams).ifPresent(m -> m
                .forEach((s, strings) -> requestSettings.header(s, strings.toArray(new String[]{})))
        );
    }
}
