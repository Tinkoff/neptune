package ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import static java.util.Optional.ofNullable;

/**
 * This listeners just intercepts current {@link TestContext}
 */
public final class CurrentApplicationContextTestExecutionListener implements TestExecutionListener {

    private static final ThreadLocal<TestContext> TEST_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public CurrentApplicationContextTestExecutionListener() {
        super();
    }

    /**
     * Returns currently active {@link  ApplicationContext}
     *
     * @return current {@link  ApplicationContext}
     */
    public static ApplicationContext getCurrentApplicationContext() {
        return ofNullable(TEST_CONTEXT_THREAD_LOCAL.get()).map(TestContext::getApplicationContext).orElse(null);
    }

    public void beforeTestClass(TestContext testContext) throws Exception {
        TEST_CONTEXT_THREAD_LOCAL.set(testContext);
    }

    public void prepareTestInstance(TestContext testContext) throws Exception {
        TEST_CONTEXT_THREAD_LOCAL.set(testContext);
    }

    public void beforeTestMethod(TestContext testContext) throws Exception {
        TEST_CONTEXT_THREAD_LOCAL.set(testContext);
    }

    public void beforeTestExecution(TestContext testContext) throws Exception {
        TEST_CONTEXT_THREAD_LOCAL.set(testContext);
    }

    public void afterTestExecution(TestContext testContext) throws Exception {
        TEST_CONTEXT_THREAD_LOCAL.set(testContext);
    }

    public void afterTestMethod(TestContext testContext) throws Exception {
        TEST_CONTEXT_THREAD_LOCAL.set(testContext);
    }

    public void afterTestClass(TestContext testContext) throws Exception {
        TEST_CONTEXT_THREAD_LOCAL.set(testContext);
    }
}
