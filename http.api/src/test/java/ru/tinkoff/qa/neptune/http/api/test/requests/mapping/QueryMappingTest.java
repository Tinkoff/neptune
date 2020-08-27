package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormParam;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.getProperties;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryMatcher.uriHasQuery;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.GET;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.*;

public class QueryMappingTest {

    private final static URI TEST_URI = URI.create("http://127.0.0.1:8089");

    private final static Map<String, Object> LOW_LEVEL_MAP = new LinkedHashMap<>() {
        {
            put("someNum3", 3);
            put("someString3", "string3=");
            put("someArray3", new Object[]{1, 2, 3, 3});
        }
    };

    private final static Map<String, Object> MID_LEVEL_MAP = new LinkedHashMap<>() {
        {
            put("someNum2", 2);
            put("someString2", "string2");
            put("someArray2", new Object[]{1, 2, 3, 3});
            put("nested2", LOW_LEVEL_MAP);
        }
    };

    private final static Map<String, Object> MID_LEVEL_MAP2 = new LinkedHashMap<>() {
        {
            put("someNum4", 4);
            put("someArray4", new Object[]{1, 2, 3, 3});
            put("someString4", "string4$/=");
            put("nested4", LOW_LEVEL_MAP);
        }
    };

    private final static Map<String, Object> HIGH_LEVEL_MAP = new LinkedHashMap<>() {
        {
            put("someNumber", 1);
            put("someString", "string[1]");
            put("someArray", List.of("ABCD?", "EF:GH", "АБВГ Д&="));
            put("nested", MID_LEVEL_MAP);
            put("nestedNext", MID_LEVEL_MAP2);
        }
    };

    private final static QueryParameterObject3 LOW_LEVEL_NESTED_OBJECT = new QueryParameterObject3()
            .setSomeNum(3)
            .setSomeString("string3=")
            .setSomeArray(new Integer[]{1, 2, 3, 3});

    private final static QueryParameterObject QUERY_PARAMETER_OBJECT = new QueryParameterObject()
            .setSomeNum(1)
            .setSomeString("string[1]")
            .setSomeArray(of("ABCD?", "EF:GH", "АБВГ Д&="))
            .setNested(new QueryParameterObject2()
                    .setSomeNum(2)
                    .setSomeString("string2")
                    .setSomeArray(new Integer[]{1, 2, 3, 3})
                    .setNested(LOW_LEVEL_NESTED_OBJECT))
            .setNestedNext(new QueryParameterObject4()
                    .setSomeNum(4)
                    .setSomeString("string4$/=")
                    .setSomeArray(new Integer[]{1, 2, 3, 3})
                    .setNested(LOW_LEVEL_NESTED_OBJECT));

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

