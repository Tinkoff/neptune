package ru.tinkoff.qa.neptune.spring.boot.starter.mock.mvc;

import org.springframework.test.web.servlet.MockMvc;

import static java.util.Objects.nonNull;
import static org.springframework.beans.factory.BeanFactoryUtils.beanNamesForTypeIncludingAncestors;
import static ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

/**
 * Provides an instance of {@link MockMvc}
 */
public final class MockMvcProvider {

    /**
     * @return instance of {@link MockMvc}
     */
    public MockMvc provide() {
        var context = getCurrentApplicationContext();
        var beans = beanNamesForTypeIncludingAncestors(context, MockMvc.class);
        for (var bean : beans) {
            try {
                var result = context.getBean(bean);
                if (nonNull(result)) {
                    return (MockMvc) result;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
