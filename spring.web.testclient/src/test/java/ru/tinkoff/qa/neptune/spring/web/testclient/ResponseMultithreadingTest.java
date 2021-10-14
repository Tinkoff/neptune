package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.*;
import org.testng.annotations.*;

import java.net.URI;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.emptyOrNullString;
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
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.TestStringInjector.getMessages;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ResponseMultithreadingTest {

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
    private EntityExchangeResult<byte[]> rawResult;

    @Mock
    private WebTestClient.BodyContentSpec bodyContentSpec;

    @BeforeClass
    public void setUpBeforeClass() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

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
        when(bodyContentSpec.returnResult()).thenReturn(rawResult);
        when(rawResult.getResponseBody()).thenReturn(b);

        when(responseSpec.returnResult(Void.class)).thenReturn(resultForReport);
        when(resultForReport.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(resultForReport.getMethod()).thenReturn(HttpMethod.GET);
        when(resultForReport.getRequestHeaders()).thenReturn(requestHeaders);
        when(resultForReport.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(resultForReport.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(resultForReport.getRawStatusCode()).thenReturn(200);
        when(resultForReport.getResponseHeaders()).thenReturn(responseHeaders);
    }

    @AfterClass
    public void clear() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @Test(threadPoolSize = 10, invocationCount = 10)
    public void test() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .hasBody()
                .thenGetBody());

        assertThat(body, arrayWithSize(20));

        assertThat(getMessages(),
                mapOf(
                        mapEntry("Request body", notOf(emptyOrNullString())),
                        mapEntry("Response body", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                ));
    }

    @AfterMethod
    @BeforeMethod
    public void prepare() {
        getMessages().clear();
    }
}
