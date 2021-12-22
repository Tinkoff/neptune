package ru.tinkoff.qa.neptune.spring.boot.starter.mock.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

@Configuration
public class MockMvcWrappingConfiguration {

    private final static List<MockMvcWrappingConfiguration> MOCKMVC_WRAPPERS = new CopyOnWriteArrayList<>();

    @Autowired
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
        return mockMvc;
    }

    private ApplicationContext getContext() {
        return context;
    }
}
