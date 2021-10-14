package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

public class PseudoTest {

    private WebTestClient webTestClient;

    public PseudoTest setWebTestClient(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
        return this;
    }

    public WebTestClient test1() {
        return WebTestClientContext.getContext().getDefaultWebTestClient();
    }

    public Byte[] test2() {
        return webTestClient(send(w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .hasBody()
                .thenGetBody());
    }
}
