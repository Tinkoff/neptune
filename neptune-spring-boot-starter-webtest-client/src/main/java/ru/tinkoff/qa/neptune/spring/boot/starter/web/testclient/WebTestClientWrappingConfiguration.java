package ru.tinkoff.qa.neptune.spring.boot.starter.web.testclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

@Configuration
public class WebTestClientWrappingConfiguration {

    private final static Set<WebTestClientWrappingConfiguration> WEB_TEST_CLIENT_WRAPPERS = new CopyOnWriteArraySet<>();

    @Autowired(required = false)
    WebTestClient webTestClient;

    @Autowired
    ApplicationContext context;

    public WebTestClientWrappingConfiguration() {
        super();
        WEB_TEST_CLIENT_WRAPPERS.add(this);
    }

    static WebTestClient getWebTestClientStatic() {
        var context = getCurrentApplicationContext();
        return WEB_TEST_CLIENT_WRAPPERS
                .stream()
                .filter(web -> web.getContext() == context)
                .findFirst()
                .map(WebTestClientWrappingConfiguration::getWebTestClient)
                .orElse(null);
    }

    private WebTestClient getWebTestClient() {
        return webTestClient;
    }

    private ApplicationContext getContext() {
        return context;
    }
}
