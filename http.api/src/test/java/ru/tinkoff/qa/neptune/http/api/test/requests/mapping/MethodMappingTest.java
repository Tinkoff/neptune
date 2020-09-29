package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;

import java.net.URI;

import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.*;

public class MethodMappingTest {

    private final static URI TEST_URI = URI.create("http://127.0.0.1:8089");

    private static Object[][] prepareDataForMethodMapping(MethodMapping someMappedAPI) {
        return new Object[][]{
                {someMappedAPI.postSomething(), "POST", true},
                {someMappedAPI.getSomething(), "GET", false},
                {someMappedAPI.putSomething(), "PUT", true},
                {someMappedAPI.deleteSomething(), "DELETE", false},
                {someMappedAPI.patchSomething(), "PATCH", true},
                {someMappedAPI.headSomething(), "HEAD", true},
                {someMappedAPI.optionsSomething(), "OPTIONS", true},
                {someMappedAPI.traceSomething(), "TRACE", true},
                {someMappedAPI.customMethod(), "CUSTOM_METHOD", true},
        };
    }

    @DataProvider
    public static Object[][] data1() {
        return prepareDataForMethodMapping(createAPI(MethodMapping.class, TEST_URI));
    }

    @DataProvider
    public static Object[][] data2() {
        try {
            DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.accept("http://127.0.0.1:8089");
            return prepareDataForMethodMapping(createAPI(MethodMapping.class));
        } finally {
            getProperties().remove(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getPropertyName());
        }
    }

    @Test(dataProvider = "data1")
    public void test1(RequestBuilder builder, String method, boolean isBodyPresent) {
        var r = builder.build();
        assertThat(r.method(), is(method));
        assertThat(r.uri(), is(URI.create("http://127.0.0.1:8089")));

        var r2 = (NeptuneHttpRequestImpl) r;
        var body = r2.body();
        if (isBodyPresent) {
            assertThat(body, instanceOf(RequestBody.class));
            assertThat(body.body(), nullValue());
        } else {
            assertThat(body, nullValue());
        }
    }

    @Test(dataProvider = "data2")
    public void test2(RequestBuilder builder, String method, boolean isBodyPresent) {
        test1(builder, method, isBodyPresent);
    }

    private interface MethodMapping extends HttpAPI<MethodMapping> {

        @HttpMethod(httpMethod = POST)
        RequestBuilder postSomething();

        @HttpMethod(httpMethod = GET)
        RequestBuilder getSomething();

        @HttpMethod(httpMethod = PUT)
        RequestBuilder putSomething();

        @HttpMethod(httpMethod = DELETE)
        RequestBuilder deleteSomething();

        @HttpMethod(httpMethod = PATCH)
        RequestBuilder patchSomething();

        @HttpMethod(httpMethod = HEAD)
        RequestBuilder headSomething();

        @HttpMethod(httpMethod = OPTIONS)
        RequestBuilder optionsSomething();

        @HttpMethod(httpMethod = TRACE)
        RequestBuilder traceSomething();

        @HttpMethod(httpMethodStr = "CUSTOM_METHOD")
        RequestBuilder customMethod();
    }
}
