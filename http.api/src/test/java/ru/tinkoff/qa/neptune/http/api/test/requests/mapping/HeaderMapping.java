package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.Header;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter;

import java.util.Map;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public interface HeaderMapping extends HttpAPI<HeaderMapping> {
    @Header(name = "header1", headerValues = {"abc", "one more value"})
    @Header(name = "header2", headerValues = "one more value")
    @Header(name = "header3", headerValues = "one more value again")
    @HttpMethod(httpMethod = POST)
    RequestBuilder<?> postSomethingWithHeaders();

    @Header(name = "header1", headerValues = {"abc"})
    @Header(name = "header2", headerValues = "one more value")
    @Header(name = "header3", headerValues = "one more value again")
    @HttpMethod(httpMethod = POST)
    RequestBuilder<?> postSomethingWithHeaders(@HeaderParameter(headerName = "header1") String header1,
                                               @HeaderParameter(headerName = "digitHeader") int digitHeader,
                                               @HeaderParameter(headerName = "digitHeader") Integer digitHeader2,
                                               @HeaderParameter(headerName = "digitHeader") Float digitHeader3,
                                               @HeaderParameter(headerName = "arrayHeader") Object[] array,
                                               @HeaderParameter(headerName = "iterable") Iterable<Object> iterable,
                                               @HeaderParameter(headerName = "simpleMap") Map<?, ?> map1,
                                               @HeaderParameter(headerName = "explodedMap", explode = true) Map<?, ?> map2,
                                               @HeaderParameter(headerName = "simpleObject") HeaderParameterObject o1,
                                               @HeaderParameter(headerName = "explodedObject", explode = true) HeaderParameterObject o2);

    @Header(name = "header1", headerValues = {"abc", "one more value"})
    @Header(name = "header2", headerValues = "one more value")
    @Header(name = "header3", headerValues = "one more value again")
    @HttpMethod(httpMethod = POST)
    RequestBuilder<?> postSomethingWithHeaders(@HeaderParameter(headerName = "notRequired1") Object notRequired1,
                                               @HeaderParameter(headerName = "notRequired2") Object notRequired2);

    @HttpMethod(httpMethod = POST)
    RequestBuilder<?> postSomethingWithHeaders(@HeaderParameter(headerName = "required", required = true) Object requiredHeader);

    default RequestBuilder<?> postSomethingWithHeadersDefault() {
        return postSomethingWithHeaders(5);
    }
}