                {someMappedAPI.getSomethingWithQueryAndPathFNE(of(1, "Кирилица$@", false, of("Hello world:??", "АБВ", 1, false)), true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1,%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%86%D0%B0%24%40,false,Hello+world%3A%3F%3F,%D0%90%D0%91%D0%92,1,false&param2=true")},


                {someMappedAPI.getSomethingWithQueryAndPathFEAR(of(1, "Кирилица$@", false, of("Hello world:??", "АБВ", 1, false)), true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1&param1=%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%86%D0%B0$@&param1=false&param1=Hello+world:??,%D0%90%D0%91%D0%92,1,false&param2=true")},


                {someMappedAPI.getSomethingWithQueryAndPathFNEAR(of(1, "Кирилица$@", false, of("Hello world:??", "АБВ", 1, false)), true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1,%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%86%D0%B0$@,false,Hello+world:??,%D0%90%D0%91%D0%92,1,false&param2=true")},


                {someMappedAPI.getSomethingWithQueryAndPathFE(HIGH_LEVEL_MAP, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("someNumber=1&someString=string%5B1%5D&someArray=ABCD%3F,EF%3AGH,%D0%90%D0%91%D0%92%D0%93+%D0%94%26%3D" +
                                "&nested=someNum2,2,someString2,string2,someArray2,1,2,3,3" +
                                "&nestedNext=someNum4,4,someArray4,1,2,3,3,someString4,string4%24%2F%3D" +
                                "&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathFEAR(HIGH_LEVEL_MAP, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("someNumber=1&someString=string[1]&someArray=ABCD?,EF:GH,%D0%90%D0%91%D0%92%D0%93+%D0%94&=" +
                                "&nested=someNum2,2,someString2,string2,someArray2,1,2,3,3" +
                                "&nestedNext=someNum4,4,someArray4,1,2,3,3,someString4,string4$/=" +
                                "&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathFNE(HIGH_LEVEL_MAP, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=someNumber,1,someString,string%5B1%5D,someArray,ABCD%3F,EF%3AGH,%D0%90%D0%91%D0%92%D0%93+%D0%94%26%3D" +
                                "&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathFNEAR(HIGH_LEVEL_MAP, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=someNumber,1,someString,string[1],someArray,ABCD?,EF:GH,%D0%90%D0%91%D0%92%D0%93+%D0%94&=" +
                                "&param2=true")},


                {someMappedAPI.getSomethingWithQueryAndPathFE(QUERY_PARAMETER_OBJECT, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("someNumber=1&someString=string%5B1%5D&someArray=ABCD%3F,EF%3AGH,%D0%90%D0%91%D0%92%D0%93+%D0%94%26%3D" +
                                "&nested=someNum2,2,someString2,string2,someArray2,1,2,3,3" +
                                "&nestedNext=someNum4,4,someArray4,1,2,3,3,someString4,string4%24%2F%3D" +
                                "&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathFEAR(QUERY_PARAMETER_OBJECT, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("someNumber=1&someString=string[1]&someArray=ABCD?,EF:GH,%D0%90%D0%91%D0%92%D0%93+%D0%94&=" +
                                "&nested=someNum2,2,someString2,string2,someArray2,1,2,3,3" +
                                "&nestedNext=someNum4,4,someArray4,1,2,3,3,someString4,string4$/=" +
                                "&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathFNE(QUERY_PARAMETER_OBJECT, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=someNumber,1,someString,string%5B1%5D,someArray,ABCD%3F,EF%3AGH,%D0%90%D0%91%D0%92%D0%93+%D0%94%26%3D" +
                                "&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathFNEAR(QUERY_PARAMETER_OBJECT, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=someNumber,1,someString,string[1],someArray,ABCD?,EF:GH,%D0%90%D0%91%D0%92%D0%93+%D0%94&=" +
                                "&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathSNE(new Object[]{1, "Кирилица$@", false, of("Hello world:??", "АБВ", 1, false)}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1%20%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%86%D0%B0%24%40%20false%20Hello+world%3A%3F%3F,%D0%90%D0%91%D0%92,1,false&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathSNEAR(new Object[]{1, "Кирилица$@", false, of("Hello world:??", "АБВ", 1, false)}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1%20%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%86%D0%B0$@%20false%20Hello+world:??,%D0%90%D0%91%D0%92,1,false&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathPNE(new Object[]{1, "Кирилица$@", false, of("Hello world:??", "АБВ", 1, false)}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1%7C%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%86%D0%B0%24%40%7Cfalse%7CHello+world%3A%3F%3F,%D0%90%D0%91%D0%92,1,false&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathDE(HIGH_LEVEL_MAP, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1[someNumber]=1&param1[someString]=string%5B1%5D&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathDEAR(HIGH_LEVEL_MAP, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1[someNumber]=1&param1[someString]=string[1]&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathDE(QUERY_PARAMETER_OBJECT, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1[someNumber]=1&param1[someString]=string%5B1%5D&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPathDEAR(QUERY_PARAMETER_OBJECT, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1[someNumber]=1&param1[someString]=string[1]&param2=true")}
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

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = ".*['Query parameter 'param2' requires value that differs from null']")
    public void test3() {
        createAPI(QueryMapping.class, TEST_URI).getSomethingWithQueryRequired(null, null, null);
        fail("Exception was expected");
    }

    @Test
    public void test4() {
        var query = createAPI(QueryMapping.class, TEST_URI)
                .getSomethingWithQueryRequired(null, "My String", null)
                .build()
                .uri()
                .getQuery();

        assertThat(query, is("param2=My+String"));
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
        RequestBuilder getSomethingWithQueryAndPathFNE(@QueryParameter(name = "param1")
                                                       @FormParam(explode = false) List<Object> param1,
                                                       @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathFEAR(@QueryParameter(name = "param1")
                                                        @FormParam(allowReserved = true) List<Object> param1,
                                                        @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathFNEAR(@QueryParameter(name = "param1")
                                                         @FormParam(explode = false, allowReserved = true) List<Object> param1,
                                                         @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathFE(@QueryParameter(name = "param1") Object param1,
                                                      @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathFNE(@QueryParameter(name = "param1")
                                                       @FormParam(explode = false) Object param1,
                                                       @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathFEAR(@QueryParameter(name = "param1")
                                                        @FormParam(allowReserved = true) Object param1,
                                                        @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathFNEAR(@QueryParameter(name = "param1")
                                                         @FormParam(explode = false, allowReserved = true) Object param1,
                                                         @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathSNE(
                @QueryParameter(name = "param1")
                @FormParam(style = SPACE_DELIMITED, explode = false) Object[] param1,
                @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathSNEAR(
                @QueryParameter(name = "param1")
                @FormParam(style = SPACE_DELIMITED, explode = false, allowReserved = true) Object[] param1,
                @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathPNE(
                @QueryParameter(name = "param1")
                @FormParam(style = PIPE_DELIMITED, explode = false) Object[] param1,
                @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathDE(@QueryParameter(name = "param1")
                                                      @FormParam(style = DEEP_OBJECT) Object param1,
                                                      @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryAndPathDEAR(@QueryParameter(name = "param1")
                                                        @FormParam(style = DEEP_OBJECT, allowReserved = true) Object param1,
                                                        @QueryParameter(name = "param2") Boolean param2);

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithQueryRequired(@QueryParameter(name = "param1", required = false) Object param1,
                                                     @QueryParameter(name = "param2") Object param2,
                                                     @QueryParameter(name = "param2", required = false) Object param3);
    }

    @JsonPropertyOrder({"someNumber", "someString", "someArray", "nested", "nestedNext"})
    private static class QueryParameterObject extends MappedObject {

        @JsonProperty("someNumber")
        private Integer someNum;

        private String someString;

        private List<String> someArray;

        private QueryParameterObject2 nested;

        private QueryParameterObject4 nestedNext;


        public QueryParameterObject setSomeNum(Integer someNum) {
            this.someNum = someNum;
            return this;
        }

        public String getSomeString() {
            return someString;
        }

        public QueryParameterObject setSomeString(String someString) {
            this.someString = someString;
            return this;
        }

        public QueryParameterObject setSomeArray(List<String> someArray) {
            this.someArray = someArray;
            return this;
        }

        public QueryParameterObject setNested(QueryParameterObject2 nested) {
            this.nested = nested;
            return this;
        }

        public QueryParameterObject setNestedNext(QueryParameterObject4 nestedNext) {
            this.nestedNext = nestedNext;
            return this;
        }

        public Integer getSomeNum() {
            return someNum;
        }

        public List<String> getSomeArray() {
            return someArray;
        }

        public QueryParameterObject2 getNested() {
            return nested;
        }

        public QueryParameterObject4 getNestedNext() {
            return nestedNext;
        }
    }

    private static class QueryParameterObject2 extends MappedObject {
        Integer someNum2;

        String someString2;

        Integer[] someArray2;

        QueryParameterObject3 nested2;

        public QueryParameterObject2 setSomeNum(Integer someNum) {
            this.someNum2 = someNum;
            return this;
        }

        public QueryParameterObject2 setSomeString(String someString) {
            this.someString2 = someString;
            return this;
        }

        public QueryParameterObject2 setSomeArray(Integer[] someArray) {
            this.someArray2 = someArray;
            return this;
        }

        public QueryParameterObject2 setNested(QueryParameterObject3 nested) {
            this.nested2 = nested;
            return this;
        }

        public Integer getSomeNum2() {
            return someNum2;
        }

        public Integer[] getSomeArray2() {
            return someArray2;
        }

        public QueryParameterObject3 getNested2() {
            return nested2;
        }

        public String getSomeString2() {
            return someString2;
        }
    }

    private static class QueryParameterObject3 extends MappedObject {
        Integer someNum3;

        String someString3;

        Integer[] someArray3;

        public QueryParameterObject3 setSomeNum(Integer someNum) {
            this.someNum3 = someNum;
            return this;
        }

        public QueryParameterObject3 setSomeString(String someString) {
            this.someString3 = someString;
            return this;
        }

        public QueryParameterObject3 setSomeArray(Integer[] someArray) {
            this.someArray3 = someArray;
            return this;
        }

        public Integer getSomeNum3() {
            return someNum3;
        }

        public Integer[] getSomeArray3() {
            return someArray3;
        }

        public String getSomeString3() {
            return someString3;
        }
    }

    private static class QueryParameterObject4 extends MappedObject {
        Integer someNum4;

        Integer[] someArray4;

        String someString4;

        QueryParameterObject3 nested4;

        public QueryParameterObject4 setSomeNum(Integer someNum) {
            this.someNum4 = someNum;
            return this;
        }

        public QueryParameterObject4 setSomeString(String someString) {
            this.someString4 = someString;
            return this;
        }

        public QueryParameterObject4 setSomeArray(Integer[] someArray) {
            this.someArray4 = someArray;
            return this;
        }

        public QueryParameterObject4 setNested(QueryParameterObject3 nested) {
            this.nested4 = nested;
            return this;
        }

        public Integer getSomeNum4() {
            return someNum4;
        }

        public Integer[] getSomeArray4() {
            return someArray4;
        }

        public QueryParameterObject3 getNested4() {
            return nested4;
        }

        public String getSomeString4() {
            return someString4;
        }
    }
}
