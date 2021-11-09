package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.*;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.reactive.server.MockAssertionsCreator.createHeaderAssertion;
import static org.springframework.test.web.reactive.server.MockAssertionsCreator.createStatusAssertion;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.TestStringInjector.getMessages;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SuccessfulResponseTest {

    @Mock
    private WebTestClient client;

    @Mock
    private WebTestClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebTestClient.ResponseSpec responseSpec;

    @Mock
    private ExchangeResult result;

    @Mock
    private FluxExchangeResult<Void> resultForReport;

    @Mock
    private EntityExchangeResult<Void> empty;

    @Mock
    private WebTestClient.BodyContentSpec bodyContentSpec;

    @Mock
    private EntityExchangeResult<byte[]> rawResult;

    @Mock
    private WebTestClient.BodySpec<Integer, ?> integerBodySpec;

    @Mock
    private WebTestClient.BodySpec<List<Integer>, ?> listBodySpec;

    @Mock
    private WebTestClient.ListBodySpec<Integer> listBodySpec2;

    @Mock
    private WebTestClient.ListBodySpec<List<Integer>> listOfListBodySpec;

    @Mock
    private EntityExchangeResult<Integer> intResult;

    @Mock
    private EntityExchangeResult<List<Integer>> listResult;

    @Mock
    private EntityExchangeResult<List<Integer>> listResult2;

    @Mock
    private EntityExchangeResult<List<List<Integer>>> listListResult;

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {SUCCESS, mapOf(
                        mapEntry("Request body", notOf(emptyOrNullString())),
                        mapEntry("Response body", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                )},
                {FAILURE, anEmptyMap()},
                {SUCCESS_AND_FAILURE, mapOf(
                        mapEntry("Request body", notOf(emptyOrNullString())),
                        mapEntry("Response body", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                )},
        };
    }

    @BeforeClass
    public void setUpBeforeClass() {
        byte[] b = new byte[20];
        new Random().nextBytes(b);

        var requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setContentLength(50);

        var responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(TEXT_PLAIN);
        responseHeaders.setContentLength(50);

        openMocks(this);
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri("https://google.com/api/request/1")).thenReturn(uriSpec);
        when(uriSpec.exchange()).thenReturn(responseSpec);

        when(responseSpec.expectStatus()).thenReturn(createStatusAssertion(result, responseSpec));
        when(responseSpec.expectHeader()).thenReturn(createHeaderAssertion(result, responseSpec));

        when(result.getRawStatusCode()).thenReturn(200);
        when(result.getResponseHeaders()).thenReturn(new HttpHeaders());
        doNothing().when(result).assertWithDiagnostics(any(Runnable.class));

        when(responseSpec.expectBody()).thenReturn(bodyContentSpec);
        when(bodyContentSpec.isEmpty()).thenReturn(empty);
        when(empty.getResponseBody()).thenReturn(null);

        when(bodyContentSpec.returnResult()).thenReturn(rawResult);
        when(rawResult.getResponseBody()).thenReturn(b);

        Mockito.<WebTestClient.BodySpec<Integer, ?>>when(responseSpec.expectBody(Integer.class)).thenReturn(integerBodySpec);
        when(integerBodySpec.returnResult()).thenReturn(intResult);
        when(intResult.getResponseBody()).thenReturn(1);

        Mockito.<WebTestClient.BodySpec<List<Integer>, ?>>when(responseSpec.expectBody(new ParameterizedTypeReference<List<Integer>>() {
        })).thenReturn(listBodySpec);
        when(listBodySpec.returnResult()).thenReturn(listResult);
        when(listResult.getResponseBody()).thenReturn(of(1, 2, 3));

        when(responseSpec.expectBodyList(Integer.class)).thenReturn(listBodySpec2);
        when(listBodySpec2.returnResult()).thenReturn(listResult2);
        when(listResult2.getResponseBody()).thenReturn(of(1, 2, 3));

        when(responseSpec.expectBodyList(new ParameterizedTypeReference<List<Integer>>() {
        })).thenReturn(listOfListBodySpec);
        when(listOfListBodySpec.returnResult()).thenReturn(listListResult);
        when(listListResult.getResponseBody()).thenReturn(of(of(1, 2, 3), of(1, 2, 3), of(1, 2, 3)));

        when(responseSpec.returnResult(Void.class)).thenReturn(resultForReport);
        when(resultForReport.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(resultForReport.getMethod()).thenReturn(HttpMethod.GET);
        when(resultForReport.getRequestHeaders()).thenReturn(requestHeaders);
        when(resultForReport.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(resultForReport.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(resultForReport.getRawStatusCode()).thenReturn(200);
        when(resultForReport.getResponseHeaders()).thenReturn(responseHeaders);
    }

    @AfterMethod
    @BeforeMethod
    public void prepare() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
        getMessages().clear();
    }

    @Test(description = "successful")
    public void successfulTestWithoutBodySpec() {
        webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)));
    }

    @Test(description = "successful with body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulBody() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetBody());

        assertThat(body, arrayWithSize(20));
    }

    @Test(description = "successful with empty body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulEmptyBody() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .emptyBody()
                .thenGetBody());

        assertThat(body, nullValue());
    }

    @Test(description = "successful with any body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulAnyBody() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .hasBody()
                .thenGetBody());

        assertThat(body, arrayWithSize(20));
    }

    @Test(description = "successful with class body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulBodyByClass() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Integer.class)
                .thenGetBody());

        assertThat(body, is(1));
    }

    @Test(description = "successful with type body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulBodyByType() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(new ParameterizedTypeReference<List<Integer>>() {
                })
                .thenGetBody());

        assertThat(body, contains(1, 2, 3));
    }

    @Test(description = "successful with list body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulListBodyByClass() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAsListOf(Integer.class)
                .thenGetBody());

        assertThat(body, contains(1, 2, 3));
    }

    @Test(description = "successful with list body by type", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulListBodyByType() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAsListOf(new ParameterizedTypeReference<List<Integer>>() {
                })
                .thenGetBody());

        assertThat(body, contains(of(1, 2, 3), of(1, 2, 3), of(1, 2, 3)));
    }

    @Test(dependsOnMethods = "successfulTestWithoutBodySpec", dataProvider = "data")
    public void capturesOfSuccessful(CapturedEvents eventType, Matcher<Map<String, String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(eventType);

        webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)));

        assertThat(getMessages(), matcher);
    }

    /*@Test(description = "successful")
    public void failedTest() {
        try {
            mockMvcGet(response(mockMvc, builder2)
                    .expect(status().is2xxSuccessful())
                    .expect(header().stringValues("someHeader1",
                            iterableInOrder("1",
                                    "2",
                                    "String 1",
                                    "true"
                            )))
                    .expect(content().string(containsString("SUCCESS")))
                    .expect(forwardedUrl("https://google.com/api/request/1")));
        } catch (AssertionError e) {
            assertThat(e.getMessage(), all(
                    containsString("Mismatches: "),
                    containsString("Range for response status value "),
                    containsString("Response header"),
                    containsString("Response content"),
                    containsString("Forwarded URL expected")));
            return;
        }

        fail("Exception was expected");
    }

    @Test(dependsOnMethods = "failedTest", dataProvider = "data2")
    public void capturesOfFailed(CapturedEvents eventType, Matcher<Map<String, String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(eventType);

        try {
            mockMvcGet(response(mockMvc, builder2)
                    .expect(status().isOk())
                    .expect(header().stringValues("someHeader1",
                            iterableInOrder("1",
                                    "2",
                                    "String 1",
                                    "true"
                            )))
                    .expect(content().string(containsString("SUCCESS")))
                    .expect(forwardedUrl("https://google.com/api/request/1")));
        } catch (Throwable ignored) {
        }

        assertThat(getMessages(), matcher);
    }*/
}
