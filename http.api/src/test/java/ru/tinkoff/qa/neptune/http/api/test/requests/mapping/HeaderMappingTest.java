package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.Header;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.HeaderParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.MethodParameter;
import ru.tinkoff.qa.neptune.http.api.test.BaseHttpTest;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.matching.UrlPattern.ANY;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.List.of;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asIs;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public class HeaderMappingTest extends BaseHttpTest {

    private final HeaderMapping methodMappingAPI = createAPI(HeaderMapping.class, REQUEST_URI);

    @BeforeClass
    public void prepare() {
        stubFor(any(ANY)
                .withHeader("header1", containing("abc"))
                .willReturn(aResponse().withBody("SUCCESS")));
    }

    @Test
    public void test1() {
        var r = methodMappingAPI.postSomethingWithHeaders()
                .build();

        assertThat(r.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));

        assertThat(http().bodyData(asIs(methodMappingAPI.postSomethingWithHeaders(), ofString())),
                is("SUCCESS"));
    }

    @Test
    public void test2() {
        var r = methodMappingAPI.postSomethingWithHeaders("one more value", 1,
                100, 8.5F,
                new Object[]{"string value", true, 1},
                of("string value", true, 1),
                ofEntries(entry("someString", "String value"),
                        entry("someNum", 10.1111D),
                        entry("someBool", true)),
                ofEntries(entry("someString", "String value"),
                        entry("someNum", 10.1111D),
                        entry("someBool", true)),
                new HeaderParameterObject().setSomeString("String value")
                        .setSomeNum(10.1111D)
                        .setSomeBool(true),
                new HeaderParameterObject().setSomeString("String value")
                        .setSomeNum(10.1111D)
                        .setSomeBool(true))
                .build();

        assertThat(r.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again")),
                hasEntry("digitHeader", of("1", "100", "8.5")),
                hasEntry("arrayHeader", of("string value,true,1")),
                hasEntry("iterable", of("string value,true,1")),
                hasEntry("simpleMap", of("someString,String value,someNum,"))));

        assertThat(http().bodyData(asIs(methodMappingAPI.postSomethingWithHeaders(), ofString())),
                is("SUCCESS"));
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
    }

    @MethodParameter
    private static class HeaderParameterObject {

        private String someString;

        private Number someNum;

        private Boolean someBool;

        private Object nullable;

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
    }
}
