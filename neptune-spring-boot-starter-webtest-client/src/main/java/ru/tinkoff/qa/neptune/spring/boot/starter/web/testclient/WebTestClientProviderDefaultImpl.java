package ru.tinkoff.qa.neptune.spring.boot.starter.web.testclient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.boot.starter.web.testclient.WebTestClientWrappingConfiguration.getWebTestClientStatic;

public class WebTestClientProviderDefaultImpl implements WebTestClientProvider {

    @Override
    public WebTestClient provide() {
        return getWebTestClientStatic();
    }
}
