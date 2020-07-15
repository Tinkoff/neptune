package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter;

import java.net.URI;
import java.util.List;

import static java.lang.System.getProperties;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryMatcher.uriHasQuery;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.GET;

public class QueryMappingTest {

    private final static URI TEST_URI = URI.create("http://127.0.0.1:8089");

    private static Object[][] prepareDataForQueryMapping(QueryMapping someMappedAPI) {
        return new Object[][]{
                {someMappedAPI.getSomethingWithQuery("val1", 3, "Hello world", true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPath("val1", 3, "Hello world", true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},


                {someMappedAPI.getSomethingWithQuery(of("val1", 3, "Hello world"), true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPath(of("val1", 3, "Hello world"), true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQuery(new Object[]{"val1", 3, "Hello world"}, true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPath(new Object[]{"val1", 3, "Hello world"}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQuery(new int[]{1, 2, 3}, true),
                        equalTo("/"),
                        equalTo("param1=1&param1=2&param1=3&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPath(new int[]{1, 2, 3}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1&param1=2&param1=3&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathSNE(of(1, "Кирилица", false, of("Hello world", "АБВ", 1, false)), true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1,%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%86%D0%B0,false,Hello+world,%D0%90%D0%91%D0%92,1,false&param2=true")},
        };
    }

    @DataProvider
    public Object[][] data1() {
        return prepareDataForQueryMapping(createAPI(QueryMapping.class, TEST_URI));
    }

    @DataProvider
    public Object[][] data2() {
        try {
            DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.accept("http://127.0.0.1:8089");
            return prepareDataForQueryMapping(createAPI(QueryMapping.class));
        } finally {
            getProperties().remove(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getPropertyName());
        }
    }

    @Test(dataProvider = "data1")
    public void test1(RequestBuilder builder, Matcher<String> pathMatcher, Matcher<String> queryMather) {
        var uri = builder.build().uri();
        assertThat(uri, uriHasScheme("http"));
        assertThat(uri, uriHasHost("127.0.0.1"));
        assertThat(uri, uriHasPort(8089));
        assertThat(uri, uriHasPath(pathMatcher));
        assertThat(uri, uriHasQuery(queryMather));
    }

    @Test(dataProvider = "data2")
    public void test2(RequestBuilder builder, Matcher<String> pathMatcher, Matcher<String> queryMather) {
        test1(builder, pathMatcher, queryMather);
    }

    private interface QueryMapping extends HttpAPI<QueryMapping> {

        @HttpMethod(httpMethod = GET)
        RequestBuilder getSomethingWithQuery(@QueryParameter(name = "param1") String value1,
                                             @QueryParameter(name = "param1") int value2,
                                             @QueryParameter(name = "param1") String value3,
                                             @QueryParameter(name = "param2") Boolean value4);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPath(@QueryParameter(name = "param1") String value1,
                                                    @QueryParameter(name = "param1") int value2,
                                                    @QueryParameter(name = "param1") String value3,
                                                    @QueryParameter(name = "param2") Boolean value4);

        @HttpMethod(httpMethod = GET)
        RequestBuilder getSomethingWithQuery(@QueryParameter(name = "param1") List<Object> param1,
                                             @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPath(@QueryParameter(name = "param1") List<Object> param1,
                                                    @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        RequestBuilder getSomethingWithQuery(@QueryParameter(name = "param1") Object[] param1,
                                             @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPath(@QueryParameter(name = "param1") Object[] param1,
                                                    @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        RequestBuilder getSomethingWithQuery(@QueryParameter(name = "param1") int[] param1,
                                             @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPath(@QueryParameter(name = "param1") int[] param1,
                                                    @QueryParameter(name = "param2") Boolean param2);


        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathSNE(@QueryParameter(name = "param1", explode = false) List<Object> param1,
                                                       @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathSE(@QueryParameter(name = "param1") Object param1,
                                                      @QueryParameter(name = "param2") Boolean param2);
    }
}
