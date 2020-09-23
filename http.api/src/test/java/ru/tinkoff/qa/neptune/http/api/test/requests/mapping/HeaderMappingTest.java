package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.test.BaseHttpTest;
import ru.tinkoff.qa.neptune.http.api.test.requests.RequestTuner2;

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

    @Test
    public void test5() {
        var rb = methodMappingAPI.postSomethingWithHeadersDefault();
        var r = rb.build();
        var headerMap = r.headers().map();

        assertThat(headerMap, hasEntry(Matchers.equalTo("required"), contains("5")));
    }

    @Test
    public void test6() {
        var rb = createAPI(HeaderMapping.class, REQUEST_URI)
                .useForRequestBuilding(RequestTuner2.class)
                .postSomethingWithHeadersDefault();

        var r = rb.build();
        var headerMap = r.headers().map();

        assertThat(headerMap, hasEntry(Matchers.equalTo("header1"), contains("abc", "one more value")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("header2"), contains("one more value")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("header3"), contains("one more value again")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("required"), contains("5")));
    }

    @Test
    public void test7() {
        var rb = createAPI(HeaderMapping.class, REQUEST_URI)
                .useForRequestBuilding(new RequestTuner2())
                .postSomethingWithHeadersDefault();

        var r = rb.build();
        var headerMap = r.headers().map();

        assertThat(headerMap, hasEntry(Matchers.equalTo("header1"), contains("abc", "one more value")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("header2"), contains("one more value")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("header3"), contains("one more value again")));
        assertThat(headerMap, hasEntry(Matchers.equalTo("required"), contains("5")));
    }
}
