package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasQueryStringMatcher;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasBody.hasBody;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasHeaders.hasHeader;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasPreviousResponse.hasPreviousResponse;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasStatusCode.hasStatusCode;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasURI.hasURI;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasVersion.hasVersion;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;

public class HttpResponseInfoTest extends BaseHttpTest {

    @Test
    public void statusCode() {
        stubFor(get(urlPathEqualTo("/testStatus.html"))
                .willReturn(aResponse().withBody("SUCCESS")));

        assertThat(http().responseOf(GET(REQUEST_URI + "/testStatus.html"), ofString()),
                hasStatusCode(200));
    }


    @Test
    public void headersTest() {
        stubFor(post(urlPathEqualTo("/header2.html"))
                .willReturn(aResponse().withBody("SUCCESS")));

        assertThat(http().responseOf(POST(REQUEST_URI + "/header2.html", "Request body")),
                all(
                        hasHeader("matched-stub-id", notOf(emptyIterable())),
                        hasHeader("transfer-encoding", iterableOf("chunked")),
                        hasHeader("vary", iterableInOrder("Accept-Encoding, User-Agent"))
                ));
    }

    @Test
    public void bodyTest() {
        stubFor(get(urlPathEqualTo("/body2.html"))
                .willReturn(aResponse().withBody("SUCCESS")));

        assertThat(http().responseOf(GET(format("%s/body2.html", REQUEST_URI)), ofString()),
                hasBody("SUCCESS"));
    }

    @Test
    public void uriTest() {
        stubFor(get(urlPathEqualTo("/uri.html"))
                .willReturn(aResponse().withBody("SUCCESS")));

        assertThat(http().responseOf(GET(format("%s/uri.html", REQUEST_URI))),
                hasURI(allOf(uriHasScheme("http"),
                        uriHasHost("127.0.0.1"),
                        uriHasPort(8089),
                        uriHasPath("/uri.html"),
                        HasQueryStringMatcher.uriHasQueryString(nullValue())
                )));
    }

    @Test
    public void versionTest() {
        stubFor(get(urlPathEqualTo("/version.html"))
                .willReturn(aResponse().withBody("SUCCESS")));

        assertThat(http().responseOf(GET(format("%s/version.html", REQUEST_URI))),
                hasVersion(HTTP_1_1));
    }


    @Test
    public void previousResponseTest() {
        stubFor(get(urlPathEqualTo("/version2.html"))
                .willReturn(aResponse().withBody("SUCCESS")));

        assertThat(http().responseOf(GET(format("%s/version2.html", REQUEST_URI))),
                not(hasPreviousResponse()));
    }
}
