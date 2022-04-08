package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasPathMatcher;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathParameter;

import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.getProperties;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.http.api.properties.end.point.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.GET;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.LABEL;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.MATRIX;

public class PathMappingTest {

    private final static URI TEST_URI = URI.create("http://127.0.0.1:8089");

    private static final Map<?, ?> pathParamMap = new LinkedHashMap<>() {
        {
            put("someString", "String value");
            put("someNum", 10.1111D);
            put("someBool", true);
            put("someList", new Object[]{1, "ABC", null, "АБВ", true});
        }
    };

    private static final PathParameterObject pathParameterObject = new PathParameterObject().setSomeString("String value")
            .setSomeNum(10.1111D)
            .setSomeBool(true)
            .setSomeList(asList(new Object[]{1, "ABC", null, "АБВ", true}));

    private static Object[][] prepareDataForPathMapping(PathMapping someMappedAPI) {
        return new Object[][]{
                {someMappedAPI.getSomethingWithConstantPath(),
                        uriHasPath("/path/to/target/end/point"),
                        "/path/to/target/end/point"},

                {someMappedAPI.getSomethingWithConstantPath2(),
                        uriHasPath("/path/to/target/end/point"),
                        "/path/to/target/end/point"},

                {someMappedAPI.getSomethingWithVariablePath("Start path", 1.5F, "Кириллический текст"),
                        uriHasPath("/Start path/1.5/and/then/Кириллический текст/end/point"),
                        "/Start%20path/1.5/and/then/%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9%20%D1%82%D0%B5%D0%BA%D1%81%D1%82/end/point"},
                {someMappedAPI.getSomethingWithVariablePath("Start", "Next"),
                        uriHasPath("/Start/Next/and/then/Next/end/point"),
                        "/Start/Next/and/then/Next/end/point"},

                {someMappedAPI.getSomethingWithVariablePath(null, 1.5F, "Third"),
                        uriHasPath("/1.5/and/then/Third/end/point"),
                        "/1.5/and/then/Third/end/point"},

                {someMappedAPI.getSomethingWithVariablePath(null, null, 1.5F),
                        uriHasPath("/and/then/1.5/end/point"),
                        "/and/then/1.5/end/point"},

                {someMappedAPI.getSomethingWithVariablePathArrayS("Start path",
                        "Next value",
                        new Object[]{1, 2, null, "Кирилический текст", true}),
                        uriHasPath("/Start path/Next value/and/then/1,2,Кирилический текст,true/end/point"),
                        "/Start%20path/Next%20value/and/then/1,2,%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9%20%D1%82%D0%B5%D0%BA%D1%81%D1%82,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathArraySE("Start path",
                        "Next value",
                        new Object[]{1, 2, null, "Кирилический текст", true}),
                        uriHasPath("/Start path/Next value/and/then/1,2,Кирилический текст,true/end/point"),
                        "/Start%20path/Next%20value/and/then/1,2,%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9%20%D1%82%D0%B5%D0%BA%D1%81%D1%82,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathArrayL("Start path",
                        "Next value",
                        new Object[]{1, 2, null, "Кирилический текст", true}),
                        uriHasPath("/Start path/Next value/and/then/.1.2.Кирилический текст.true/end/point"),
                        "/Start%20path/Next%20value/and/then/.1.2.%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9%20%D1%82%D0%B5%D0%BA%D1%81%D1%82.true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathArrayLE("Start path",
                        "Next value",
                        new Object[]{1, 2, null, "Кирилический текст", true}),
                        uriHasPath("/Start path/Next value/and/then/.1.2.Кирилический текст.true/end/point"),
                        "/Start%20path/Next%20value/and/then/.1.2.%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9%20%D1%82%D0%B5%D0%BA%D1%81%D1%82.true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathArrayM("Start path",
                        "Next value",
                        new Object[]{1, 2, null, "Кирилический текст", true}),
                        uriHasPath("/Start path/Next value/and/then/;array=1,2,Кирилический текст,true/end/point"),
                        "/Start%20path/Next%20value/and/then/;array=1,2,%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9%20%D1%82%D0%B5%D0%BA%D1%81%D1%82,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathArrayME("Start path",
                        "Next value",
                        new Object[]{1, 2, null, "Кирилический текст", true}),
                        uriHasPath("/Start path/Next value/and/then/;array=1;array=2;array=Кирилический текст;array=true/end/point"),
                        "/Start%20path/Next%20value/and/then/;array=1;array=2;array=%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9%20%D1%82%D0%B5%D0%BA%D1%81%D1%82;array=true/end/point"},


                {someMappedAPI.getSomethingWithVariablePathObjectL("Start path",
                        "Next value",
                        "Some String"),
                        uriHasPath("/Start path/Next value/and/then/.Some String/end/point"),
                        "/Start%20path/Next%20value/and/then/.Some%20String/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectLE("Start path",
                        "Next value",
                        "Some String"),
                        uriHasPath("/Start path/Next value/and/then/.Some String/end/point"),
                        "/Start%20path/Next%20value/and/then/.Some%20String/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectM("Start path",
                        "Next value",
                        "Some String"),
                        uriHasPath("/Start path/Next value/and/then/;object=Some String/end/point"),
                        "/Start%20path/Next%20value/and/then/;object=Some%20String/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectME("Start path",
                        "Next value",
                        "Some String"),
                        uriHasPath("/Start path/Next value/and/then/;object=Some String/end/point"),
                        "/Start%20path/Next%20value/and/then/;object=Some%20String/end/point"},


                {someMappedAPI.getSomethingWithVariablePathObjectS("Start path",
                        "Next value",
                        pathParamMap),
                        uriHasPath("/Start path/Next value/and/then/someString,String value,someNum,10.1111,someBool,true,someList,1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/someString,String%20value,someNum,10.1111,someBool,true,someList,1,ABC,%D0%90%D0%91%D0%92,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectSE("Start path",
                        "Next value",
                        pathParamMap),
                        uriHasPath("/Start path/Next value/and/then/someString=String value,someNum=10.1111,someBool=true,someList=1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/someString=String%20value,someNum=10.1111,someBool=true,someList=1,ABC,%D0%90%D0%91%D0%92,true/end/point"},


                {someMappedAPI.getSomethingWithVariablePathObjectL("Start path",
                        "Next value",
                        pathParamMap),
                        uriHasPath("/Start path/Next value/and/then/.someString.String value.someNum.10.1111.someBool.true.someList.1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/.someString.String%20value.someNum.10.1111.someBool.true.someList.1,ABC,%D0%90%D0%91%D0%92,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectLE("Start path",
                        "Next value",
                        pathParamMap),
                        uriHasPath("/Start path/Next value/and/then/.someString=String value.someNum=10.1111.someBool=true.someList=1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/.someString=String%20value.someNum=10.1111.someBool=true.someList=1,ABC,%D0%90%D0%91%D0%92,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectM("Start path",
                        "Next value",
                        pathParamMap),
                        uriHasPath("/Start path/Next value/and/then/;object=someString,String value,someNum,10.1111,someBool,true,someList,1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/;object=someString,String%20value,someNum,10.1111,someBool,true,someList,1,ABC,%D0%90%D0%91%D0%92,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectME("Start path",
                        "Next value",
                        pathParamMap),
                        uriHasPath("/Start path/Next value/and/then/;someString=String value;someNum=10.1111;someBool=true;someList=1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/;someString=String%20value;someNum=10.1111;someBool=true;someList=1,ABC,%D0%90%D0%91%D0%92,true/end/point"},


                {someMappedAPI.getSomethingWithVariablePathObjectS("Start path",
                        "Next value",
                        pathParameterObject),
                        uriHasPath("/Start path/Next value/and/then/someString,String value,someNum,10.1111,someBool,true,someList,1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/someString,String%20value,someNum,10.1111,someBool,true,someList,1,ABC,%D0%90%D0%91%D0%92,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectSE("Start path",
                        "Next value",
                        pathParameterObject),
                        uriHasPath("/Start path/Next value/and/then/someString=String value,someNum=10.1111,someBool=true,someList=1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/someString=String%20value,someNum=10.1111,someBool=true,someList=1,ABC,%D0%90%D0%91%D0%92,true/end/point"},


                {someMappedAPI.getSomethingWithVariablePathObjectL("Start path",
                        "Next value",
                        pathParameterObject),
                        uriHasPath("/Start path/Next value/and/then/.someString.String value.someNum.10.1111.someBool.true.someList.1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/.someString.String%20value.someNum.10.1111.someBool.true.someList.1,ABC,%D0%90%D0%91%D0%92,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectLE("Start path",
                        "Next value",
                        pathParameterObject),
                        uriHasPath("/Start path/Next value/and/then/.someString=String value.someNum=10.1111.someBool=true.someList=1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/.someString=String%20value.someNum=10.1111.someBool=true.someList=1,ABC,%D0%90%D0%91%D0%92,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectM("Start path",
                        "Next value",
                        pathParameterObject),
                        uriHasPath("/Start path/Next value/and/then/;object=someString,String value,someNum,10.1111,someBool,true,someList,1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/;object=someString,String%20value,someNum,10.1111,someBool,true,someList,1,ABC,%D0%90%D0%91%D0%92,true/end/point"},

                {someMappedAPI.getSomethingWithVariablePathObjectME("Start path",
                        "Next value",
                        pathParameterObject),
                        uriHasPath("/Start path/Next value/and/then/;someString=String value;someNum=10.1111;someBool=true;someList=1,ABC,АБВ,true/end/point"),
                        "/Start%20path/Next%20value/and/then/;someString=String%20value;someNum=10.1111;someBool=true;someList=1,ABC,%D0%90%D0%91%D0%92,true/end/point"}
        };
    }

    @DataProvider
    public Object[][] data1() {
        return prepareDataForPathMapping(createAPI(PathMapping.class, TEST_URI));
    }

    @DataProvider
    public Object[][] data2() throws Exception {
        try {
            DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.accept(new URL("http://127.0.0.1:8089"));
            return prepareDataForPathMapping(createAPI(PathMapping.class));
        } finally {
            getProperties().remove(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getName());
        }
    }

    @Test(dataProvider = "data1")
    public void test1(RequestBuilder builder, HasPathMatcher<URI> pathMatcher, String rawPath) {
        var uri = builder.build().uri();
        assertThat(uri, uriHasScheme("http"));
        assertThat(uri, uriHasHost("127.0.0.1"));
        assertThat(uri, uriHasPort(8089));
        assertThat(uri, pathMatcher);
        assertThat(uri.toString(), endsWith(rawPath));
    }

    @Test(dataProvider = "data2")
    public void test2(RequestBuilder builder, HasPathMatcher<URI> pathMatcher, String rawPath) {
        test1(builder, pathMatcher, rawPath);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = ".*['These parameters are not defined by pattern: [third]. Pattern: {path begin}/{next}/and/then/third/end/point']")
    public void test3() {
        var methodMappingAPI = createAPI(PathMapping.class, TEST_URI);
        methodMappingAPI.getSomethingWithVariablePathFailed("StartPath",
                1.5F,
                "EndPath");
        fail("Exception was expected");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class,
            expectedExceptionsMessageRegExp = ".*['Path variable 'next' is defined more than once. This is not supported']")
    public void test4() {
        var methodMappingAPI = createAPI(PathMapping.class, TEST_URI);
        methodMappingAPI.getSomethingWithVariablePathFailed("StartPath",
                1.5F,
                true);
        fail("Exception was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = ".*['These parameters are not defined by method parameters: [third]. These names are defined by pattern {path begin}/{next}/and/then/{third}/end/point']")
    public void test5() {
        var methodMappingAPI = createAPI(PathMapping.class, TEST_URI);
        methodMappingAPI.getSomethingWithVariablePathFailed("StartPath",
                1.5F);
        fail("Exception was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = ".*['Path variable 'third' requires value that differs from null']")
    public void test6() {
        var methodMappingAPI = createAPI(PathMapping.class, TEST_URI);
        methodMappingAPI.getSomethingWithVariablePath("Start path", 1.5F, null);
        fail("Exception was expected");
    }


    private interface PathMapping extends HttpAPI<PathMapping> {

        @HttpMethod(httpMethod = GET)
        @URIPath("path/to/target/end/point")
        RequestBuilder getSomethingWithConstantPath();

        @HttpMethod(httpMethod = GET)
        @URIPath("/path/to/target/end/point")
        RequestBuilder getSomethingWithConstantPath2();

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{third}/end/point")
        RequestBuilder getSomethingWithVariablePath(@PathParameter(name = "path begin", required = false) String start,
                                                    @PathParameter(name = "next") float next,
                                                    @PathParameter(name = "third") String third);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{third}/end/point")
        RequestBuilder getSomethingWithVariablePath(@PathParameter(name = "path begin", required = false) String start,
                                                    @PathParameter(name = "next", required = false) String next,
                                                    @PathParameter(name = "third") float third);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{array}/end/point")
        RequestBuilder getSomethingWithVariablePathArrayS(@PathParameter(name = "path begin", required = false) String start,
                                                          @PathParameter(name = "next", required = false) String next,
                                                          @PathParameter(name = "array") Object[] array);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{array}/end/point")
        RequestBuilder getSomethingWithVariablePathArraySE(@PathParameter(name = "path begin", required = false) String start,
                                                           @PathParameter(name = "next", required = false) String next,
                                                           @PathParameter(name = "array", explode = true) Object[] array);


        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{array}/end/point")
        RequestBuilder getSomethingWithVariablePathArrayL(@PathParameter(name = "path begin", required = false) String start,
                                                          @PathParameter(name = "next", required = false) String next,
                                                          @PathParameter(name = "array",
                                                                  style = LABEL) Object[] array);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{array}/end/point")
        RequestBuilder getSomethingWithVariablePathArrayLE(@PathParameter(name = "path begin", required = false) String start,
                                                           @PathParameter(name = "next", required = false) String next,
                                                           @PathParameter(name = "array",
                                                                   style = LABEL,
                                                                   explode = true) Object[] array);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{array}/end/point")
        RequestBuilder getSomethingWithVariablePathArrayM(@PathParameter(name = "path begin", required = false) String start,
                                                          @PathParameter(name = "next", required = false) String next,
                                                          @PathParameter(name = "array",
                                                                  style = MATRIX) Object[] array);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{array}/end/point")
        RequestBuilder getSomethingWithVariablePathArrayME(@PathParameter(name = "path begin", required = false) String start,
                                                           @PathParameter(name = "next", required = false) String next,
                                                           @PathParameter(name = "array",
                                                                   style = MATRIX,
                                                                   explode = true) Object[] array);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{object}/end/point")
        RequestBuilder getSomethingWithVariablePathObjectS(@PathParameter(name = "path begin", required = false) String start,
                                                           @PathParameter(name = "next", required = false) String next,
                                                           @PathParameter(name = "object") Object object);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{object}/end/point")
        RequestBuilder getSomethingWithVariablePathObjectSE(@PathParameter(name = "path begin", required = false) String start,
                                                            @PathParameter(name = "next", required = false) String next,
                                                            @PathParameter(name = "object", explode = true) Object object);


        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{object}/end/point")
        RequestBuilder getSomethingWithVariablePathObjectL(@PathParameter(name = "path begin", required = false) String start,
                                                           @PathParameter(name = "next", required = false) String next,
                                                           @PathParameter(name = "object",
                                                                   style = LABEL) Object object);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{object}/end/point")
        RequestBuilder getSomethingWithVariablePathObjectLE(@PathParameter(name = "path begin", required = false) String start,
                                                            @PathParameter(name = "next", required = false) String next,
                                                            @PathParameter(name = "object",
                                                                    style = LABEL,
                                                                    explode = true) Object object);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{object}/end/point")
        RequestBuilder getSomethingWithVariablePathObjectM(@PathParameter(name = "path begin", required = false) String start,
                                                           @PathParameter(name = "next", required = false) String next,
                                                           @PathParameter(name = "object",
                                                                   style = MATRIX) Object object);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{object}/end/point")
        RequestBuilder getSomethingWithVariablePathObjectME(@PathParameter(name = "path begin", required = false) String start,
                                                            @PathParameter(name = "next", required = false) String next,
                                                            @PathParameter(name = "object",
                                                                    style = MATRIX,
                                                                    explode = true) Object object);


        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{next}/end/point")
        RequestBuilder getSomethingWithVariablePath(@PathParameter(name = "path begin") String start,
                                                    @PathParameter(name = "next") String next);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/third/end/point")
        RequestBuilder getSomethingWithVariablePathFailed(@PathParameter(name = "path begin") String start,
                                                          @PathParameter(name = "next") float next,
                                                          @PathParameter(name = "third") String third);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/third/end/point")
        RequestBuilder getSomethingWithVariablePathFailed(@PathParameter(name = "path begin") String start,
                                                          @PathParameter(name = "next") float next,
                                                          @PathParameter(name = "next") boolean third);

        @HttpMethod(httpMethod = GET)
        @URIPath("{path begin}/{next}/and/then/{third}/end/point")
        RequestBuilder getSomethingWithVariablePathFailed(@PathParameter(name = "path begin") String start,
                                                          @PathParameter(name = "next") float next);
    }

    private static class PathParameterObject extends MappedObject {

        private String someString;

        private Number someNum;

        private Boolean someBool;

        private List<Object> someList;

        private Object nullable;

        public String getSomeString() {
            return someString;
        }

        public PathParameterObject setSomeString(String someString) {
            this.someString = someString;
            return this;
        }

        public Number getSomeNum() {
            return someNum;
        }

        public PathParameterObject setSomeNum(Number someNum) {
            this.someNum = someNum;
            return this;
        }

        public Boolean getSomeBool() {
            return someBool;
        }

        public PathParameterObject setSomeBool(Boolean someBool) {
            this.someBool = someBool;
            return this;
        }

        public Object getNullable() {
            return nullable;
        }

        public PathParameterObject setNullable(Object nullable) {
            this.nullable = nullable;
            return this;
        }

        public List<Object> getSomeList() {
            return someList;
        }

        public PathParameterObject setSomeList(List<Object> someList) {
            this.someList = someList;
            return this;
        }
    }
}
