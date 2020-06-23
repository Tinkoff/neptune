package ru.tinkoff.qa.neptune.http.api.service.mapping;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

import java.lang.reflect.Method;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.Header.HeaderReader.readHeaders;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.QueryParameter.QueryParameterReader.readQueryParameters;

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

        headers.forEach((s, strings) -> requestSettings.header(s, strings.toArray(new String[]{})));
        queryParams.forEach((s, objects) -> requestSettings.queryParam(s, objects.toArray()));
    }
}
