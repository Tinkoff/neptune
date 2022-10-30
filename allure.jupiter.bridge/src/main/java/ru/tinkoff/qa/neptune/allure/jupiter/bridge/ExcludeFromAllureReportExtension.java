package ru.tinkoff.qa.neptune.allure.jupiter.bridge;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;

import static ru.tinkoff.qa.neptune.allure.jupiter.bridge.NeptuneAllureExcludeTest.getCurrentTestClass;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.excludeFixtureIfNecessary;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.excludeTestResultIfNecessary;

public class ExcludeFromAllureReportExtension implements InvocationInterceptor {

    public ExcludeFromAllureReportExtension() {
        super();
    }


    private <T> T checkCurrentLifeCycleItemAndProceed(Invocation<T> invocation,
                                                      ReflectiveInvocationContext<Method> invocationContext,
                                                      boolean isTest) throws Throwable {
        var method = invocationContext.getExecutable();
        var target = getCurrentTestClass();

        if (isTest) {
            excludeTestResultIfNecessary(target, method);
        } else {
            excludeFixtureIfNecessary(target, method);
        }

        return invocation.proceed();
    }


    @Override
    public void interceptBeforeAllMethod(InvocationInterceptor.Invocation<Void> invocation,
                                         ReflectiveInvocationContext<Method> invocationContext,
                                         ExtensionContext extensionContext) throws Throwable {
        checkCurrentLifeCycleItemAndProceed(invocation, invocationContext, false);
    }

    @Override
    public void interceptAfterAllMethod(Invocation<Void> invocation,
                                        ReflectiveInvocationContext<Method> invocationContext,
                                        ExtensionContext extensionContext) throws Throwable {
        checkCurrentLifeCycleItemAndProceed(invocation, invocationContext, false);
    }

    @Override
    public void interceptBeforeEachMethod(InvocationInterceptor.Invocation<Void> invocation,
                                          ReflectiveInvocationContext<Method> invocationContext,
                                          ExtensionContext extensionContext) throws Throwable {
        checkCurrentLifeCycleItemAndProceed(invocation, invocationContext, false);
    }

    @Override
    public void interceptAfterEachMethod(Invocation<Void> invocation,
                                         ReflectiveInvocationContext<Method> invocationContext,
                                         ExtensionContext extensionContext) throws Throwable {
        checkCurrentLifeCycleItemAndProceed(invocation, invocationContext, false);
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {
        checkCurrentLifeCycleItemAndProceed(invocation, invocationContext, true);
    }

    @Override
    public <T> T interceptTestFactoryMethod(Invocation<T> invocation,
                                            ReflectiveInvocationContext<Method> invocationContext,
                                            ExtensionContext extensionContext) throws Throwable {
        return checkCurrentLifeCycleItemAndProceed(invocation, invocationContext, true);
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation,
                                            ReflectiveInvocationContext<Method> invocationContext,
                                            ExtensionContext extensionContext) throws Throwable {
        checkCurrentLifeCycleItemAndProceed(invocation, invocationContext, true);
    }
}
