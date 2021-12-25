package ru.tinkoff.qa.neptune.spring.boot.starter.mock.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.springframework.beans.factory.BeanFactoryUtils.beanNamesForTypeIncludingAncestors;
import static ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

@Configuration
public class MockMvcWrappingConfiguration {

    private final static Set<MockMvcWrappingConfiguration> MOCKMVC_WRAPPERS = new CopyOnWriteArraySet<>();

    @Autowired(required = false)
    MockMvc mockMvc;

    @Autowired
    ApplicationContext context;

    public MockMvcWrappingConfiguration() {
        super();
        MOCKMVC_WRAPPERS.add(this);
    }

    static MockMvc getMockMvcStatic() {
        var context = getCurrentApplicationContext();
        return MOCKMVC_WRAPPERS
                .stream()
                .filter(mock -> mock.getContext() == context)
                .findFirst()
                .map(MockMvcWrappingConfiguration::getMockMvc)
                .orElse(null);
    }

    private MockMvc getMockMvc() {
        return ofNullable(mockMvc)
                .orElseGet(() -> {
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
                });
    }

    private ApplicationContext getContext() {
        return context;
    }
}
