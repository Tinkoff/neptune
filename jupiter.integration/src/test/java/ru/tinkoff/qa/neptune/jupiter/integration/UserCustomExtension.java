package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.jupiter.api.extension.*;

public class UserCustomExtension implements BeforeAllCallback, TestInstancePostProcessor,
        BeforeEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, InvocationInterceptor {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {

    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {

    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {

    }
}
