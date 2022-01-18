package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static java.util.Objects.nonNull;
import static org.springframework.beans.factory.BeanFactoryUtils.beanNamesForTypeIncludingAncestors;
import static ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

/**
 * Provides an instance of {@link WebTestClient}
 */
final class WebTestClientProvider {

    /**
     * @return instance of {@link WebTestClient}
     */
    public WebTestClient provide() {
        var context = getCurrentApplicationContext();
        var beans = beanNamesForTypeIncludingAncestors(context, WebTestClient.class);
        for (var bean : beans) {
            try {
                var result = context.getBean(bean);
                if (nonNull(result)) {
                    return (WebTestClient) result;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
