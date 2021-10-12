package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.*;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;

import java.net.URI;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.reactive.server.MockAssertionsCreator.createHeaderAssertion;
import static org.springframework.test.web.reactive.server.MockAssertionsCreator.createStatusAssertion;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.TestStringInjector.getMessages;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FailedResponseTest {

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
    private WebTestClient.BodyContentSpec bodyContentSpec;

    @Mock
    private WebTestClient.BodySpec<Integer, ?> integerBodySpec;

    @Mock
    private EntityExchangeResult<Integer> intResult;

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {SUCCESS, anEmptyMap()},
                {FAILURE, mapOf(
                        mapEntry("Request body", notOf(emptyOrNullString())),
                        mapEntry("Response body", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                )},
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
        doThrow(new AssertionError("Test assertion error")).when(result).assertWithDiagnostics(any(Runnable.class));

        when(responseSpec.expectBody()).thenReturn(bodyContentSpec);
        when(bodyContentSpec.isEmpty()).thenThrow(new AssertionError("Body is not empty"));

        Mockito.<WebTestClient.BodySpec<Integer, ?>>when(responseSpec.expectBody(Integer.class)).thenReturn(integerBodySpec);
        when(integerBodySpec.returnResult()).thenReturn(intResult);
        when(intResult.getResponseBody()).thenThrow(new RuntimeException("Test parse exception") {
        });

        when(responseSpec.returnResult(Void.class)).thenReturn(resultForReport);
        when(resultForReport.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(resultForReport.getMethod()).thenReturn(HttpMethod.GET);
        when(resultForReport.getRequestHeaders()).thenReturn(requestHeaders);
        when(resultForReport.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(resultForReport.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(resultForReport.getRawStatusCode()).thenReturn(200);
        when(resultForReport.getResponseHeaders()).thenReturn(responseHeaders);
    }

    @Test
    public void failedTest() {
        try {
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .emptyBody()
                    .thenGetBody());
        } catch (AssertionError e) {
            assertThat(e.getCause(), nullValue());
            assertThat(e.getMessage(), is("Mismatches: \r\n" +
                    "\r\n" +
                    "Test assertion error\r\n" +
                    "\r\n" +
                    "Test assertion error\r\n" +
                    "\r\n" +
                    "Body is not empty"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void failedTest2() {
        try {
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .bodyAs(Integer.class)
                    .thenGetBody());
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is("Mismatches: \r\n" +
                    "\r\n" +
                    "Test assertion error\r\n" +
                    "\r\n" +
                    "Test assertion error\r\n" +
                    "\r\n" +
                    "Test parse exception"));
            return;
        }

        fail("Exception was expected");
    }

    @AfterMethod
    @BeforeMethod
    public void prepare() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
        getMessages().clear();
    }

    @Test(dependsOnMethods = "failedTest", dataProvider = "data")
    public void capturesOfFailed(CapturedEvents eventType, Matcher<Map<String, String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(eventType);

        try {
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .emptyBody()
                    .thenGetBody());
        } catch (Throwable ignored) {
        }

        assertThat(getMessages(), matcher);
    }
}
