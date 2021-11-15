package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

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
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ResponseBodyTest {

    @Mock
    private WebTestClient client;

    @Mock
    private WebTestClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebTestClient.ResponseSpec responseSpec;

    @Mock
    private ExchangeResult result;

    @Mock
    private WebTestClient.BodyContentSpec bodyContentSpec;

    @Mock
    private WebTestClient.BodySpec<Dto, ?> dtoBodySpec;

    @Mock
    private EntityExchangeResult<Dto> dtoResult;

    @BeforeClass
    public void setUpBeforeClass() {
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

        Mockito.<WebTestClient.BodySpec<Dto, ?>>when(responseSpec.expectBody(Dto.class)).thenReturn(dtoBodySpec);
        when(dtoBodySpec.returnResult()).thenReturn(dtoResult);
        when(dtoResult.getResponseBody()).thenReturn(new Dto()
                .setStringValue("ABCD")
                .setArrayValue1(new ArrayList<>(of("a", "b", "c", "d")))
                .setArrayValue2(new Integer[]{1, 2, 3, 4}));
    }

    @Test
    public void bodyTest() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetBody()
                .criteria("stringValue = 'ABCD'", dto -> dto.getStringValue().equals("ABCD"))
                .criteria("arrayValue1 size greater then 1", dto -> dto.getArrayValue1().size() > 1)
                .criteria("arrayValue2 length greater then 1", dto -> dto.getArrayValue2().length > 1));

        assertThat(body, not(nullValue()));
    }

    @Test
    public void bodyTest2() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetBody()
                .criteria("stringValue = 'ABCD'", dto -> dto.getStringValue().equals("ABCD"))
                .criteria("arrayValue1 size greater then 5", dto -> dto.getArrayValue1().size() > 5)
                .criteria("arrayValue2 length greater then 5", dto -> dto.getArrayValue2().length > 5));

        assertThat(body, nullValue());
    }

    @Test
    public void valueTest() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetValue("Value of the field 'stringValue'", Dto::getStringValue)
                .criteria("contains 'A'", s -> s.contains("A")));

        assertThat(value, not(nullValue()));
    }

    @Test
    public void valueTest2() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetValue("Value of the field 'stringValue'", Dto::getStringValue)
                .criteria("contains 'E'", s -> s.contains("E")));

        assertThat(value, nullValue());
    }

    @Test
    public void iterableTest() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetList("List from 'getArrayValue1'", Dto::getArrayValue1)
                .criteria("not contains 'E'", s -> !s.contains("E")));

        assertThat(value, hasSize(4));
    }

    @Test
    public void iterableTest2() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetList("List from 'getArrayValue1'", Dto::getArrayValue1)
                .criteria("contains 'E'", s -> s.contains("E")));

        assertThat(value, emptyIterable());
    }

    @Test
    public void iterableItemTest() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetValueFromIterable("Value from List from 'getArrayValue1'", Dto::getArrayValue1)
                .criteria("not contains 'E'", s -> !s.contains("E")));

        assertThat(value, not(nullValue()));
    }

    @Test
    public void iterableItemTest2() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetValueFromIterable("Value from List from 'getArrayValue1'", Dto::getArrayValue1)
                .criteria("contains 'E'", s -> s.contains("E")));

        assertThat(value, nullValue());
    }

    @Test
    public void arrayTest() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetArray("Array of int", Dto::getArrayValue2)
                .criteria("lesser than 5", i -> i < 5));

        assertThat(value, arrayWithSize(4));
    }

    @Test
    public void arrayTest2() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetArray("Array of int", Dto::getArrayValue2)
                .criteria("greater than 5", i -> i > 5));

        assertThat(value, arrayWithSize(0));
    }

    @Test
    public void arrayItemTest() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetValueFromArray("Array of int", Dto::getArrayValue2)
                .criteria("lesser than 5", i -> i < 5));

        assertThat(value, notNullValue());
    }

    @Test
    public void arrayItemTest2() {
        var value = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
                .thenGetValueFromArray("Array of int", Dto::getArrayValue2)
                .criteria("greater than 5", i -> i > 5));

        assertThat(value, nullValue());
    }
}
