package ru.tinkoff.qa.neptune.spring.boot.starter.web.testclient;

import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Provides an instance of {@link WebTestClient}. Instance of {WebTestClientProvider}
 * is considered to be loaded by SPI {@link java.util.ServiceLoader}
 */
public interface WebTestClientProvider {

    /**
     * @return instance of {@link WebTestClient}
     */
    WebTestClient provide();
}
