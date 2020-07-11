package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.Header;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.MethodParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter;
import ru.tinkoff.qa.neptune.http.api.test.BaseHttpTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.matching.UrlPattern.ANY;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asIs;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public class HeaderMappingTest extends BaseHttpTest {

    private final HeaderMapping methodMappingAPI = createAPI(HeaderMapping.class, REQUEST_URI);

    private final Map<?, ?> headerMap = new LinkedHashMap<>() {
        {
            put("someString", "String value");
            put("someNum", 10.1111D);
            put("someBool", true);
            put("someArray", List.of(1, "ABC", true));
        }
    };

    private final HeaderParameterObject testHeaderObject = new HeaderParameterObject().setSomeString("String value")
            .setSomeNum(10.1111D)
            .setSomeBool(true)
            .setSomeArray(new Object[]{1, "ABC", true});

    @BeforeClass
    public void prepare() {
        stubFor(any(ANY)
                .withHeader("header1", containing("abc"))
                .willReturn(aResponse().withBody("SUCCESS")));
    }

    @Test
    public void test1() {
        var rb = methodMappingAPI.postSomethingWithHeaders();
        var r = rb.build();

        assertThat(r.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));

        assertThat(http().bodyData(asIs(methodMappingAPI.postSomethingWithHeaders(), ofString())),
                is("SUCCESS"));
    }

    @Test
    public void test2() {
        var rb = methodMappingAPI.postSomethingWithHeaders("one more value", 1,
                100, 8.5F,
                new Object[]{"string value", true, 1},
                of("string value", true, 1),
                headerMap,
                headerMap,
                testHeaderObject,
                testHeaderObject);

        var r = rb.build();
        var headerMap = r.headers().map();

        assertThat(headerMap, hasEntry(Matchers.equalTo("header1"), contains("abc", "one more value")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("header2"), contains("one more value")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("header3"), contains("one more value again")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("digitHeader"), contains("1", "100", "8.5")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("arrayHeader"), contains("string value", "true", "1")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("iterable"), contains("string value", "true", "1")));

        assertThat(headerMap, hasEntry(Matchers.equalTo("simpleMap"), contains("someString,String value,someNum,10.1111,someBool,true,someArray,1,ABC,true")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("explodedMap"), contains("someString=String value,someNum=10.1111,someBool=true,someArray=1,ABC,true")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("simpleObject"), contains(allOf(
                containsString("someString,String value"),
                containsString("someNum,10.1111"),
                containsString("someBool,true"),
                containsString("someArray,1,ABC,true"),
                not(containsString("nullable"))))));

        assertThat(headerMap, hasEntry(Matchers.equalTo("explodedObject"),
                contains(allOf(
                        containsString("someString=String value"),
                        containsString("someNum=10.1111"),
                        containsString("someBool=true"),
                        containsString("someArray=1,ABC,true"),
                        not(containsString("nullable"))))));

        assertThat(http().bodyData(asIs(rb, ofString())),
                is("SUCCESS"));
    }

    @Test
    public void test3() {
        var rb = methodMappingAPI.postSomethingWithHeaders(null, null);

        var r = rb.build();
        var headerMap = r.headers().map();
        assertThat(headerMap, hasEntry(Matchers.equalTo("header1"), contains("abc", "one more value")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("header2"), contains("one more value")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("header3"), contains("one more value again")));
        assertThat(headerMap, not(hasKey("notRequired1")));
        assertThat(headerMap, not(hasKey("notRequired2")));

        assertThat(http().bodyData(asIs(rb, ofString())),
                is("SUCCESS"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = ".*['Value of the header 'required' is defined " +
                    "as required, but given value is null']")
    public void test4() {
        methodMappingAPI.postSomethingWithHeaders(null);
        fail("Exception was expected");
    }

    private interface HeaderMapping extends HttpAPI<HeaderMapping> {

        @Header(name = "header1", headerValues = {"abc", "one more value"})
        @Header(name = "header2", headerValues = "one more value")
        @Header(name = "header3", headerValues = "one more value again")
        @HttpMethod(httpMethod = POST)
        RequestBuilder postSomethingWithHeaders();

        @Header(name = "header1", headerValues = {"abc"})
        @Header(name = "header2", headerValues = "one more value")
        @Header(name = "header3", headerValues = "one more value again")
        @HttpMethod(httpMethod = POST)
        RequestBuilder postSomethingWithHeaders(@HeaderParameter(headerName = "header1") String header1,
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
        RequestBuilder postSomethingWithHeaders(@HeaderParameter(headerName = "notRequired1") Object notRequired1,
                                                @HeaderParameter(headerName = "notRequired2") Object notRequired2);

        @Header(name = "header1", headerValues = {"abc", "one more value"})
        @Header(name = "header2", headerValues = "one more value")
        @Header(name = "header3", headerValues = "one more value again")
        @HttpMethod(httpMethod = POST)
        RequestBuilder postSomethingWithHeaders(@HeaderParameter(headerName = "required", required = true) Object requiredHeader);
    }

    @MethodParameter
    private static class HeaderParameterObject {

        private String someString;

        private Number someNum;

        private Boolean someBool;

        private Object nullable;

        private Object[] someArray;

        public String getSomeString() {
            return someString;
        }

        public HeaderParameterObject setSomeString(String someString) {
            this.someString = someString;
            return this;
        }

        public Number getSomeNum() {
            return someNum;
        }

        public HeaderParameterObject setSomeNum(Number someNum) {
            this.someNum = someNum;
            return this;
        }

        public Boolean getSomeBool() {
            return someBool;
        }

        public HeaderParameterObject setSomeBool(Boolean someBool) {
            this.someBool = someBool;
            return this;
        }

        public Object getNullable() {
            return nullable;
        }

        public HeaderParameterObject setNullable(Object nullable) {
            this.nullable = nullable;
            return this;
        }

        public Object[] getSomeArray() {
            return someArray;
        }

        public HeaderParameterObject setSomeArray(Object[] someArray) {
            this.someArray = someArray;
            return this;
        }
    }
}
