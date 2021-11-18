package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.reactive.server.MockAssertionsCreator.createHeaderAssertion;
import static org.springframework.test.web.reactive.server.MockAssertionsCreator.createStatusAssertion;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseTest {

    @Mock
    WebTestClient client;

    @Mock
    WebTestClient.RequestHeadersUriSpec uriSpec;

    @Mock
    WebTestClient.ResponseSpec responseSpec;

    @Mock
    ExchangeResult result;

    @Mock
    EntityExchangeResult<Void> empty;

    @Mock
    WebTestClient.BodyContentSpec bodyContentSpec;

    @Mock
    EntityExchangeResult<byte[]> rawResult;

    @Mock
    WebTestClient.BodySpec<Integer, ?> integerBodySpec;

    @Mock
    WebTestClient.BodySpec<List<Integer>, ?> listBodySpec;

    @Mock
    WebTestClient.ListBodySpec<Integer> listBodySpec2;

    @Mock
    WebTestClient.ListBodySpec<List<Integer>> listOfListBodySpec;

    @Mock
    EntityExchangeResult<Integer> intResult;

    @Mock
    EntityExchangeResult<List<Integer>> listResult;

    @Mock
    EntityExchangeResult<List<Integer>> listResult2;

    @Mock
    EntityExchangeResult<List<List<Integer>>> listListResult;

    private Supplier<byte[]> byteSupplier = () -> {
        byte[] b = new byte[20];
        new Random().nextBytes(b);
        return b;
    };

    @BeforeClass
    public void setUpBeforeClass() {
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

        when(rawResult.getResponseBody()).thenAnswer(invocation -> byteSupplier.get());
        when(rawResult.toString()).thenReturn("Request execution result");

        when(rawResult.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(rawResult.getMethod()).thenReturn(HttpMethod.GET);
        when(rawResult.getRequestHeaders()).thenReturn(requestHeaders);
        when(rawResult.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(rawResult.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(rawResult.getRawStatusCode()).thenReturn(200);
        when(rawResult.getResponseHeaders()).thenReturn(responseHeaders);

        when(empty.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(empty.getMethod()).thenReturn(HttpMethod.GET);
        when(empty.getRequestHeaders()).thenReturn(requestHeaders);
        when(empty.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(empty.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(empty.getRawStatusCode()).thenReturn(200);
        when(empty.getResponseHeaders()).thenReturn(responseHeaders);

        when(intResult.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(intResult.getMethod()).thenReturn(HttpMethod.GET);
        when(intResult.getRequestHeaders()).thenReturn(requestHeaders);
        when(intResult.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(intResult.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(intResult.getRawStatusCode()).thenReturn(200);
        when(intResult.getResponseHeaders()).thenReturn(responseHeaders);

        when(listResult.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(listResult.getMethod()).thenReturn(HttpMethod.GET);
        when(listResult.getRequestHeaders()).thenReturn(requestHeaders);
        when(listResult.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(listResult.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(listResult.getRawStatusCode()).thenReturn(200);
        when(listResult.getResponseHeaders()).thenReturn(responseHeaders);

        when(listResult2.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(listResult2.getMethod()).thenReturn(HttpMethod.GET);
        when(listResult2.getRequestHeaders()).thenReturn(requestHeaders);
        when(listResult2.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(listResult2.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(listResult2.getRawStatusCode()).thenReturn(200);
        when(listResult2.getResponseHeaders()).thenReturn(responseHeaders);

        when(listListResult.getUrl()).thenReturn(URI.create("https://google.com/api/request/1"));
        when(listListResult.getMethod()).thenReturn(HttpMethod.GET);
        when(listListResult.getRequestHeaders()).thenReturn(requestHeaders);
        when(listListResult.getRequestBodyContent()).thenReturn("Hello".getBytes());
        when(listListResult.getResponseBodyContent()).thenReturn("Hi".getBytes());
        when(listListResult.getRawStatusCode()).thenReturn(200);
        when(listListResult.getResponseHeaders()).thenReturn(responseHeaders);

        when(responseSpec.expectBody()).thenReturn(bodyContentSpec);
        when(bodyContentSpec.isEmpty()).thenReturn(empty);
        when(empty.getResponseBody()).thenReturn(null);

        doAnswer(invocation -> {
            var consumer = invocation.getArgument(0, Consumer.class);
            consumer.accept(rawResult);
            return bodyContentSpec;
        }).when(bodyContentSpec).consumeWith(any(Consumer.class));

        Mockito.<WebTestClient.BodySpec<Integer, ?>>when(responseSpec.expectBody(Integer.class)).thenReturn(integerBodySpec);
        doAnswer(invocation -> {
            var consumer = invocation.getArgument(0, Consumer.class);
            consumer.accept(intResult);
            return integerBodySpec;
        }).when(integerBodySpec).consumeWith(any(Consumer.class));
        when(intResult.getResponseBody()).thenReturn(1);

        Mockito.<WebTestClient.BodySpec<List<Integer>, ?>>when(responseSpec.expectBody(new ParameterizedTypeReference<List<Integer>>() {
        })).thenReturn(listBodySpec);
        doAnswer(invocation -> {
            var consumer = invocation.getArgument(0, Consumer.class);
            consumer.accept(listResult);
            return listBodySpec;
        }).when(listBodySpec).consumeWith(any(Consumer.class));
        when(listResult.getResponseBody()).thenReturn(of(1, 2, 3));

        when(responseSpec.expectBodyList(Integer.class)).thenReturn(listBodySpec2);
        doAnswer(invocation -> {
            var consumer = invocation.getArgument(0, Consumer.class);
            consumer.accept(listResult2);
            return listBodySpec2;
        }).when(listBodySpec2).consumeWith(any(Consumer.class));
        when(listResult2.getResponseBody()).thenReturn(of(1, 2, 3));

        when(responseSpec.expectBodyList(new ParameterizedTypeReference<List<Integer>>() {
        })).thenReturn(listOfListBodySpec);
        doAnswer(invocation -> {
            var consumer = invocation.getArgument(0, Consumer.class);
            consumer.accept(listListResult);
            return listOfListBodySpec;
        }).when(listOfListBodySpec).consumeWith(any(Consumer.class));
        when(listListResult.getResponseBody()).thenReturn(of(of(1, 2, 3), of(1, 2, 3), of(1, 2, 3)));
    }

    void setByteSupplier(Supplier<byte[]> supplier) {
        this.byteSupplier = supplier;
    }

    @AfterMethod
    public void rollBackByteSupplier() {
        setByteSupplier(() -> {
            byte[] b = new byte[20];
            new Random().nextBytes(b);
            return b;
        });
    }
}
